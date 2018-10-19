
var units = units || {};

var newEquipment = {};

units.action = {
		'UNIT_SAVED' : function(){
			 core.showStatus($success.saveUnit,"success");
			 units.closeAllModal();
		
			 units.setupUnitTable();
		 },
		'UNIT_NOT_SAVED' : function(){
			  core.showStatus($error.saveUnit,"error");
		},
		'UNIT_DROPED'  : function(){
			 core.showStatus($success.dropUint,"success");
			 units.setupUnitTable();
			// setupUsersTable();
		},
		'UNIT_NOT_DROPED'  : function(){
			 core.showStatus($error.dropUnit,"error");
			
		},
		'UNIT_TEST' : function(){
			console.log('UNIT_TEST');
		} 
	}
newEquipment.action = {
		'EQUIPMENT_SAVED' : function(){
			 core.showStatus($success.saveEquipment,"success");
			 units.closeAllModal();
		
			 units.setupUnitTable();
		 },
		'EQUIPMENT_NOT_SAVED' : function(){
			  core.showStatus($error.saveEquipment,"error");
			  units.closeModal("equipment-modal-form");
		},
		'EQUIPMENT_DROPED'  : function(){
			 core.showStatus($success.dropEquipment,"success");
			 units.closeModal("equipment-warn-modal-form");
			 units.setupUnitTable();
			// setupUsersTable();
		},
		'EQUIPMENT_NOT_DROPED'  : function(){
			 core.showStatus($error.dropEquipment,"error");
			 units.closeModal("equipment-warn-modal-form");
			
		},
		'EQUIPMENT_TEST' : function(){
			console.log('EQUIPMENT_TEST');
		} 
	}
