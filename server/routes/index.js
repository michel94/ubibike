var express = require('express');
var router = express.Router();
var schemas = require('./db')
var db = schemas.db;
var Test = db.get('test');

var ObjectID = require('mongodb').ObjectID

var User = schemas.User;
var Bike = schemas.Bike;
var Station = schemas.Station;
var Trajectory = schemas.Trajectory;

router.get('/', function(req, res, next){
    res.render('public/index')
})

router.get('/test', function(req, res, next){
	console.log(req.body);
	Test.findOne({},{},function(e, doc){
		console.log(doc);
        res.json(doc);
    });
});

router.get('/users', function(req, res, next){
	console.log(req.body);
	User.find({},{},function(e, docs){
		console.log(docs);
        res.json({users: docs});
    });
});

router.post('/userStats', function(req, res, next){
    console.log(req.body);
    var data = req.body;
    User.find({username: data.username}, {}, function(e, doc){
        console.log(doc);
        if(doc == null || doc.length == 0){
            res.json({success: false, message: "Username doesn't exist"})
        } else {
            res.json({success:true, user: doc[0]});
        }
    })
})

router.post('/register', function(req, res, next){
	var data = req.body;
	console.log(data);
	User.findOne({username: data.username}, {}, function(e, doc){
		console.log(data);
		if(doc != null)
			res.json({success: false, message: "Username already exists"});
		else{
			var user = new User({
				username: data.username,
				password: data.password,
                score: 0,
                distance: 0,
                currentBike: null,
                trajectories: []
			});
			user.save();
			res.json({success: true});
		}

	})
});

router.post('/login', function(req, res, next){
	var data = req.body;
	console.log(data);
	User.findOne({username: data.username, password: data.password}, {},
		function(e, user){
			if(user != null)
				res.json({success: true, userID: user._id});
			else
				res.json({success: false});
		})
});

router.get('/stations', function(req, res, next){
	Station.find({}, {}, function(e, docs){
		res.json({stations: docs});
	});
});

router.post('/stationInfo', function(req, res, next){
	var data = req.body;
	console.log("StationId: ", data.stationId);
	Station.findById(data.stationId, function(e, station){
		Bike.find({station: data.stationId}, {}, function(e, bikes){
			res.json({station: station, bikes: bikes});
		})
	});
});

Bike.find({}, {}, function(e, docs){
	for(var i=0; i<docs.length; i++){
		docs[i].reservedBy = null;
		docs[i].save()
	}
});

User.find({}, {}, function(e, docs){
	for(var i=0; i<docs.length; i++){
		docs[i].currentBike = null;
		docs[i].save()
	}
});

function hasBike(userId, callback){
	User.findById(userId, function(e, user){
        callback(user, user.currentBike != null);
	});
}

router.post('/requestBike', function(req, res, next){
	var data = req.body;
	console.log("User " + data.user +  " is requesting bike " + data.bike);
	hasBike(data.user, function(user, has){
		if(!has){
			Bike.findById(data.bike, function(e, bike){
				console.log(bike);
				if(bike.reservedBy == null){
					user.currentBike = bike._id;
					bike.reservedBy = user._id;
					user.save();
					bike.save();
					res.json({success: true});
				}else{
					res.json({success: false, reason: "This bike is already requested"});
				}
			})
		}else{
			res.json({success: false, reason: "User has already requested a bike"});
		}

	});
});

router.post('/returnBike', function(req, res, next){
	var data = req.body;
    console.log("\n" + JSON.stringify(data) + "\n");
	console.log("User " + data.user +  " is returning his bike to station " + data.station);
	hasBike(data.user, function(user, has){
		if(has){
            console.log("User "  + JSON.stringify(user))
			Bike.findById(user.currentBike, function(e, bike){
				if(bike){
					user.currentBike = null;
					bike.reservedBy = null;
					bike.station = data.station;
					user.save();
					bike.save();
				}else{
					res.json({success: false, reason: "Critical error: bike doesn't exist"});
				}
			});
		}else{
			res.json({success: false, reason: "User has not requested a bike"});
		}
	});
});

