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
    name: String,
    score: Number,
    trajectories: []
});

var bikeSchema = new Schema({
	station: Id
});

var stationSchema = new Schema({
	bikes: [Id],

})

Users = mongoose.model('Users', userSchema);
Bikes = mongoose.model('Bikes', bikeSchema);
Stations = mongoose.model('Stations', stationSchema);

module.exports = {db: db, Users: Users, Bikes: Bikes, Stations: Stations};
