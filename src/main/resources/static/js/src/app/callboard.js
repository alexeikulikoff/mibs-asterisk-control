var callboard = callboard || {};

stompClient = null;

callboard.connect = function(){
	var headers = {};
	var csrf = {};
	//csrf = core.csrf(); 
	//headers[csrf.headerName] = csrf.token;
    var socket = new SockJS( '/asterisk-control/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect( {}, function (frame) {
    	stompClient.subscribe('/topic/sender', function( message ) {
    		callboard.translateMessage(JSON.parse(message.body).content);
        	
        });
    });
}
callboard.translateMessage = function(message){
	console.log(message);
	$("#config-file-text").html(message);

}
callboard.disconnect = function(){
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}
callboard.init = function(){
	
	//core.getJSconfig();
	callboard.connect();
	//callboard.sendMessage("hello ++++++++++++++++");
}
callboard.sendMessage = function( command ){
	   
    var pbxId = $("#unit-pbx-id").val();
    
	stompClient.send( core.jsconfig.serverContextPath  + "/receiver", {}, JSON.stringify({'id': pbxId, 'command' : command}));	
}

$(document).ready( function()
{
			
	callboard.init();
});

