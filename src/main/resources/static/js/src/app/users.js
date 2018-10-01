var users = users || {} ;
var usersTable = null;

users.action = {
		'USER_SAVED' : function(){
			 core.showStatus($success.saveUser,"success");
			 setupUsersTable();
			 closeForm();
		 },
		'USER_NOT_SAVED' : function(){
			  core.showStatus($error.saveUser,"error");
		},
		'USER_DROPED'  : function(){
			 core.showStatus($success.dropUser,"success");
			 setupUsersTable();
		},
		'USER_NOT_DROPED'  : function(){
			 core.showStatus($error.dropUser,"error");
			
		}
	}
function clearForm(){
	$('#form-add-user').find('input').each(function(){
		  $(this).val("") ;
	});
}
users.userDrop = function(id){
	var user = {
			name : "",
			password : "",
			role : "",
			id : id
	};

	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "dropUser",
			  data: JSON.stringify(user),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  users.action[e.message]();
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
		
}
users.userEdit = function( id ){
	$.ajax({
		type: "GET",
		url: "findUser?id=" + id,
		dataType: "json",
		success: function( user ){
			if (user.name != null){
				core.bindForm2Object("form-add-user",user);
				openForm();
			}else{
				core.showStatus($error.findUser,"error");
			}
		
		},
		error: function(e){
			 core.showStatus($error.network,"error");
			
		}	
	});	
}

function setupUsersTable(){
	console.log("users.init");
	if (usersTable !=  null){
		usersTable.destroy();
	}
	core.showWaitDialog();
	$.ajax({
		type: "GET",
		url: "findAllUsers",
		dataType: "json",
		success: function( data ){
			$( "#userTableContainer" ).empty();
			$( "#userTableContainer" ).append("<table id=\"usersTable\" class=\"table\"></table>");
			$( "#userNumber" ).text($label.registered + "-" + data.length );
			usersTable = $("#usersTable")
				.on('draw.dt', function(){
					core.hideWaitDialog();
			})
			.DataTable({
				data : data,
				columns : 
					[
						{ title	: $label.id, data : "id" , className : "col-md-2", render : function( data, type, row){
							return  data  ;
						} },
						{ title	: $label.name, data : "name" , className : "col-md-4", render : function( data, type, row){
							return  data  ;
						} },
						{ title	: $label.role, data : "role" , className : "col-md-4", render : function( data, type, row){
							return  data  ;
						} },
						{ title	: "+", data : "id" , className : "col-md-2", render : function( data, type, row){
							  
						 return '<div class="btn-group">' + 
		                 		'<button id="actionBtn-"'+ row.id + '"  data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle " aria-expanded="false"><i class="fa fa-edit"></i>' 
		                 			+ '<span class="caret"></span></button>' 
			                 		+ '<ul class="dropdown-menu pull-right">' + 
			                 			'<li><a href="#" onclick="users.userEdit(\'' + data +  '\')"><i class="fa fa-folder-open-o"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
			                 			'<li class="divider"></li>'+
			                 			'<li><a href="#" onclick="users.userDrop(\'' + data + '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
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
function saveUser(){
	var user = {
			name : "",
			password : "",
			role : "",
			id : ""
	};
	var empty = core.testNotEmptyField("form-add-user");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-user", user);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "saveUser",
			  data: JSON.stringify(user),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  users.action[e.message]();
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
	
}
function openForm(){
	$("#user-edit-container").removeClass( "hidden" );
	$("#btn-user-add-cancel").removeClass("btn-primary");
	$("#btn-user-add-cancel").addClass("btn-warning");
	$("#btn-user-add-cancel").text( $button.cancel	 );
}
function closeForm(){
	$("#user-edit-container").addClass( "hidden" )
	$("#btn-user-add-cancel").addClass("btn-primary");
	$("#btn-user-add-cancel").removeClass("btn-warning");
	$("#btn-user-add-cancel").text( $button.addUser	);
}
function setupGUI(){
	$("#btn-user-add-cancel").click( function(){
		clearForm();
		if ($("#user-edit-container").hasClass("hidden")){
			openForm();
			return;
		}
		if (!$("#user-edit-container").hasClass("hidden") ){
			closeForm();
			return;
		}
	});
	
	$("#btn-user-save").click( function(){
		saveUser();
	});
}
users.init = function(){
	setupUsersTable();
	setupGUI();
}