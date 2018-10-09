
var units = units || {};

units.init = function(){
	
	console.log("init");
	$("#phones-table-container").empty();
	$("#phones-table-container").append('<table class="table" id="table-level-0"><tbody>');
	

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
											   '<button data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle" aria-expanded="false"><i class="fa fa-edit"></i><span class="caret"></span></button>' + 
											   '<ul class="dropdown-menu">' + 
											   '<li><a href="#" style="color: #000000;"><i class="fa fa-hospital-o"></i><span style="padding-left: 5px;">' + $button.addCenter + '</span></a></li> ' +
											   '<li><a href="#" style="color: #000000;"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li> ' +
					                           '<li class="divider" style="color: #000000;"></li>' + 
											   '<li><a href="#" style="color: #000000;"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' + 
											   '</ul></div>' +
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
											   '<button data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle" aria-expanded="false"><i class="fa fa-edit"></i><span class="caret"></span></button>' + 
											   '<ul class="dropdown-menu">' + 
											   '<li><a href="#"><i class="fa fa-phone"></i><span style="padding-left: 5px;">' + $label.addPhone + '</span></a></li> ' +
											   '<li><a href="#"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li> ' +
					                           '<li class="divider"></li>' + 
											   '<li><a href="#"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' + 
											   '</ul></div>' +
				    							'</td></tr>');
				 
				    var equipments = e.containers[i].containers[j].pnameQ.equipments;
				    
				    for(var k=0; k <  equipments.length; k++){
					   
					   $("#table-level-0").append('<tr  style="color:#1f3d71;"><td>&nbsp;' + 
							 
							   '</td><td>' + 
							   '&nbsp;' + 
							   '</td><td>' + equipments[k].office  + '&nbsp;<strong> ' + equipments[k].person +  
							   '</strong></td><td>' +   equipments[k].phone + 
							   '</td><td>' 			+   equipments[k].ipaddress + 
							   '</td><td>' 			+   equipments[k].netmask +
							   '</td><td>'			+   equipments[k].gateway + 
							   '</td><td>' + 
							   '<div class="btn-group">' +
							   '<button data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle" aria-expanded="false"><i class="fa fa-edit"></i><span class="caret"></span></button>' + 
							   '<ul class="dropdown-menu">' + 
							   '<li><a href="#"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '<span></a></li> ' +
	                           '<li class="divider"></li>' + 
							   '<li><a href="#"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' + 
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
units.openForm = function(){
	$("#unit-edit-container").removeClass( "hidden" );
	$("#btn-unit-add-cancel").removeClass("btn-primary");
	$("#btn-unit-add-cancel").addClass("btn-warning");
	$("#btn-unit-add-cancel").text( $button.cancel	 );
}
units.closeForm = function(){
	$("#unit-edit-container").addClass( "hidden" )
	$("#btn-unit-add-cancel").addClass("btn-primary");
	$("#btn-unit-add-cancel").removeClass("btn-warning");
	$("#btn-unit-add-cancel").text( $button.addUnit	);
}

units.setupUserGUI = function(){
	
	$("#btn-unit-add-cancel").click( function(){
		console.log("setupGUI units");
		units.openModal();
	});
	
	$("#btn-unit-save").click( function(){
		//saveUnit();
		console.log("close Unit...");
		units.closeModal();
	});
	$("#btn-unit-close").click( function(){
		//saveUnit();
		console.log("close Unit...");
		units.closeModal();
	});
	
}
units.openModal = function(){
	$("body").addClass("modal-open");
	$("body").attr("style","padding-right: 14px;");
	$("body").append('<div id="modal-backdrop" class="modal-backdrop in"></div>');
	
	$( "#unit-modal-form" ).addClass("in");
	$( "#unit-modal-form" ).attr("style","display: block;");
}
units.closeModal = function(){
	$( "body" ).removeClass("modal-open");
	$( "body" ).attr("style","");
	$( "#modal-backdrop" ).remove();
	
	$( "#unit-modal-form" ).removeClass("in");
	$( "#unit-modal-form" ).attr("style","display: none;");
	
}
$(document).ready( function()
{
	units.setupUserGUI();
	units.init();
	
	
	
});
