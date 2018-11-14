var callboard = callboard || {};

stompClient = null;

callboard.connect = function(){
	var headers = {};
	var csrf = {};
	//csrf = core.csrf(); 
	//headers[csrf.headerName] = csrf.token;
    var socket = new SockJS( '/asterisk-control/ws2');
    stompClient = Stomp.over(socket);
    stompClient.connect( {}, function (frame) {
    	stompClient.subscribe('/topic/sender2', function( message ) {
    		units.translateMessage(JSON.parse(message.body).content);
        	
        });
    });
}
callboard.translateMessage = function(message){
	$("#config-file-text").text(message);
}
callboard.disconnect = function(){
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}
callboard.init = function(){
	
	//core.getJSconfig();
	callboard.connect();
	//callboard.sendMessage("hello");
}
callboard.sendMessage = function( command ){
	   
    var pbxId = $("#unit-pbx-id").val();
    
	stompClient.send( core.jsconfig.serverContextPath  + "/receiver2", {}, JSON.stringify({'id': pbxId, 'command' : command}));	
}

$(document).ready( function()
{
			
	callboard.init();
});

