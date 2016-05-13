var mongo = require('mongodb');
var monk = require('monk');
var db = monk('localhost:27017/ubibike');
var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/ubibike');

var Schema = mongoose.Schema;
var Id = Schema.Types.ObjectId;

var userSchema = new Schema({
    username: String,
    password: String,
    name: {type: String, default: 'undefined'},
    score: Number,
    distance: Number,
    currentBike: {type: Id, default: null},
    trajectories: [{
	    coordinates: [{lat: Number, lon: Number}],
	    points: Number,
	    distance: Number,
	    beginDate: Date,
	    endDate: Date,
	    _id: Id
	}]
});

var bikeSchema = new Schema({
	station: Id,
	name: String,
	reservedBy: Id
});

var stationSchema = new Schema({
	location: {latitude: Number,
			   longitude: Number},
	name: String
});

var processedTransactions = new Schema({
	srcUser: Id,
	srcMessageId: Number
});

User = mongoose.model('User', userSchema);
Bike = mongoose.model('Bike', bikeSchema);
Station = mongoose.model('Station', stationSchema);

function populateDB(){

	var stations = [{name: "Station 1", location: [38.7738718, -9.0958291]},
					{name: "Station 2", location: [38.7354228, -9.1466685]},
					{name: "Station 3", location: [38.7334772, -9.1366397]},
					{name: "Station 4", location: [38.7552882, -9.1163247]}];

	var bikes =	[{name: "bike 1", reservedBy: null},
				{name: "bike 2", reservedBy: null},
				{name: "bike 3", reservedBy: null},
				{name: "bike 4", reservedBy: null}];

	var users = [{username: "user1", password: "pass", name: "Quim",  score: 10, distance: 0, trajectories: [{coordinates: [{lat: 34.43, lng: 54.45}, {lat: 34.47, lng: 54.46}, {lat: 34.38, lng: 54.48}], points: 10, distance: 5000, beginDate: new Date(), endDate: new Date()}], currentBike: null},
				 {username: "user2", password: "pass", name: "Ze", 	  score: 8 , distance: 0, trajectories: [{coordinates: [{lat: 34.43, lng: 54.45}, {lat: 34.47, lng: 54.46}, {lat: 34.38, lng: 54.48}], points: 8, distance: 10000, beginDate: new Date(), endDate: new Date()}], currentBike: null},
				 {username: "user3", password: "pass", name: "Manel", score: 12, distance: 0, trajectories: [{coordinates: [{lat: 34.43, lng: 54.45}, {lat: 34.47, lng: 54.46}, {lat: 34.38, lng: 54.48}], points: 12, distance: 8000, beginDate: new Date(), endDate: new Date()}], currentBike: null} ];

	for(var i=0; i<users.length; i++){
		var user = new User(users[i]);
		user.save();
	}

	for(var i=0; i<stations.length; i++){
		var s = stations[i];
		var station = new Station({
			name: s.name,
			location: {latitude: s.location[0], longitude: s.location[1]}
		});
		console.log(i);
		station.save(function(e, nstation){
			var b = bikes[Number(nstation.name.split(' ')[1])-1];
			console.log(i, bikes);
			var bike = new Bike({
				name: b.name,
				reservedBy: null,
				station: nstation._id
			});
			bike.save();

		});
	}
}

//Station.remove({}, function() {});
//User.remove({}, function() {});
//Bike.remove({}, function() {});

Station.count(function (err, count) {
    if (count == 0) {
    	console.log("Empty collection");
        populateDB();
    }
});

module.exports = {db: db, User: User, Bike: Bike, Station: Station};
