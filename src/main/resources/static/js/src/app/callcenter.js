var callcenter = callcenter || {},
		  agentTable = null,
		  peerTable = null
;

callcenter.action = {
		'AGENT_SAVED' : function(){
			 core.showStatus($success.saveAgent,"success");
			 callcenter.setupAgentsTable();
			 closeForm();
		 },
		'AGENT_NOT_SAVED' : function(){
			  core.showStatus($error.saveAgent,"error");
		},
		'AGENT_DROPED'  : function(){
			 core.showStatus($success.dropAgent,"success");
			 callcenter.setupAgentsTable();
		},
		'AGENT_NOT_DROPED'  : function(){
			 core.showStatus($error.dropAgent,"error");
			
		},
		'PEER_SAVED' : function(){
			 core.showStatus($success.saveEquipment,"success");
			 callcenter.setupPeersTable();
			 closeForm("peer-edit-container", "btn-peer-add-cancel",$button.addPeer);
		 },
		'PEER_NOT_SAVED' : function(){
			  core.showStatus($error.saveEquipment,"error");
		},
		'PEER_DROPED'  : function(){
			 core.showStatus($success.dropEquipment,"success");
			 callcenter.setupPeersTable();
		},
		'PEER_NOT_DROPED'  : function(){
			 core.showStatus($error.dropEquipment,"error");
			
		}
	}
callcenter.setEnable = function(){
	
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
	
	$("#peer-table-container").removeClass("hidden");
	
	
	
}
callcenter.dropAgent = function(id){
	var agent = {
			pbxid : $("#agentpbx-id").val(),
			id : id
	};
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
		  type: "POST",
		  url:  "dropAgent",
		  data: JSON.stringify( agent ),
		  contentType : 'application/json',
		  dataType: "json",
		  headers : headers ,    	
		  success: function(e){
			 
			  calcenter.action[e.message]();
			  
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});	
	
}
callcenter.setPBX = function(id){
	
	$("#agentpbx-id").val(id);
	
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success: function(config){
				
			callcenter.setEnable();
			$("#agent-pbx-name").val(config.astname);
			
			$("#btn-agent-add-cancel").removeAttr("disabled");
			$("#btn-peer-add-cancel").removeAttr("disabled");
			
			
			callcenter.setupAgentsTable();
			callcenter.setupPeersTable();
			
		},
 	    error : function(e) {
		  core.showStatus($error.network, "error");
		}
	});
}
callcenter.init = function() {
	$.ajax({
		type : "GET",
		url : "findAllconfig",
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {
		
			for (var i = 0; i < config.length; i++) {
				$("#agents-ul").append(
						$('<li><a href="#" onclick="callcenter.setPBX(\''
								+ config[i].id + '\')">' + config[i].astname
								+ '</a></li>'));
			}

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});

}
callcenter.setupAgentsTable = function(){
	
	
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
		                 			'<li><a href="#" onclick="callcenter.editAgent(\'' + data +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
		                 			'<li class="divider"></li>'+
		                 			'<li><a href="#" onclick="callcenter.dropAgent(\'' + data + '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
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

function openForm(container, button){
	$("#" + container).removeClass( "hidden" );
	$("#" + button).removeClass("btn-primary");
	$("#" + button).addClass("btn-warning");
	$("#" + button).text( $button.cancel );
}
function closeForm(container, button, caption){
	$("#" + container).addClass( "hidden" )
	$("#" + button).addClass("btn-primary");
	$("#" + button).removeClass("btn-warning");
	$("#" + button).text( caption	);
}
function clearForm( form ){
	$("#" + form).find('input').each(function(){
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
				  callcenter.action[e.message]();
			  	},error : function( e) {
			  	//	console.log(e);
				  core.showStatus($error.network,"error");
			  	}
		});	
	
}
callcenter.editAgent = function( id ){
	
	var pbxid = $("#agentpbx-id").val();
	
	$.ajax({
		type: "GET",
		url: "findAgent?id=" + id + "&pbxid=" + pbxid,
		dataType: "json",
		success: function( agent ){
			console.log(agent);
			if (agent.name != null){
				core.bindForm2Object("form-add-agent",agent);
				
				openForm("agent-edit-container", "btn-agent-add-cancel");
				
			}else{
				core.showStatus($error.findAgent,"error");
			}
		
		},
		error: function(e){
			 core.showStatus($error.network,"error");
			
		}	
	});	
	
}
function savePeer(){
	var peer = {
			id : "",
			name : "",
			pbx	: $("#agentpbx-id").val()
			
	};
	var empty = core.testNotEmptyField("form-add-peer");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-peer", peer);
	
	console.log( peer );
	
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "savePeer",
			  data: JSON.stringify( peer ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  callcenter.action[e.message]();
			  	},error : function( e) {
			  		console.log(e);
//				  core.showStatus($error.network,"error");
			  	}
		});	
	
}
callcenter.editPeer = function( id ){
	
	var pbxid = $("#agentpbx-id").val();
	
	$.ajax({
		type: "GET",
		url: "findPeer?id=" + id + "&pbxid=" + pbxid,
		dataType: "json",
		success: function( peer ){
			console.log(peer);
			if (peer.name != null){
				core.bindForm2Object("form-add-peer",peer);
				openForm("peer-edit-container","btn-peer-add-cancel");
			}else{
				core.showStatus($error.findAgent,"error");
			}
		
		},
		error: function(e){
			 core.showStatus($error.network,"error");
			
		}	
	});	
	
}
callcenter.setupPeersTable = function(){
	
	
	var id = $("#agentpbx-id").val();
	
	$.ajax({
		type : "GET",
		url : "findAllPeers?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(agents) {
		
			if (peerTable!=null){
				peerTable.destroy();
			}
			
			peerTable = $("#peers-table")
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
					{ title	: "+", data : "id" , className : "col-md-2", render : function( data, type, row){
					 return '<div class="btn-group">' + 
	                 		'<button id="actionBtn-"'+ row.id + '"  data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle " aria-expanded="false"><i class="fa fa-edit"></i>' 
	                 			+ '<span class="caret"></span></button>' 
		                 		+ '<ul class="dropdown-menu pull-right">' + 
		                 			'<li><a href="#" onclick="callcenter.editPeer(\'' + data +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
		                 			'<li class="divider"></li>'+
		                 			'<li><a href="#" onclick="callcenter.dropPeer(\'' + data + '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
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
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});

}
callcenter.setupUI = function(){
	$("#btn-agent-add-cancel").click( function(){
		clearForm("form-add-agent");
		if ($("#agent-edit-container").hasClass("hidden")){
			openForm("agent-edit-container", "btn-agent-add-cancel");
			return;
		}
		if (!$("#agent-edit-container").hasClass("hidden") ){
			closeForm("agent-edit-container", "btn-agent-add-cancel" ,$button.addAgent );
			return;
		}
	});	
	$("#btn-peer-add-cancel").click( function(){
		clearForm("form-add-peer");
		if ($("#peer-edit-container").hasClass("hidden")){
			openForm("peer-edit-container", "btn-peer-add-cancel");
			return;
		}
		if (!$("#peer-edit-container").hasClass("hidden") ){
			closeForm("peer-edit-container", "btn-peer-add-cancel" ,$button.addPeer );
			return;
		}
	});	
	
	$("#btn-agent-save").click( function(){
		saveAgent();
	});
	$("#btn-peer-save").click( function(){
		savePeer();
	});
}
$(document).ready(function() {

	callcenter.setupUI();
	callcenter.init();
	
});