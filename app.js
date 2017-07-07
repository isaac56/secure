var express = require('express')
    ,http = require('http');

//객체생성.
var app = express();
var port = 3000;

var db = require('./mongodb');

app.post('/checkPw', function(req, res) {
     var chunk = "";
     req.on('data', function(data){
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        console.log(String(chunk.pw));

        db.checkPw(String(chunk.pw), function(err, length) {
        if(err) throw err;
        console.log(length);
        res.write(String(length));
        res.end();
    });
    });


});

app.post('/pwExist', function(req, res) {
    db.pwExist(function(err, c) {
        if(err) throw err;
        console.log(c);
        res.write(String(c));
        res.end();
    });
});

app.post('/makePw', function(req, res) {
     var chunk = "";
     var pw = "dasdf";
     req.on('data', function(data){
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        console.log("pw : "+chunk.pw);
        db.savePw("host",String(chunk.pw));
    });

    //pw = String(chunk.pw);
    
    
    res.write("password was made");
    res.end();
});

app.get('/',function(req,res){
    console.log("get으로 들어왔습니다.");
});


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

app.post('/door',function(req,res){
    var chunk = "";
    console.log("the door is open");
    req.on('data', function(data){
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        console.log("name : "+chunk.name);
    });
    res.write("doorisokay");
    res.end();
    //serial.sendmsg("1");
});


app.post('/aduino',function(req,res){
    var chunk = "";
    //데이터를 가져옵니다.
    req.on('data', function(data){
        //데이터를 JSON으로 파싱합니다.
        console.log(data);
        chunk = JSON.parse(data);
    });
    req.on('end',function(){
        //파싱된 데이터를 확인합니다.
        console.log("name : "+chunk.name);
        db.device.save({device_id:chunk.device_id,
                        device_data:chunk.device_data, 
                        device_status: chunk.device_status, 
                        device_sort: chunk.device_sort,
                        device_date: chunk.device_data
                       });
    });
    // 아래의 OK라는 내용이 안드로이드의 ReadBuffer를 통해
    // result String으로 바뀝니다.
    res.write("OK");
    res.end();
});




/*

*/

//푸시알림을 위한 구글 fcm 통신

//var fcm = require('./fcm');
//fcm.sendalarminfcm();
//var mongodb = require('./mongodb');
//mongodb.saveinmongo();
//mongodb.findinmongo();
//var serial = require('./serial');
//serial.sendmsg("hi");



app.listen(port);
console.log('서버가 운영중입니다.');