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
        res.json({users: docs});
    });
});

router.post('/register', function(req, res, next){
	var data = req.body;
	console.log(data);
	User.findOne({username: data.username}, {}, function(e, doc){
		console.log(data);
		if(doc != null)
			res.json({success: false, reason: "Username already exists"});
		else{
			var user = new User({
				username: data.username,
				password: data.password
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
		function(e, doc){
			if(doc != null)
				res.json({success: true});
			else
				res.json({success: false});
		})
});


module.exports = router;
