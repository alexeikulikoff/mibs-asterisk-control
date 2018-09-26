var app ={};

app.init = function(){
	console.log("app init");

	cdr.init();
	users.init();
} 
$(document).ready( function()
		{
			app.init();
		
});