units.closeAllModal = function(){
	 units.closeModal("unit-modal-form");
		
	 units.closeModal("center-modal-form");
	 
	 units.closeModal("equipment-modal-form");
	 
}
units.setupUnitTable = function(){
	
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
											   '<li><a href="#" style="color: #000000;" onclick="units.addCenter(\'' + e.containers[i].pnameQ.p +  '\')"><i class="fa fa-hospital-o"></i><span style="padding-left: 5px;">' + $button.addCenter + '</span></a></li> ' +
											   '<li><a href="#" style="color: #000000;" onclick="units.editUnit(\'' + e.containers[i].pnameQ.p +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li> ' +
					                           '<li class="divider" style="color: #000000;"></li>' + 
											   '<li><a href="#" style="color: #000000;" onclick="units.dropUnit(\'' + e.containers[i].pnameQ.p +  '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' + 
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
											   '<li><a href="#" onclick="units.addEquipment(\'' + e.containers[i].containers[j].pnameQ.p +  '\')"><i class="fa fa-phone"></i><span style="padding-left: 5px;">' + $label.addPhone + '</span></a></li> ' +
											   '<li><a href="#"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li> ' +
					                           '<li class="divider"></li>' + 
											   '<li><a href="#" onclick="units.dropCenter(\'' + e.containers[i].containers[j].pnameQ.p +  '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' + 
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
							   '<li><a href="#" onclick="units.editEquipment(\'' + equipments[k].id +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '<span></a></li> ' +
	                           '<li class="divider"></li>' + 
							   '<li><a href="#" onclick="units.warnDropEquipment(\'' + equipments[k].id +  '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' + 
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
units.addEquipment = function(p){
	
	$("#equipment-p").val(p);	
	units.openModal("equipment-modal-form");
	
}
units.warnDropEquipment = function( id ){
	$.ajax({
		  type: "GET",
		  url:  "findEquipment?id=" + id,
		  contentType : 'application/json',
		  dataType: "json",
		  success: function(equipment){
			 
			  core.bindForm2Object("equipment-warn-modal-form",equipment);
			  units.openModal("equipment-warn-modal-form");
			  
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});		
}
units.editEquipment = function( id ){
	$.ajax({
		  type: "GET",
		  url:  "findEquipment?id=" + id,
		  contentType : 'application/json',
		  dataType: "json",
		  success: function(equipment){
			 
			  core.bindForm2Object("equipment-modal-form",equipment);
			  units.openModal("equipment-modal-form");
			  
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});		
}
units.dropEquipment = function(){
	var equipment = {
			id	: $("#dropEquipment-id").val(),
			phone : "",
			office	: "",
			p		: "",
			ipaddress : "",
			netmask   : "",
			password : "",
			person : ""
	};
	console.log( equipment );
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "dropEquipment",
			  data: JSON.stringify( equipment ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				 
				  newEquipment.action[e.message]();
				  
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
	
}
units.saveEquipment = function(){
	
	var equipment = {
			id	: "",
			phone : "",
			office	: "",
			p		: "",
			ipaddress : "",
			netmask   : "",
			password : "",
			person : ""
	}
	
	var empty = core.testNotEmptyField("form-add-equipment");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-equipment", equipment);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "saveEquipment",
			  data: JSON.stringify( equipment ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				 
				  newEquipment.action[e.message]();
				  
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
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

units.setupUnitsGUI = function(){
	
	$("#btn-unit-add-cancel").click( function(){
		units.openModal("unit-modal-form");
	});
	
	$("#btn-unit-save").click( function(){
		units.saveUnit();
	});
	$("#btn-unit-close").click( function(){
		units.closeModal("unit-modal-form");
	});
	$("#btn-center-close").click( function(){
		units.closeModal("center-modal-form");
	});
	$("#btn-center-save").click( function(){
		units.saveCenter();
	});
	$("#btn-equipment-save").click( function(){
		units.saveEquipment();
	});
	$("#btn-equipment-close").click( function(){
		units.closeModal("equipment-modal-form");
	});
	$("#btn-equipment-warn-close").click( function(){
		units.closeModal("equipment-warn-modal-form");
	});
	$("#btn-equipment-drop").click( function(){
		units.dropEquipment();
	});
}
units.openModal = function(form){
	$("body").addClass("modal-open");
	$("body").attr("style","padding-right: 14px;");
	$("body").append('<div id="modal-backdrop" class="modal-backdrop in"></div>');
	
	$( "#" + form).addClass("in");
	$( "#" + form).attr("style","display: block;");
}
units.closeModal = function(form){
	$( "body" ).removeClass("modal-open");
	$( "body" ).attr("style","");
	$( "#modal-backdrop" ).remove();
	
	$( "#" + form).removeClass("in");
	$( "#" + form).attr("style","display: none;");
	
}
units.saveCenter = function(){
	var p = $("#center-p").val();
	var data = {
			p : p,
			name : $("#center-name").val(),
			q : ""
	};
	var empty = core.testNotEmptyField("center-modal-form");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("center-modal-form", data);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "saveCenter",
			  data: JSON.stringify(data),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  units.action[e.message]();
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
	
}
units.addCenter = function(p){
	
	$.ajax({
		type: "GET",
		url: "findUnit?id=" + p,
		dataType: "json",
		success: function( unit ){
			if (unit.name != null){
				//core.bindForm2Object("form-add-unit",unit);
				$("#center-modal-title").text(unit.name);
				$("#center-p").val(unit.p);
				units.openModal("center-modal-form");
			}else{
				core.showStatus($error.findUnit,"error");
			}
		
		},
		error: function(e){
			 core.showStatus($error.network,"error");
			
		}	
	});	
}
units.dropCenter = function(p){
	var data = {
			p : p,
			name : "",
			q : ""
	};
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "dropCenter",
			  data: JSON.stringify( data ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  units.action[ e.message ]();
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
		
}
units.dropUnit = function(p){
	var data = {
			p : p,
			name : "",
			q : ""
	};

	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "dropUnit",
			  data: JSON.stringify( data ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  units.action[ e.message ]();
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
		
}
units.editUnit = function( p ){
	$.ajax({
		type: "GET",
		url: "findUnit?id=" + p,
		dataType: "json",
		success: function( unit ){
			if (unit.name != null){
				core.bindForm2Object("form-add-unit",unit);
				units.openModal();
			}else{
				core.showStatus($error.findUnit,"error");
			}
		
		},
		error: function(e){
			 core.showStatus($error.network,"error");
			
		}	
	});	
}
units.saveUnit = function(){
	var empty = core.testNotEmptyField("form-add-unit");
	if ( empty ) {
		return ;
	};
	var data = {
			p : "",
			name : "",
			q : ""
	};
	
	core.bindObject2Form("form-add-unit", data);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "saveUnit",
			  data: JSON.stringify(data),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				 
				 units.action[ e.message ]();
				
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
}

units.init = function(){
	units.setupUnitsGUI();
	units.setupUnitTable();
}

$(document).ready( function()
{
	units.init();
});

