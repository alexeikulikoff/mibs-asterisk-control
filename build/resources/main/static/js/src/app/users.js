var users = users || {} ;
var usersTable = null;

function clearForm(){
	$('#form-add-user').find('input').each(function(){
		  $(this).val("") ;
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
							return  '<button class="btn btn-primary btn-circle" onclick="users.userEdit(\'' + data + '\')" type="button"><i class="fa fa-pencil"></i></button>'  ;
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
}
users.init = function(){
	setupUsersTable();
	setupGUI();
}