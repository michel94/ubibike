var express = require('express');
var router = express.Router();
var schemas = require('./db')
var db = schemas.db;
var Test = db.get('test');

var Users = schemas.Users;
var Bikes = schemas.Bikes;
var Stations = schemas.Stations

router.post('/test', function(req, res, next){
	console.log(req.body);
	Test.findOne({},{},function(e, doc){
		console.log(doc);
        res.json(doc);
    });
});

router.post('/register', function(req, res, next){
	Users.find({}, {}, function(e, docs){
		res.json(docs);
	})
});

router.post('/login', function(req, res, next){
	console.log(req);
	Users.find({username: req.username, password: password}, {},
		function(e, docs){
			if(docs.length == 1)
				res.json({success: true});
			else
				res.json({success: false});
		})
});


module.exports = router;
