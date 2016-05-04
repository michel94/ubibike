var express = require('express');
var router = express.Router();
var schemas = require('./db')
var db = schemas.db;
var Test = db.get('test');

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

router.post('/trajectories/listByUser', function(req, res, next){
    console.log(req.body);
    var data = req.body;
	Trajectory.find({username: data.username}, {}, function(e, doc){
        console.log(doc);
        if(doc == null || doc.length == 0){
            res.json({sucess: false, message: "Username doesn't exist"})
        } else {
            var trajectories = [];
            for(var i=0; i!= doc.length; i++){
                trajectories.push({
                    "_id": doc[i]._id,
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
    console.log(req.body);
    var data = req.body;
    Trajectory.findOne({_id: data._id}, {}, function(e, doc){
        console.log(doc);
        if(doc == null || doc.length == 0){
            res.json({sucess: false, message: "Wrong trajectory id"})
        } else {
            res.json({success:true, trajectories: [doc]});
        }
    });
})

router.get('/trajectories', function(req, res, next){
    console.log(req.body);
    var data = req.body;
    Trajectory.find({},{}, function(e, docs){
        console.log(docs);
        var trajectories = [];
        for(var i=0; i!= docs.length; i++){
            trajectories.push({
                "_id": docs[i]._id,
                "points": docs[i].points,
                "beginDate": docs[i].beginDate,
                "endDate": docs[i].endDate
            })
        }
        res.json({success: true, trajectories: trajectories})
    })
})

module.exports = router;
