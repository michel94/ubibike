var express = require('express');
var router = express.Router();
var schemas = require('./db')
var db = schemas.db;
var Test = db.get('test');

var Users = schemas.Users;
var Bikes = schemas.Bikes;
var Stations = schemas.Stations

router.get('/test', function(req, res, next){
	Test.find({},{},function(e,docs){
        res.json(docs);
    });
});



module.exports = router;
