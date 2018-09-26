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
			$( "#userNumber" ).text(data.length + " " +$label.registered);
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
						{ title	: $label.name, data : "name" , className : "col-md-6", render : function( data, type, row){
							return  data  ;
						} },
						{ title	: $label.role, data : "role" , className : "col-md-4", render : function( data, type, row){
							return  data  ;
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