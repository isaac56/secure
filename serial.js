//아두이노와의 통신
var SerialPort = require('serialport');
var port1 = new SerialPort('/dev/rfcomm0',{baudraate:9600, parser:SerialPort.parsers.readline('\n')});
port1.on('open', function() {
  port1.write('1', function(err) {
    if (err) {
      return console.log('Error on write: ', err.message);
    }
    console.log('1 written');
  });
});
 
port1.on('data', function (data) {
  console.log('Read Data : ' + data);
});

port1.on('error', function(err) {
  console.log('Error: ', err.message);
});


출처: http://hinco.tistory.com/8 [생각 정리소]
exports.sendmsg = function(msg){
    port1.write(msg, function(err){
    if (err){
	console.log("It's in sendmsg");
        console.log(err.message);
        return ;
    }    
        console.log(msg + ' sent ');    
    })
}