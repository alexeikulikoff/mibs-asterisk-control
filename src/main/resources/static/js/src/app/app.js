var app ={};

app.init = function(){
	console.log("app init");

	cdr.init();
	users.init();
} 
$(document).ready( function()
{
	app.init();
	$("#btn-add-user").click( function(){
		if ($("#user-edit-container").hasClass("hidden")){
			$("#user-edit-container").removeClass( "hidden" );
			$("#btn-add-user").text("Save User");
			return;
		}
		if (!$("#user-edit-container").hasClass("hidden") ){
			$("#user-edit-container").addClass( "hidden" )
			$("#btn-add-user").text("New User");
			return;
		}
		
	});
    			
		
});