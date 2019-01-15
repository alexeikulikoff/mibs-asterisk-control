
var units = units || {};

var newEquipment = {};
stompClient = null;

units.connect = function(){
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
    var socket = new SockJS( core.jsconfig.serverContextPath  + '/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect( headers, function (frame) {
    	stompClient.subscribe('/topic/sender', function( message ) {
    		units.translateMessage(JSON.parse(message.body).content);
        	
        });
    });
}
units.translateMessage = function(message){
	$("#config-file-text").val(message);
}
units.disconnect = function(){
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}
units.sendMessage = function( command ){
   
    var pbxId = $("#unit-pbx-id").val();
    
	stompClient.send( core.jsconfig.serverContextPath  + "/receiver", {}, JSON.stringify({'id': pbxId, 'command' : command}));	
}


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
	
	
	$("#phones-table-container").empty();
	$("#phones-table-container").append('<table class="table" id="table-level-0"><tbody>');
	
    var pbx = $("#unit-pbx-id").val();
    
	core.showWaitDialog();
	
	$.ajax({
		type: "GET",
		url: "showAllUnits?pbx=" + pbx,
		dataType: "json",
		success: function(e){
			var user_role = $("#user_role").val();
			
			for(var i=0; i < e.containers.length; i++) {
				
				var str_level_0 = '';

				if(user_role == "ADMIN"){
					var str_level_0 = '<div class="btn-group">' +
					   '<button data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle" aria-expanded="false"><i class="fa fa-edit"></i><span class="caret"></span></button>' + 
					   '<ul class="dropdown-menu">' + 
					   '<li><a href="#" style="color: #000000;" onclick="units.addCenter(\'' + e.containers[i].pnameQ.p +  '\')"><i class="fa fa-hospital-o"></i><span style="padding-left: 5px;">' + $button.addCenter + '</span></a></li> ' +
					   '<li><a href="#" style="color: #000000;" onclick="units.editUnit(\'' + e.containers[i].pnameQ.p +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li> ' +
	                   '<li class="divider" style="color: #000000;"></li>' + 
					   '<li><a href="#" style="color: #000000;" onclick="units.dropUnit(\'' + e.containers[i].pnameQ.p +  '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' + 
					   '</ul></div>';	
				}
				
				$("#table-level-0").append('<tr style="background-color:#4b6074; color:#ffffff;"><td colspan="10">' + e.containers[i].pnameQ.name  + 
											'</td><td>' + str_level_0 + '</td></tr>');
				
				for(var j=0; j < e.containers[i].containers.length; j++){
					
					var str_level_1 = '';
					if(user_role == "ADMIN"){
						str_level_1 = '</td><td>' + $label.ipaddress +
							'</td><td>' + $label.mask +
							'</td><td>' + $label.gateway +
							'</td><td>' +
							'<div class="btn-group">' +
						   '<button data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle" aria-expanded="false"><i class="fa fa-edit"></i><span class="caret"></span></button>' + 
						   '<ul class="dropdown-menu">' + 
						   '<li><a href="#" onclick="units.addEquipment(\'' + e.containers[i].containers[j].pnameQ.p +  '\')"><i class="fa fa-phone"></i><span style="padding-left: 5px;">' + $label.addPhone + '</span></a></li> ' +
						   '<li><a href="#" onclick="units.editCenter(\'' +e.containers[i].containers[j].pnameQ.p +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li> ' +
                           '<li class="divider"></li>' + 
						   '<li><a href="#" onclick="units.dropCenter(\'' + e.containers[i].containers[j].pnameQ.p +  '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' + 
						   '</ul></div>';
						
					}
					
					var pnq = e.containers[i].containers[j].pnameQ;  
				    $("#table-level-0").append('<tr style="background-color:#efefef; color:#293846;" ><td>' + 
				    		'<a href="#"><i class="fa fa-hospital-o"></i></a>' + 
				    		'</td><td colspan="2">' + pnq.name  + 
				    							'</td><td>' + $label.phone + 
				    							'</td><td>' + $label.recordIn + 
				    							'</td><td>' + $label.recordOut +
				    							'</td><td>' + $label.external + 
				    						
				    							str_level_1 +
				    							'</td></tr>');
				 
				    var equipments = e.containers[i].containers[j].pnameQ.equipments;
				    
				    for(var k=0; k <  equipments.length; k++){
					   
				      var recordIn = (equipments[k].recordIn == 'No') ? '<div class="text-danger"><i class="fa fa-ban"></i></div>' : '<div class="text-navy"><i class="fa fa-check-square-o"></i></div>';
				      var recordOut = (equipments[k].recordOut == 'No') ? '<div class="text-danger"><i class="fa fa-ban"></i></div>' : '<div class="text-navy"><i class="fa fa-check-square-o"></i></div>';
				      var external = (equipments[k].external == 'No') ? '<div class="text-danger"><i class="fa fa-ban"></i></div>' :  equipments[k].external;
				      
				      var str_level_2 = '';
				      
				      
				      
				  	  if(user_role == "ADMIN"){
				  		  str_level_2 = 
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
						   '</ul></div>';
				  		  
				  	  }
				      
					   $("#table-level-0").append('<tr  style="color:#1f3d71;"><td>&nbsp;' + 
							 
							   '</td><td>' + 
							   '&nbsp;' + 
							   '</td><td>' + equipments[k].office  + '&nbsp;<strong> ' + equipments[k].person +  
							   '</strong></td><td>' +   equipments[k].phone + 
							   '</td><td>' 			+   recordIn +
							   '</td><td>' 			+   recordOut +
							   '</td><td>' 			+   external +
							
							   str_level_2 +
							   '</td></tr>');
					   
					   
				   }
				}
			}
			
			$("#phones-table-container").append('</tbody></table>');

			$("#unit-all-container").removeClass("hidden");
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
units.createSipConf = function(){
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "createSipConf",
			  contentType : 'application/json',
			  dataType: "text",
			  headers : headers ,    	
			  success: function(e){
				  units.openModal("config-file-modal");
				  $("#config-file-text").val( e );
				  units.connect();
				  
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
}
units.sendFileToAsterisk = function(){
	
	$("#config-file-text").val("");
	
	core.showWaitDialog();
	
	var id = $("#unit-pbx-id").val();
	var pbx = {
			id : id 
	};
	
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "sendFileToAsterisk",
			  contentType : 'application/json',
			  data: JSON.stringify( pbx ),
			  dataType: "text",
			  headers : headers ,    	
			  success: function(e){
				
				  $("#config-file-text").val(e);
				  core.hideWaitDialog();
				  
				 // units.action[e.message]();
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
}
units.saveCenter = function(){

	var pbx =  $("#unit-pbx-id").val();
	
	var data = {
			p : "",
			name : "",
			q : "",
			pbx : pbx
	};
	var empty = core.testNotEmptyField("form-add-center");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-center", data);
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
units.editCenter = function(p){
	$.ajax({
		type: "GET",
		url: "findUnit?id=" + p,
		dataType: "json",
		success: function( unit ){
			if (unit.name != null){
				core.bindForm2Object("form-add-center",unit);
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
units.editUnit = function( p ){
	$.ajax({
		type: "GET",
		url: "findUnit?id=" + p,
		dataType: "json",
		success: function( unit ){
			if (unit.name != null){
				core.bindForm2Object("form-add-unit",unit);
				units.openModal("unit-modal-form");
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
	var pbx =  $("#unit-pbx-id").val();
	var data = {
			p : "",
			name : "",
			q : "",
			pbx :  pbx
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

units.setPBX = function(id){
	
	$("#unit-pbx-id").val(id);
	$.ajax({
		  type: "GET",
		  url:  "findConfig?id=" + id,
		  contentType : 'application/json',
		  dataType: "json",
		  success: function(config){
			  core.enableElemtnt("btn-unit-add-cancel");
			  core.enableElemtnt("btn-unit-sip-config");
			  $("#unit-pbx-name").val(config.astname);
			  units.setupUnitsGUI();
			  units.setupUnitTable();
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});		
}
units.init = function(){
	
	core.getJSconfig();
	
	core.disableElemtnt("btn-unit-add-cancel");
	core.disableElemtnt("btn-unit-sip-config");
	$.ajax({
		  type: "GET",
		  url:  "findAllconfig",
		  contentType : 'application/json',
		  dataType: "json",
		  success: function(config){
			  for(var i=0 ; i < config.length; i++ ){
			     $("#config-ul").append($('<li><a href="#" onclick="units.setPBX(\'' + config[i].id +'\')">' + config[i].astname + '</a></li>'));
			  }
			  
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});		

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
	$("#btn-unit-sip-config").click( function(){
		units.createSipConf();
	});
	$("#btn-unit-send-config").click( function(){
		units.sendFileToAsterisk();
	});
	$("#btn-file-warn-close").click( function(){
		 units.disconnect();
		 units.closeModal("config-file-modal");
	});
	
	$("#btn-unit-sip-reload").click( function(){
		units.sendMessage("sip reload");
	});
	
	$("#btn-unit-send-test-ws").click( function(){
		units.sendMessage("sip show peers");
	});
	
	$('[data-toggle="tooltip"]').tooltip();
	
}

$(document).ready( function()
{
	
	units.init();
});

