var express = require('express')
    ,http = require('http')
    ,app = express();
var port = 3000;

var date_utils = require('date-utils');
var date = new Date();
var time = date.toFormat('YYYY-MM-DD HH24:MI:SS');

var db = require('./mongodb');
var me = require('./app');
var fs = require('fs');
var os = require('os');
var fcm = require('./fcm');
// var serial = require('./serial');

////////////////////////////////////////상태확인////////////////////////////////////////////

app.post('/doorStatus', function(req, res) {
    serial.sendmsg1('4');
    
    setTimeout(function(){
        console.log("new status"+serial.rcv_msg1);
        res.write(String(serial.rcv_msg1));
        res.end();
    },1000);
});

app.post('/windowStatus', function(req, res) {
    serial.sendmsg2('4');

    //setTimeout(function(){
    //    console.log("new status"+serial.rcv_msg2);
    //    res.write(String(serial.rcv_msg2));
    //   res.end();
    //},2000);
    console.log("new status"+serial.rcv_msg2);
    res.write(String(serial.rcv_msg2));
    res.end();
});

app.post('/window2Status', function(req, res) {
    serial.sendmsg3('4');

    console.log("new status"+serial.rcv_msg3);
    res.write(String(serial.rcv_msg3));
    res.end();
});



app.post('/Status', function(req, res) {
    serial.sendmsg1('4');
    serial.sendmsg2('4');
    serial.sendmsg3('4');
    console.log("all new status : "+serial.rcv_status);
    res.write(String(serial.rcv_status));
    res.end();
});

////////////////////////////////////////비밀번호////////////////////////////////////////////
app.post('/pwExist', function(req, res) {
    db.pwExist(function(err, c) {
        if(err) throw err;
        console.log("turple number is " + c);
        res.write(String(c));
        res.end();
    });
});

app.post('/checkPw', function(req, res) {
     var chunk = "";
     req.on('data', function(data){
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        console.log("input Password: "+String(chunk.pw));

        db.checkPw(String(chunk.pw), function(err, length) {
        if(err) throw err;
        console.log(length);
        res.write(String(length));
        res.end();
        });
        setTimeout(function (){
            console.log("isaac:" + db.test);                
        },500);

    });
});

////////////////////////////////////현관문 여닫기///////////////////////////////////////
/*app.post('/door',function(req,res){
    var chunk = "";
    console.log("the door is open");
    req.on('data', function(data){
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        console.log("name : "+chunk.name);
    });
    while(serial.switch == 0){}
    res.write(serial.string);
    serial.switch = 0;
    res.end();
    //serial.sendmsg("1");
});*/

// app.post('/fcm',function(req,res){
//     var chunk = "";
//     req.on('data', function(data){
//         chunk = JSON.parse(data);
//     });
//     req.on('end',function(){
        
//         console.log("the token is "+ chunk.name);
//    fcm.fcm_serial = chunk.name;
//     });
//     //setTimeout(function(){
//     //fcm.sendalarminfcm();
//     //},1000);
    

// });


app.post('/door',function(req,res){
    var chunk = "";
    req.on('data', function(data){
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        console.log("name : "+chunk.name);
    });
    setTimeout(function(){
   console.log(chunk.name);
   switch(String(chunk.name)){
      case "49" :
         console.log("1 checked sending to door");
         serial.sendmsg1('1');
         break;
      case "50" :
         console.log("2 checked sending to door");
         serial.sendmsg1('2');
         break;
      case "53" :
         console.log("5 checked sending to door");
         serial.sendmsg1('5');
         break;
   }
   },500);
    //db.savelog(,,)
    // setTimeout(function(){
    //     console.log(serial.rcv_msg1);
    //     res.write(serial.rcv_msg1);
    //    res.end();
    // },2000);
});

app.post('/window',function(req,res){
    var chunk = "";
    req.on('data', function(data){
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        console.log("name : "+chunk.name);
    });
    setTimeout(function(){
   console.log(chunk.name);
   switch(String(chunk.name)){
      case "49" :
         console.log("1 checked sending to window");
         serial.sendmsg2('1');
         break;
      case "50" :
         console.log("2 checked sending to window");
         serial.sendmsg2('2');
         break;
      case "53" :
         console.log("5 checked sending to window");
         serial.sendmsg2('5');
         break;
   }
   },500);
    // setTimeout(function(){
    //     console.log(serial.rcv_msg2);
    //     res.write(serial.rcv_msg2);
    //    res.end();
    // },2000);
});

