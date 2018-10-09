var config = config || {} ;
var configTable = null;

config.action = {
		'CONFIG_SAVED' : function(){
			 core.showStatus($success.saveConfig,"success");
			 setupconfigTable();
			 closeConfigForm();
		 },
		'CONFIG_NOT_SAVED' : function(){
			  core.showStatus($error.saveConfig,"error");
		},
		'CONFIG_DROPED'  : function(){
			 core.showStatus($success.dropConfig,"success");
			 setupconfigTable();
		},
		'CONFIG_NOT_DROPED'  : function(){
			 core.showStatus($error.dropConfig,"error");
			
		}
	}
function clearConfigForm(){
	$('#form-add-config').find('input').each(function(){
		  $(this).val("") ;
	});
}
config.Drop = function(id){
	var configuration = {
			id : id, 
			astname : "",
			dbhost : "",
			dbname : "",
			dbuser : "",
			dbpassword : ""
	};
	

	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "dropConfig",
			  data: JSON.stringify(configuration),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  config.action[e.message]();
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
		
}
config.Edit = function( id ){
	$.ajax({
		type: "GET",
		url: "findConfig?id=" + id,
		dataType: "json",
		success: function( config ){
			
			core.bindForm2Object("form-add-config",config);
			openConfigForm();
		
		},
		error: function(e){
			 core.showStatus($error.network,"error");
			
		}	
	});	
}

function setupconfigTable(){
	console.log("config.init");
	if (configTable !=  null){
		configTable.destroy();
	}
	core.showWaitDialog();
	$.ajax({
		type: "GET",
		url: "findAllconfig",
		dataType: "json",
		success: function( data ){
			$( "#configTableContainer" ).empty();
			$( "#configTableContainer" ).append("<table id=\"configTable\" class=\"table\"></table>");
			$( "#configNumber" ).text($label.registered + "-" + data.length );
			configTable = $("#configTable")
				.on('draw.dt', function(){
					core.hideWaitDialog();
			})
			.DataTable({
				data : data,
				columns : 
					[
						{ title	: $label.id, data : "id" ,  render : function( data, type, row){
							return  data  ;
						} },
						{ title	: $label.astname, data : "astname" , render : function( data, type, row){
							return  data  ;
						} },
						{ title	: $label.dbhost, data : "dbhost"  , render : function( data, type, row){
							return  data  ;
						} },
						{ title	: $label.dbname, data : "dbname"  , render : function( data, type, row){
							return  data  ;
						} },
						{ title	: $label.dbuser, data : "dbuser"  , render : function( data, type, row){
							return  data  ;
						} },
						{ title	: $label.dbpassword, data : "dbpassword"  , render : function( data, type, row){
							return  data  ;
						} },
					
						{ title	: "+", data : "id" , className : "col-md-2", render : function( data, type, row){
							  
						 return '<div class="btn-group">' + 
		                 		'<button id="actionBtn-"'+ row.id + '"  data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle " aria-expanded="false"><i class="fa fa-edit"></i>' 
		                 			+ '<span class="caret"></span></button>' 
			                 		+ '<ul class="dropdown-menu pull-right">' + 
			                 			'<li><a href="#" onclick="config.Edit(\'' + data +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
			                 			'<li class="divider"></li>'+
			                 			'<li><a href="#" onclick="config.Drop(\'' + data + '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
			                 		 '</ul>' + 
			                 		'</div>' ;
						} }
					],
					order: [[0, 'asc']],
					paging    : false,
					info 	 : false,
					searching : false, 
					iDisplayLength : 100,
					scrollY : 300
			});	
			
		},
		error: function(e){
			console.log(e);
			
		}	
	});
	
}
function saveConfig(){
	var configuration = {
			id : "", 
			astname : "",
			dbhost : "",
			dbname : "",
			dbuser : "",
			dbpassword : ""
	};
	
	var empty = core.testNotEmptyField("form-add-config");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-config", configuration);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "saveConfig",
			  data: JSON.stringify(configuration),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  config.action[e.message]();
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
	
}
function openConfigForm(){
	$("#config-edit-container").removeClass( "hidden" );
	$("#btn-config-add-cancel").removeClass("btn-primary");
	$("#btn-config-add-cancel").addClass("btn-warning");
	$("#btn-config-add-cancel").text( $button.cancel	 );
}
function closeConfigForm(){
	$("#config-edit-container").addClass( "hidden" )
	$("#btn-config-add-cancel").addClass("btn-primary");
	$("#btn-config-add-cancel").removeClass("btn-warning");
	$("#btn-config-add-cancel").text( $button.addConfig	);
}
function setupConfigGUI(){
	
	$("#btn-config-add-cancel").click( function(){
		
		clearConfigForm();
		if ($("#config-edit-container").hasClass("hidden")){
			openConfigForm();
			return;
		}
		if (!$("#config-edit-container").hasClass("hidden") ){
			closeConfigForm();
			return;
		}
	});
	
	$("#btn-config-save").click( function(){
		saveConfig();
	});
}
config.init = function(){
	setupconfigTable();
	setupConfigGUI();
}