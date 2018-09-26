var users = users || {} ;
var usersTable = null;

users.init = function(){
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
			console.log( data );
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