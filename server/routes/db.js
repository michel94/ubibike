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
	    coordinates: [{lat: Number, lng: Number}],
	    points: Number,
	    distance: Number,
	    beginDate: String,
	    endDate: String
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

var transactionSchema = new Schema({
	srcUser: Id,
	srcMessageId: Number
});


User = mongoose.model('User', userSchema);
Bike = mongoose.model('Bike', bikeSchema);
Station = mongoose.model('Station', stationSchema);
Transaction = mongoose.model('Transaction', transactionSchema);

function populateDB(){

	var stations = [{name: "Saldanha", location: [38.715, -9.14108]},
					{name: "Rossio", location: [38.7262, -9.14944]},
					{name: "Alameda", location: [38.736946, -9.136567]}];

	for(var i=0; i<stations.length; i++){
		var s = stations[i];
		var station = new Station({
			name: s.name,
			location: {latitude: s.location[0], longitude: s.location[1]}
		});
		console.log(i);
		station.save(function(e, nstation){
			if(nstation.name == "Saldanha"){
				var bike = new Bike({
					name: "BK2",
					reservedBy: null,
					station: nstation._id
				});
				bike.save();
				var bike = new Bike({
					name: "BK3",
					reservedBy: null,
					station: nstation._id
				});
			}else if(nstation.name == "Rossio"){
				var bike = new Bike({
					name: "BK1",
					reservedBy: null,
					station: nstation._id
				});
				bike.save();
			}else if(nstation.name == "Alameda"){
				var bike = new Bike({
					name: "BK4",
					reservedBy: null,
					station: nstation._id
				});
				bike.save();
				var bike = new Bike({
					name: "BK5",
					reservedBy: null,
					station: nstation._id
				});
				bike.save();
			}
		});
	}
}

/*Station.remove({}, function() {});
User.remove({}, function() {});
Bike.remove({}, function() {});*/

Station.count(function (err, count) {
    if (count == 0) {
    	console.log("Empty collection");
        populateDB();
    }
});

module.exports = {db: db, User: User, Bike: Bike, Station: Station, Transaction: Transaction};
