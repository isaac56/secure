var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/data');
var db = mongoose.connection;
db.once('open', function callback () {
	// add your code here when opening
  	console.log("open");
});

var md5 = require('md5');

var Schema = mongoose.Schema;
var deviceSchema = new Schema({
    name: String,
    device_data: Number,
    device_status: String,
    device_sort: String,
    device_date: String
});
var Device = mongoose.model('Device',deviceSchema);

var userSchema = new Schema({
    name: String,
    password: String
});
var User = mongoose.model('User', userSchema);

//var AduinoList = new Device({name:"1abc",device_data:2});

var AduinoList = new Device();

exports.saveinmongo = function() {
    AduinoList.save(function(err){
        if (err) console.log(err);
    });
}

exports.updateinmongo = function() {
    Device.update({name:"1abc"},{$set:{device_data:3}}, function(err){
    if(err) console.log(err);
    });
}

exports.findinmongo = function(){
    Device.find({name:"1abc"},function (err, Devices) {
    if (err) return console.error(err);
    console.log(Devices);
    });
}

exports.savePw = function(name, pw) {
    var user = new User({
        name: name,
        password: md5(pw)
    });

    user.save(function(err, user) {
        if(err) return console.error(err);
        console.dir(user);
    });
}

exports.checkPw = function(pw, callback) {
    User.find({
        password: md5(pw)
    }, function(err, users) {
        if (err) return console.error(err);        
        callback(null, users.length);
    });
}

exports.pwExist = function(callback) {
    User.count({name: 'host'},function(err, c) {
        if (err) throw err;
        console.log(c);
        callback(null, c);
    });
}