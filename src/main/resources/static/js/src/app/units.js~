
var units = units || {};

units.init = function(){
	
	console.log("init");
	$("#phones-table-container").empty();
	$("#phones-table-container").append('<table class="table" id="table-level-0">');
	$("#table-level-0").append('<thead><tr><td>Name</td></tr></thead><tbody>');

	core.showWaitDialog();
	
	$.ajax({
		type: "GET",
		url: "showAllUnits",
		dataType: "json",
		success: function(e){
			console.log(e);
			
			for(var i=0; i < e.containers.length; i++) {
				$("#table-level-0").append('<tr style="background-color:#e6e6e6;"><td  colspan="2">' + e.containers[i].pnameQ.name  + '</td></tr>');
				
				for(var j=0; j < e.containers[i].containers.length; j++){
				   
						$("#table-level-0").append('<tr><td>&nbsp;</td><td>' + e.containers[i].containers[j].pnameQ.name  + '</td></tr>');			
				}
			}
			
			$("#phones-table-container").append('</tbody></table>');
			
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
