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
    distance: Number,
    trajectories: []
});

var trajectorySchema = new Schema({
    username: String,
    coordinates: [],
    points: Number,
    distance: Number,
    beginDate: Date,
    endDate: Date
})

var bikeSchema = new Schema({
	station: Id
});

var stationSchema = new Schema({
	bikes: [Id],

})

User = mongoose.model('User', userSchema);
Bike = mongoose.model('Bike', bikeSchema);
Station = mongoose.model('Station', stationSchema);
Trajectory = mongoose.model('Trajectory', trajectorySchema);

module.exports = {db: db, User: User, Bike: Bike, Station: Station, Trajectory: Trajectory};
