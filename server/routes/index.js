var express = require('express');
var router = express.Router();
var schemas = require('./db')
var db = schemas.db;
var Test = db.get('test');

var User = schemas.User;
var Bike = schemas.Bike;
var Station = schemas.Station;

router.post('/test', function(req, res, next){
	console.log(req.body);
	Test.findOne({},{},function(e, doc){
		console.log(doc);
        res.json(doc);
    });
});

router.post('/users', function(req, res, next){
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
            res.json({sucess: false, message: "Username doesn't exist"})
        } else {
            res.json(doc[0]);
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
                distance: 0
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

router.post('/stations', function(req, res, next){
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
})

router.get('/requestbike', function(req, res, next){
	var data = req.body;
});

/* Transactions, in the format:
[{srcUser: userId, destUser: userId, timestamp: date, srcMessageId: int, quantity: int}, ...] 
*/
router.get('/transactions', function(req, res, next){
	var data = req.body;
	for(var i=0; i<data.transactions.length; i++){
		var srcUser = transactions[i].srcUser;
		var destUser = transactions[i].destUser;
		var srcMessageId = transactions[i].srcMessageId;
		var quant = transactions[i].quantity;

		User.find({_id: {$in: [srcUser, destUser] }}, function(e, docs){
			if(docs.length == 0){
				console.log("Cannot complete transaction: src user " + srcUser + " and dest user " + destUser + " don't exist.");
			}else if(docs.length == 1){
				if(docs[0]._id == destUser)
					console.log("Cannot complete transaction: src user " + srcUser + " doesn't exist.");
				else
					console.log("Cannot complete transaction: dest user " + destUser + " doesn't exist.");
			}else{
				
				var [src, dest] = (docs[0]._id == srcUser) ? [docs[0], docs[1]] : [docs[1], docs[0]];

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
	}
});


module.exports = router;