app.post('/window2',function(req,res){
    var chunk = "";
    req.on('data', function(data){
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        console.log("name : "+chunk.name);
    });
    setTimeout(function(){
   console.log(chunk.name);
   switch(String(chunk.name)){
      case "49" :
         console.log("1 checked sending to window");
         serial.sendmsg3('1');
         break;
      case "50" :
         console.log("2 checked sending to window");
         serial.sendmsg3('2');
         break;
      case "53" :
         console.log("5 checked sending to window");
         serial.sendmsg3('5');
         break;
   }
   },500);
    // setTimeout(function(){
    //     console.log(serial.rcv_msg2);
    //     res.write(serial.rcv_msg2);
    //    res.end();
    // },2000);
});



///////////////////////////////////////////////////////////////////////////////////////
app.post('/makePw', function(req, res) {
     var chunk = "";
     req.on('data', function(data){
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        console.log("pw : "+chunk.pw);
        db.savePw("host",String(chunk.pw));
    });

    
    res.write("password was made");
    res.end();
});

// app.get('/main',function(req,res){
//     fs.readFile('main.html',function(error,data){
//     res.writeHead(200,{'Content-Type' : 'text/html'});
//     res.end(data);
//     });
// });

// app.get('/imgs',function(req,res){
//     console.log("getting image.");
//     fs.readFile('image.jpg',function(error,data){
//     res.writeHead(200,{'Content-Type' : 'text/html'});
//     res.end(data);
//     });
// });

app.post('/',function(req,res){
    var chunk = ""; // req.body;
    //데이터를 가져옵니다.
    console.log("요청들어옴");
    req.on('data', function(data){
        //데이터를 JSON으로 파싱합니다.
        console.log(data);
        chunk = JSON.parse(data);
        console.log(chunk);
    });
    req.on('end',function(){
        //파싱된 데이터를 확인합니다.
        console.log("name : "+chunk.name);
    });
    // 아래의 OK라는 내용이 안드로이드의 ReadBuffer를 통해
    // result String으로 바뀝니다.
    res.write(chunk);
    res.end();
    
});

//////////////////////////////get server ipaddress///////////////////////

// function getServerIp() {
//     var ifaces = os.networkInterfaces();
//     var result = '';
//     for (var dev in ifaces) {
//         var alias = 0;
//         ifaces[dev].forEach(function(details) {
//             if (details.family == 'IPv4' && details.internal === false) {
//                 result = details.address;
//                 ++alias;
//             }
//         });
//     }
//     fcm.server_ipaddress = result;
//     return result;
// }


/*
function getServerIp(){
const getIP = require('external-ip')();
 
getIP((err, ip) => {
    if (err) {
        // every service in the list has failed 
        throw err;
    }
    fcm.server_ipaddress = ip;
    console.log(ip);
});
}
*/
///////////////////////////////////



// app.post('/aduino',function(req,res){
//     var chunk = "";
//     //데이터를 가져옵니다.
//     req.on('data', function(data){
//         //데이터를 JSON으로 파싱합니다.
//         console.log(data);
//         chunk = JSON.parse(data);
//     });
//     req.on('end',function(){
//         //파싱된 데이터를 확인합니다.
//         console.log("name : "+chunk.name);
//         db.device.save({device_id:chunk.device_id,
//                         device_data:chunk.device_data, 
//                         device_status: chunk.device_status, 
//                         device_sort: chunk.device_sort,
//                         device_date: chunk.device_data
//                        });
//     });
//     // 아래의 OK라는 내용이 안드로이드의 ReadBuffer를 통해
//     // result String으로 바뀝니다.
//     res.write("OK");
//     res.end();
// });


//////////////////////////////////////////
// exports.capture = function(){
// var exec = require('child_process').exec;
// exec("wget http://127.0.0.1:8080/?action=snapshot -O image.jpg", function (error, stdout, stderr) {
//    console.log(stdout);
// });
// }



// //choose between two
// console.log(getServerIp());
//fcm.server_ipaddress = '192.168.0.12';

app.listen(port);
console.log(time);