
var units = units || {};

units.init = function(){
	
	console.log("init");
	$("#phones-table-container").empty();
	$("#phones-table-container").append('<table class="table" id="table-level-0">');
	$("#phones-table-container").append('<thead><tr><td>Name</td></tr></thead>');

	core.showWaitDialog();
	
	$.ajax({
		type: "GET",
		url: "showAllUnits",
		dataType: "json",
		success: function(e){
			console.log(e);
			
			for(var i=0; i < e.containers.length; i++) {
				$("#table-level-0").append('<tr><td>' + e.containers[i].pnameQ.name  + '</td></tr>');
			}
			
			$("#phones-table-container").append('</table>');
			
			core.hideWaitDialog();
			
		},error : function(e){
			console.log(e);
		}
	});	
	
}

$(document).ready( function()
{
	units.init();
	
	
	
});
