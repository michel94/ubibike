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
    trajectories: [],
    currentBike: {type: Id, default: null}
});

var trajectorySchema = new Schema({
    username: String,
    user: 
    coordinates: [],
    points: Number,
    distance: Number,
    beginDate: Date,
    endDate: Date
})

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
Trajectory = mongoose.model('Trajectory', trajectorySchema);

function populateDB(){

	var stations = [{name: "Station 1", location: [38.7738718, -9.0958291]},
					{name: "Station 2", location: [38.7354228, -9.1466685]},
					{name: "Station 3", location: [38.7334772, -9.1366397]},
					{name: "Station 4", location: [38.7552882, -9.1163247]}];

	var bikes =	[{name: "bike 1", reservedBy: null},
				{name: "bike 2", reservedBy: null},
				{name: "bike 3", reservedBy: null},
				{name: "bike 4", reservedBy: null}];

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

Station.count(function (err, count) {
    if (count == 0) {
    	console.log("Empty collection");
        populateDB();
    }
});

module.exports = {db: db, User: User, Bike: Bike, Station: Station, Trajectory: Trajectory};
