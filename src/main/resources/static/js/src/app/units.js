
var units = units || {};

units.init = function(){
	
	console.log("init");
	$.ajax({
		type: "GET",
		url: "showAllUnits",
		dataType: "json",
		success: function(e){
			console.log(e);
		},error : function(e){
			console.log(e);
		}
	});	
	
}

$(document).ready( function()
{
	units.init();
	
	
	
});
