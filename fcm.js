var FCM = require('fcm-push');

var me = require('./fcm');
exports.fcm_serial = '';
exports.server_ipaddress = '';
exports.sendalarminfcm = function(){
    var serverKey = 'AAAAFOgnUTo:APA91bHV-_fQR8fvmNRQB1zluu1otbR0M8O6zOaA9V1XjfIZCrqlVQFkj60buVBegiKhV5F2wdFsKyveOY8OsGhi8h_nAMeyuyD_dZJ6vOwOAUxY2A6w3Inusua4fVt6Q241zRMoIX6t'; //put your server key here 
    var fcm = new FCM(serverKey);
    console.log("ip address is "+me.server_ipaddress);
    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera) 
        to: me.fcm_serial,
        data: {
        'key': 'I am a Juno slave who my team want to make',
   'link': 'http://'+me.server_ipaddress+':3000/main'
        }
    };
   
    fcm.send(message, function(err, response){
        if (err) {
            console.log("Something has gone wrong!");
            console.log(err);
        } else {
            console.log("Successfully sent with response: ");
        }
    });
}