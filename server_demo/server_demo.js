var express = require('express');
var app = express();
var fs = require("fs");
var net = require('net');


app.get('/', function (req, res) {
    fs.readFile( __dirname + "/" + "version.json", 'utf8', function (err, data) {
        //console.log( data );
        res.send( data );
		console.log("一个连接\n");
    });
});

app.get('/app/Appupdate.apk',function(req,res,next){
  //..db get file realpath
  console.log("一个下载连接\n");
  var realpath = __dirname + "/app/Appupdate.apk";
  var filename = "Appupdate.apk";
  res.download(realpath,filename);
});

var server = app.listen(8081, function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log("应用实例，访问地址为 http://%s:%s", host, port);
});
var socket_server = net.createServer(function(socket){
	//socket.end("good bye!");
	socket.write("hello,i'm server!");
	console.log("client connected! %j:%j",socket.remoteAddress,socket.remotePort);
	socket.on("data",function(data){
		console.log("recived from client:",data.toString());
	})
	socket.on("close",function(had_error){
		if(!had_error){
			console.log("client closed success! %j:%j",socket.remoteAddress,socket.remotePort);
		}
		else{
			console.log("client close error! %j:%j",socket.remoteAddress,socket.remotePort);
		}
	})
	socket.on("error",function(err){
		console.log("!!!err!!!",err);
	});
	//setTimeout(function(){
	//    socket.end("我结束了","utf8");
	//},3000);
});
socket_server.listen(server);