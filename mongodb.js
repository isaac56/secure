var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/test');
var db = mongoose.connection;
db.once('open', function callback () {
	// add your code here when opening
  	console.log("open");
});

var Schema = mongoose.Schema;
var deviceSchema = new Schema({
    name: String,
    device_data: Number,
    device_status: String,
    device_sort: String,
    device_date: String
});
var Device = mongoose.model('Device',deviceSchema);

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