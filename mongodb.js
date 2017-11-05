var app = require('./app');
var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/password');
var db = mongoose.connection;
db.once('open', function callback () {
   // add your code here when opening
     console.log("open");
});

var me = require('./mongodb');
exports.test = "";
var md5 = require('md5');

var Schema = mongoose.Schema;
var deviceSchema = new Schema({
    name: String,
    device_date: String,
    device_status: String,
});
var Device = mongoose.model('Device',deviceSchema);

var userSchema = new Schema({
   name: String,
   password: String
});
//var AduinoList = new Device({namaaae:"1abc",device_data:2});

var AduinoList = new Device();
mongoose.model('Users',userSchema);
mongoose.model('Logs',deviceSchema);
var User = mongoose.model('Users');
var Log = mongoose.model('Logs');

exports.savelog = function(name,date ,status) {
    var log = new Log({
        name: name,
   device_date: date,
        device_status: status
    });

    log.save(function(err, log) {
        if(err) return console.error(err);
        console.dir(log);
    });
}




exports.savePw = function(name, pw) {
    var user = new User({
        name: name,
//   password: pw
        password: md5(pw)
    });

    user.save(function(err, user) {
        if(err) return console.error(err);
        console.dir(user);
    });
}

exports.checkPw = function(pw, callback) {
    User.find({
//   password: pw
        password: md5(pw)
    }, function(err, users) {
        if (err) return console.error(err);        
        me.test = String(users.length);
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