/* Transactions, in the format:
[{srcUser: userId, destUser: userId, timestamp: date, srcMessageId: int, quantity: int}, ...] 
*/
router.post('/transactions', function(req, res, next){
	var data = req.body;
    console.log(JSON.stringify(data));
    //res.json({success: true})
    var transactions = data.transactions;
    var error = true;
    for(var i=0 ; i<transactions.length; i++){
        var item = transactions[i];
        if(item.type == "trip"){
            var trajectory = JSON.parse(item.trajectory);
            if(trajectory.coordinates.length != 0){
                    User.findByIdAndUpdate(trajectory.user_id, {$push: {"trajectories" : trajectory}}, 
                                           { safe: true, upsert: true}, function(err, model){
                    if(err){
                        console.log(err);
                        error = true;
                    } else {
                        error = false;
                    }
                })    
            } else {
                error = true;
            }
        }
    }
    
    if(error){
        res.json({success: false, message: "an error occured"});
    } else {
        res.json({success: true});
    }
	/*for(var i=0; i<data.transactions.length; i++){
		var srcUser = transactions[i].srcUser;
		var destUser = transactions[i].destUser;
		var srcMessageId = transactions[i].srcMessageId;
		var quantity = transactions[i].quantity;

		User.find({_id: {$in: [srcUser, destUser] }}, function(e, docs){
			if(docs.length == 0){
				console.log("Cannot complete transaction: src user " + srcUser + " and dest user " + destUser + " don't exist.");
			}else if(docs.length == 1){
				if(docs[0]._id == destUser)
					console.log("Cannot complete transaction: src user " + srcUser + " doesn't exist.");
				else
					console.log("Cannot complete transaction: dest user " + destUser + " doesn't exist.");
			}else{
				
				var l = (docs[0]._id == srcUser) ? [docs[0], docs[1]] : [docs[1], docs[0]];
				var src = l[0];
				var dest = l[1];

				processedTransactions.findOne({srcUser: srcUser, srcMessageId: srcMessageId}, {}, function(e, docs){
					if(docs.length == 0){ // fresh transaction
						src.score -= quantity;
						dest.score += quantity;
						src.save();
						dest.save();
					}else{ // old transaction
						console.log("Old transaction, ignoring");

					}
				});
			}

		});
		res.json({success: true});
	}*/
});


router.post('/trajectories/create', function(req, res, next){
    var data = req.body;
    console.log(data);
    var trajectory = new Trajectory({
        username: data.username,
        coordinates: data.coordinates,
        points: data.points,
        distance: data.distance,
        beginDate: data.beginDate,
        endDate: data.endDate
    });
    
    trajectory.save(function(err) { 
        if(err) console.log(err);
        else
            res.json({sucess: true,
             trajectory: trajectory})
            
    });
    
})

router.get('/trajectories/listByUser', function(req, res, next){
    console.log(req.body);
    var data = req.body;
	User.find({username: data.username}, {}, function(e, doc){
        console.log(doc);
        if(doc == null || doc.length == 0){
            res.json({sucess: false, message: "Username doesn't exist"})
        } else {
            var trajectories = [];
            for(var i=0; i!= doc.length; i++){
                trajectories.push({
                    "_id": doc[i].beginDate,
                    "points": doc[i].points,
                    "beginDate": doc[i].beginDate,
                    "endDate": doc[i].endDate
                })
            }
            res.json({success:true, trajectories: trajectories});
        }
    })
});

router.post('/trajectories/info', function(req, res, next){
    var data = req.body;
    console.log(req.body);
    User.findById(data.user, {}, function(e, user){
        console.log(user);
        if(user == null || user.length == 0){
            res.json({success: false, message: "Wrong trajectory id", trajectories: []});
        } else {
            var docs = user.trajectories;
            for(var i=0; i<docs.length; i++) {
                if(docs[i].beginDate == data._id){
                    console.log("BEIN DATE: " + docs[i]._id);
                    docs[i]._id = data._id;
                    res.json({success: true, message: "", trajectories: [docs[i]]});
                    return;
                }
            }
            res.json({success:false, message: "Not trajectory found"});
        }
    });
})

router.post('/trajectories', function(req, res, next){
	console.log("Trajectories");
    console.log(req.body);
    var data = req.body;
    User.findById(data.user, function(e, doc){
        console.log(doc);
        if(!doc || doc.length == 0){
        	res.json({success: false})
        	console.log("Response 0");
        }else{
	        var r = doc.trajectories;
	        var trajectories = [];
	        for(var i=0; i != r.length; i++){
	            trajectories.push({
	                "_id": r[i].beginDate,
	                "points": r[i].points,
	                "beginDate": r[i].beginDate,
	                "endDate": r[i].endDate
	            })
	        }
			res.json({success: true, trajectories: trajectories});
        }
        
    });

});

module.exports = router;