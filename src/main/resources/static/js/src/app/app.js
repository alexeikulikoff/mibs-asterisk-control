var app ={};

app.init = function(){
	console.log("app init");
	dashboard.init();
	cdr.init();
} 
$(document).ready( function()
		{
			app.init();
		
});