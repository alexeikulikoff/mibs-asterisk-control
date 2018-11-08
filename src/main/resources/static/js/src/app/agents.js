var agents = agents || {},
		  agentTable = null
;

agents.action = {
		'AGENT_SAVED' : function(){
			 core.showStatus($success.saveAgent,"success");
			 agents.setupAgentsTable();
			 closeForm();
		 },
		'AGENT_NOT_SAVED' : function(){
			  core.showStatus($error.saveAgent,"error");
		},
		'AGENT_DROPED'  : function(){
			 core.showStatus($success.dropAgent,"success");
			 agents.setupAgentsTable();
		},
		'AGENT_NOT_DROPED'  : function(){
			 core.showStatus($error.dropAgent,"error");
			
		}
	}
agents.setEnable = function(){
	
	$( "#agents-btn-toolbar" ).find('input').each(function( index ) {
		var id =  $(this).attr("id").split("-")[1];
		if (id.startsWith("date")){
			$(this).removeAttr("disabled");
			$(this).val(core.getCurrentDate() );	
		}
		if (id.startsWith("phone")){
			$(this).removeAttr("disabled");
		}
		
	}).end().find('button').each(function( index ) {
		$(this).removeAttr("disabled");
	}).end().find('select').each(function( index ) {
		$(this).removeAttr("disabled");
	});
	
	$("#agent-table-container").removeClass("hidden");
	
}
agents.setPBX = function(id){
	
	$("#agentpbx-id").val(id);
	
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success: function(config){
				
			agents.setEnable();
			$("#agent-pbx-name").val(config.astname);
			
			agents.setupAgentsTable();
			
		},
 	    error : function(e) {
		  core.showStatus($error.network, "error");
		}
	});
}
agents.init = function() {
	$.ajax({
		type : "GET",
		url : "findAllconfig",
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {
		
			for (var i = 0; i < config.length; i++) {
				$("#agents-ul").append(
						$('<li><a href="#" onclick="agents.setPBX(\''
								+ config[i].id + '\')">' + config[i].astname
								+ '</a></li>'));
			}

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});

}
agents.setupAgentsTable = function(){
	
	
	var id = $("#agentpbx-id").val();
	
	$.ajax({
		type : "GET",
		url : "findAllAgents?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(agents) {
		
			if (agentTable!=null){
				agentTable.destroy();
			}
			
			agentTable = $("#agents-table")
				.on('draw.dt', function(){
					core.hideWaitDialog();
				})
				.DataTable({
					data : agents,
					columns : 
						[
					{ title	: $label.id, data : "id" , className : "col-md-2", render : function( data, type, row){
						return  data  ;
					} },
					{ title	: $label.name, data : "name" , className : "col-md-4", render : function( data, type, row){
						return  data  ;
					} },
					{ title	: $label.extension, data : "extension" , className : "col-md-1", render : function( data, type, row){
						return  data  ;
					} },
					{ title	: "+", data : "id" , className : "col-md-2", render : function( data, type, row){
						  
					 return '<div class="btn-group">' + 
	                 		'<button id="actionBtn-"'+ row.id + '"  data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle " aria-expanded="false"><i class="fa fa-edit"></i>' 
	                 			+ '<span class="caret"></span></button>' 
		                 		+ '<ul class="dropdown-menu pull-right">' + 
		                 			'<li><a href="#" onclick="agents.agentsEdit(\'' + data +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
		                 			'<li class="divider"></li>'+
		                 			'<li><a href="#" onclick="agents.agentsDrop(\'' + data + '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
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
			
			
		     console.log(agents);

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});

}
function openForm(){
	$("#agent-edit-container").removeClass( "hidden" );
	$("#btn-agent-add-cancel").removeClass("btn-primary");
	$("#btn-agent-add-cancel").addClass("btn-warning");
	$("#btn-agent-add-cancel").text( $button.cancel	 );
}
function closeForm(){
	$("#agent-edit-container").addClass( "hidden" )
	$("#btn-agent-add-cancel").addClass("btn-primary");
	$("#btn-agent-add-cancel").removeClass("btn-warning");
	$("#btn-agent-add-cancel").text( $button.addAgent	);
}
function clearForm(){
	$('#form-add-agent').find('input').each(function(){
		  $(this).val("") ;
	});
}
function saveAgent(){
	var agent = {
			id : "",
			name : "",
			extension : "",
			pbxid	: $("#agentpbx-id").val()
			
	};
	var empty = core.testNotEmptyField("form-add-agent");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-agent", agent);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	
	$.ajax({
			  type: "POST",
			  url:  "saveAgent",
			  data: JSON.stringify( agent ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  agents.action[e.message]();
			  	},error : function( e) {
			  	//	console.log(e);
				  core.showStatus($error.network,"error");
			  	}
		});	
	
}
agents.agentsEdit = function( id ){
	
	var pbxid = $("#agentpbx-id").val();
	
	$.ajax({
		type: "GET",
		url: "findAgent?id=" + id + "&pbxid=" + pbxid,
		dataType: "json",
		success: function( agent ){
			console.log(agent);
			if (agent.name != null){
				core.bindForm2Object("form-add-agent",agent);
				openForm();
			}else{
				core.showStatus($error.findAgent,"error");
			}
		
		},
		error: function(e){
			 core.showStatus($error.network,"error");
			
		}	
	});	
	
}
agents.setupUI = function(){
	
	
	$("#btn-agent-add-cancel").click( function(){
		console.log("setupGUI agents");
		clearForm();
		if ($("#agent-edit-container").hasClass("hidden")){
			openForm();
			return;
		}
		if (!$("#agent-edit-container").hasClass("hidden") ){
			closeForm();
			return;
		}
	});	
	
	$("#btn-agent-save").click( function(){
		saveAgent();
	});
}
$(document).ready(function() {

	agents.setupUI();
	agents.init();
	
});