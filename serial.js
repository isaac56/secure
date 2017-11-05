
var SerialPort = require('serialport');
var port1 = new SerialPort('/dev/rfcomm0',{baudraate:9600, parser:SerialPort.parsers.readline('\n')});
var port2 = new SerialPort('/dev/rfcomm1',{baudraate:9600, parser:SerialPort.parsers.readline('\n')});
var port3 = new SerialPort('/dev/rfcomm2',{baudraate:9600, parser:SerialPort.parsers.readline('\n')});
var db = require('./mongodb');
var app = require('./app');
var me = require('./serial');
var fcm = require('./fcm');
exports.rcv_msg1 = 0;
exports.rcv_msg2 = 0;
exports.rcv_msg3 = 0;
exports.rcv_status = 0;
var tmp_rcv_msg2=0;
var tmp_rcv_msg3 =0;
var emergency = 0;
port1.on('open', function() {


});

port1.on('data', function (data) {
  console.log('Read Data from 1 : ' + data);
  var date = new Date();
  var time = date.toFormat('YYYY-MM-DD HH24:MI:SS');
  if(me.rcv_msg1!=(7&Number(String(data)))){
   db.savelog("door",time,String(me.rcv_msg1) +"->"+ String(data));
   console.log("door log was saved");
  }
  me.rcv_msg1 = 7&(Number(String(data)));
  
  me.rcv_status = (504&me.rcv_status)+(me.rcv_msg1);
  if(Number(data) == 3 && emergency == 0){
   console.log("emergency from 1");
   fcm.sendalarminfcm();
   setTimeout(function(){
   app.capture();
    },3000);
   emergency= 1;
  }else if(Number(data) == 2 || Number(data) == 1){
      emergency= 0;
   }
});
port1.on('error', function(err) {
  //console.log('Error: ', err.message);
   console.log('port1 is error');
});

port2.on('open', function() {


});

port2.on('data', function (data) {
  console.log('Read Data from 2 : ' + data);
  var date = new Date();
  var time = date.toFormat('YYYY-MM-DD HH24:MI:SS');
  if(me.rcv_msg2!=(56&(tmp_rcv_msg2 <<3))){
   db.savelog("window",time,String(me.rcv_msg2) +"->"+ String(data));
   console.log("window log was saved");
  }
  me.rcv_msg2 = 56&(Number(String(data))<<3);
  me.rcv_status = (455&me.rcv_status)+me.rcv_msg2;
  console.log('string e' + me.rcv_msg2);
  if(Number(data) == 3 && emergency == 0){
   console.log("emergency from 2");
   fcm.sendalarminfcm();
   setTimeout(function(){
   app.capture();
    },3000);
   emergency= 1;

}else if(Number(data) == 2 || Number(data) == 1){
      emergency= 0;
   }
   
});

port2.on('error', function(err) {
  //console.log('Error: ', err.message);
   console.log('port2 is error');
});


port3.on('open', function() {


});

port3.on('data', function (data) {
  console.log('Read Data from 3 : ' + data);
  var date = new Date();
  var time = date.toFormat('YYYY-MM-DD HH24:MI:SS');
  if(me.rcv_msg3!=(448&(Number(String(data)) <<6))){
   db.savelog("window2",time,String(me.rcv_msg3) +"->"+ String(data));
   console.log("window2 log was saved");
  }
  me.rcv_msg3 = 448&(Number(String(data)) <<6);
  console.log(me.rcv_msg3);
  me.rcv_status = (63&me.rcv_status)+me.rcv_msg3;

  if(Number(data) == 3 && emergency == 0){
   console.log("emergency from 3");
   fcm.sendalarminfcm();
   setTimeout(function(){
   app.capture();
    },3000);
   emergency= 1;
  }else if(Number(data) == 2 || Number(data) == 1){
      emergency= 0;
   }
});
port3.on('error', function(err) {
  //console.log('Error: ', err.message);
   console.log('port3 is error');
});


exports.sendmsg1 = function(msg, callback){
  port1.write(msg, function(err){
    if (err){
   console.log('failed to sendmsg1');
      //console.log(err);
      return;
    }
     
    console.log("sent message to 1 : " + msg);
  })
}

exports.sendmsg2 = function(msg, callback){
  port2.write(msg, function(err){
    if (err){
      //console.log(err);
      console.log('failed to sendmsg2');
   return;
    }
    console.log("sent message to 2 : " + msg);
  })
}

exports.sendmsg3 = function(msg, callback){
  port3.write(msg, function(err){
    if (err){
      //console.log(err);
      console.log('failed to sendmsg3');
   return;
    }
    console.log("sent message to 3 : " + msg);
  })
}


