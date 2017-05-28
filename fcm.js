var FCM = require('fcm-push');
exports.sendalarminfcm = function(){
    var serverKey = 'AAAAFOgnUTo:APA91bHV-_fQR8fvmNRQB1zluu1otbR0M8O6zOaA9V1XjfIZCrqlVQFkj60buVBegiKhV5F2wdFsKyveOY8OsGhi8h_nAMeyuyD_dZJ6vOwOAUxY2A6w3Inusua4fVt6Q241zRMoIX6t'; //put your server key here 
    var fcm = new FCM(serverKey);
 
    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera) 
        to: 'flc6KGcLmFM:APA91bG28kYovumVE_z2OH6MTAsaCH6PZTZmZ4rYDjZEB2OYnsYyIwSpghJZRMbw395XHj6WFKBa2DCfszAuKn8caL2QKXysKtkEtX1EZO8nV2dKNzpvZL_uznfyeoILfA44AolKYKFg',

        
        data: {
        'key': 'faff'
        }
    };
   
    fcm.send(message, function(err, response){
        if (err) {
            console.log("Something has gone wrong!");
            console.log(err);
        } else {
            console.log("Successfully sent with response: ", response);
        }
    });
}