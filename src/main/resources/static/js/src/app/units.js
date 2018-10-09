
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
				$("#table-level-0").append('<tr style="background-color:#3289c8; color:#ffffff;"><td colspan="7">' + e.containers[i].pnameQ.name  + 
											'</td><td>'  + 
											  '<div class="btn-group">' +
				   							    '<button class="btn btn-primary btn-xs" >' + $button.addCenter +'</button>' + 
				   							   '</div>' +
											
											
											'</td></tr>');
				for(var j=0; j < e.containers[i].containers.length; j++){
					var pnq = e.containers[i].containers[j].pnameQ;  
				    $("#table-level-0").append('<tr style="background-color:#efefef; color:#1f3d71;" ><td>' + 
				    		'<a href="#"><i class="fa fa-hospital-o"></i></a>' + 
				    		'</td><td colspan="2">' + pnq.name  + 
				    							'</td><td>' + $label.phone + 
				    							'</td><td>' + $label.ipaddress +
				    							'</td><td>' + $label.mask +
				    							'</td><td>' + $label.gateway +
				    							'</td><td>' +
				    						    '<div class="btn-group">' +
				   							    '<button class="btn btn-primary btn-xs" >' + $label.addPhone +'</button>' + 
				   							    '</div>' +
				    							'</td></tr>');
				 
				    var equipments = e.containers[i].containers[j].pnameQ.equipments;
				    
				    for(var k=0; k <  equipments.length; k++){
					   
					   $("#table-level-0").append('<tr  style="color:#1f3d71;"><td>&nbsp;' + 
							 
							   '</td><td>' + 
							   '<a href="#"><i class="fa fa-phone text-navy"></i></a>' + 
							   '</td><td>' + equipments[k].office  + '&nbsp;<strong> ' + equipments[k].person +  
							   '</strong></td><td>' +   equipments[k].phone + 
							   '</td><td>' 			+   equipments[k].ipaddress + 
							   '</td><td>' 			+   equipments[k].netmask +
							   '</td><td>'			+   equipments[k].gateway + 
							   '</td><td>' + 
							   '<div class="btn-group">' +
							   '<button data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle" aria-expanded="false"><i class="fa fa-edit"></i><span class="caret"></span></button>' + 
							   '<ul class="dropdown-menu">' + 
							   '<li><a href="#">' + $button.edit + '</a></li> ' +
	                           '<li class="divider"></li>' + 
							   '<li><a href="#">' + $button.drop + '</a></li>' + 
							   '</ul></div>' +
							   '</td></tr>');
					   
					   
				   }
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
