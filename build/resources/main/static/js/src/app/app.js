var app ={};

app.init = function(){
	console.log("app init");
	
	console.log(core.csrf());
	cdr.init();
	users.init();
	config.init();
} 

$(document).ready( function()
{
	app.init();

    			
		
});