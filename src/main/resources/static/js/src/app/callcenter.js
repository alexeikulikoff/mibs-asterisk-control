var callcenter = callcenter || {},
		  agentTable = null,
		  peerTable = null,
		  queueTable = null,
		  centconfTable = null,
		  templateTable = null
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
			
		},
		'QUEUE_SAVED' : function(){
			 core.showStatus($success.saveQueue,"success");
			 callcenter.setupQueuesTable();
			 closeForm("queue-edit-container", "btn-queue-add-cancel",$button.addQueue);
		 },
		'QUEUE_NOT_SAVED' : function(){
			  core.showStatus($error.saveQueue,"error");
		},
		'QUEUE_DROPED'  : function(){
			 core.showStatus($success.dropQueue,"success");
			 callcenter.setupQueuesTable();
		},
		'QUEUE_NOT_DROPED'  : function(){
			 core.showStatus($error.dropQueue,"error");
			
		},
		'CONFIG_SAVED' : function(){
			 core.showStatus($success.saveCentconf,"success");
			 callcenter.setupCentconfTable();
			 closeForm("centconf-edit-container", "btn-centconf-add-cancel",$button.addCentconf);
		 },
		'CONFIG_NOT_SAVED' : function(){
			  core.showStatus($error.saveCentconf,"error");
		},
		'CONFIG_DROPED'  : function(){
			 core.showStatus($success.dropCentconf,"success");
			 callcenter.setupCentconfTable();
		},
		'CONFIG_NOT_DROPED'  : function(){
			 core.showStatus($error.dropCentconf,"error");
			
		},
		
		'TEMPLATE_SAVED' : function(){
			 core.showStatus($success.saveTemplate,"success");
			 callcenter.setupTemplateTable();
			 closeForm("template-edit-container", "btn-template-add-cancel",$button.addTemplate);
		 },
		
		'TEMPLATE_NOT_SAVED' : function(){
			  core.showStatus($error.saveTemplate,"error");
		},
		'TEMPLATE_DROPED'  : function(){
			 core.showStatus($success.dropTemplate,"success");
			 callcenter.setupTemplateTable();
		},
		'TEMPLATE_NOT_DROPED'  : function(){
			 core.showStatus($error.dropTemplate,"error");
			
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
	
	$("#queue-table-container").removeClass("hidden");
	
	$("#centconf-table-container").removeClass("hidden");
	
	
	
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
			 
			  callcenter.action[e.message]();
			  
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});	
	
}
callcenter.dropPeer = function(id){
	var peer = {
			pbx : $("#agentpbx-id").val(),
			id : id
	};
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
		  type: "POST",
		  url:  "dropPeer",
		  data: JSON.stringify( peer ),
		  contentType : 'application/json',
		  dataType: "json",
		  headers : headers ,    	
		  success: function(e){
			 
			  callcenter.action[e.message]();
			  
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});	
	
}
callcenter.dropQueue = function(id){
	var queue = {
			pbx : $("#agentpbx-id").val(),
			id : id
	};
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
		  type: "POST",
		  url:  "dropQueue",
		  data: JSON.stringify( queue ),
		  contentType : 'application/json',
		  dataType: "json",
		  headers : headers ,    	
		  success: function(e){
			 
			  callcenter.action[e.message]();
			  
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
			
			
			$("#tabs-container").removeClass("hidden");
			
			$("#btn-agent-add-cancel").removeAttr("disabled");
			$("#btn-peer-add-cancel").removeAttr("disabled");
			$("#btn-queue-add-cancel").removeAttr("disabled");
			$("#btn-centconf-add-cancel").removeAttr("disabled");
			
			
			callcenter.setupAgentsTable();
			callcenter.setupPeersTable();
			callcenter.setupQueuesTable();
			callcenter.setupCentconfTable();
			callcenter.setupTemplateTable();
			
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
		
			$("#centconf-agentid").empty();
			
			for (var i = 0; i < agents.length; i++) {
				$("#centconf-agentid").append( $('<option value="' + agents[i].id + '" >' + agents[i].name + '</option>') ); 
			}

			
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
				//	{ title	: $label.id, data : "id" , className : "col-md-2", render : function( data, type, row){
				//		return  data  ;
				//	} },
					{ title	: $label.name, data : "name" , className : "col-md-4", render : function( data, type, row){
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
				  core.showStatus($error.network,"error");
			  	}
		});	
}
function saveQueue(){
	var queue = {
			id : "",
			name : "",
			pbx	: $("#agentpbx-id").val()
			
	};
	var empty = core.testNotEmptyField("form-add-queue");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-queue", queue);
	
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "saveQueue",
			  data: JSON.stringify( queue ),
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
callcenter.dropTemplate = function(id){
	var template = {
			id : id,
			name : ""
	}
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "dropTemplate",
			  data: JSON.stringify( template ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  callcenter.action[e.message]();
			  	},error : function( e) {

				  core.showStatus($error.network,"error");
			  	}
		});	
}
callcenter.editTemplate = function( id ){
	
	$("#template-id").val(id);
	
	$.ajax({
		type: "GET",
		url: "findTemplate?id=" + id ,
		dataType: "json",
		success: function( templ ){
			console.log(templ);
			if (templ.name != null){
				core.bindForm2Object("form-add-template",templ);
				openForm("template-edit-container","btn-template-add-cancel");
			}else{
				core.showStatus($error.findTemplate,"error");
			}
		
		},
		error: function(e){
			 core.showStatus($error.network,"error");
			
		}	
	});	
	
}
saveTemplate = function(){
	var template = {
			id : "",
			name : ""
	}
	var empty = core.testNotEmptyField("form-add-template");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-template", template);
	
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "saveTemplate",
			  data: JSON.stringify( template ),
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
callcenter.editQueue = function( id ){
	var pbxid = $("#agentpbx-id").val();
	$.ajax({
		type: "GET",
		url: "findQueue?id=" + id + "&pbxid=" + pbxid,
		dataType: "json",
		success: function( queue ){
			console.log(queue);
			if (queue.name != null){
				core.bindForm2Object("form-add-queue",queue);
				openForm("queue-edit-container","btn-queue-add-cancel");
			}else{
				core.showStatus($error.findQueue,"error");
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
				//	{ title	: $label.id, data : "id" , className : "col-md-2", render : function( data, type, row){
				//		return  data  ;
				//	} },
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
callcenter.setupQueuesTable = function(){
	var id = $("#agentpbx-id").val();
	$.ajax({
		type : "GET",
		url : "findAllQueues?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function( queues ) {
			$("#centconf-queueid").empty();
			for (var i = 0; i < queues.length; i++) {
				$("#centconf-queueid").append( $('<option value="' + queues[i].id + '" >' + queues[i].name + '</option>') ); 
			}
			if (queueTable!=null){
				queueTable.destroy();
			}
			queueTable = $("#queues-table")
				.on('draw.dt', function(){
					core.hideWaitDialog();
				})
				.DataTable({
					data : queues,
					columns : 
						[
			//		{ title	: $label.id, data : "id" , className : "col-md-2", render : function( data, type, row){
			//			return  data  ;
			//		} },
					{ title	: $label.name, data : "name" , className : "col-md-4", render : function( data, type, row){
						return  data  ;
					} },
					{ title	: "+", data : "id" , className : "col-md-2", render : function( data, type, row){
					 return '<div class="btn-group">' + 
	                 		'<button id="actionBtn-"'+ row.id + '"  data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle " aria-expanded="false"><i class="fa fa-edit"></i>' 
	                 			+ '<span class="caret"></span></button>' 
		                 		+ '<ul class="dropdown-menu pull-right">' + 
		                 			'<li><a href="#" onclick="callcenter.editQueue(\'' + data +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
		                 			'<li class="divider"></li>'+
		                 			'<li><a href="#" onclick="callcenter.dropQueue(\'' + data + '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
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
callcenter.setupCentconfTable = function(){
	var id = $("#agentpbx-id").val();
	$.ajax({
		type : "GET",
		url : "findAllCentconf?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function( centconf ) {
		
			
			if (centconfTable!=null){
				centconfTable.destroy();
			}
			centconfTable = $("#centconfs-table")
				.on('draw.dt', function(){
					core.hideWaitDialog();
				})
				.DataTable({
					data : centconf,
					columns : 
						[
				//	{ title	: $label.id, data : "id" , className : "col-md-2", render : function( data, type, row){
				//		return  data  ;
				//	} },
					{ title	: $label.queues, data : "queuename" , className : "col-md-4", render : function( data, type, row){
						return  data  ;
					} },
					{ title	: $label.agents, data : "agentname" , className : "col-md-4", render : function( data, type, row){
						return  data  ;
					} },
					{ title	: $label.extension, data : "extention" , className : "", render : function( data, type, row){
						return  data  ;
					} },
					{ title	: $label.penalty, data : "penalty" , className : "", render : function( data, type, row){
						return  data  ;
					} },

					{ title	: "+", data : "id" , className : "col-md-2", render : function( data, type, row){
					 return '<div class="btn-group">' + 
	                 		'<button id="actionBtn-"'+ row.id + '"  data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle " aria-expanded="false"><i class="fa fa-edit"></i>' 
	                 			+ '<span class="caret"></span></button>' 
		                 		+ '<ul class="dropdown-menu pull-right">' + 
		                 			'<li><a href="#" onclick="callcenter.editCentconf(\'' + data +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
		                 			'<li class="divider"></li>'+
		                 			'<li><a href="#" onclick="callcenter.dropCentconf(\'' + data + '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
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
callcenter.setupTemplateTable = function(){
	
	$.ajax({
		type : "GET",
		url : "findAllTemplate",
		contentType : 'application/json',
		dataType : "json",
		success : function( data ) {
			if (templateTable!=null){
				templateTable.destroy();
			}
			templateTable = $("#template-table")
				.on('draw.dt', function(){
					core.hideWaitDialog();
				})
				.DataTable({
					data : data,
					columns : 
						[
					{ title	: $label.id, data : "id" , render : function( data, type, row){
						return  data  ;
					} },
					{ title	: $label.name, data : "name" ,  render : function( data, type, row){
						return  data  ;
					} },
					{ title	: "+", data : "id" ,  render : function( data, type, row){
					 return '<div class="btn-group">' + 
	                 		'<button id="actionBtn-"'+ row.id + '"  data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle " aria-expanded="false"><i class="fa fa-edit"></i>' 
	                 			+ '<span class="caret"></span></button>' 
		                 		+ '<ul class="dropdown-menu pull-right">' + 
		                 			'<li><a href="#" onclick="callcenter.editTemplate(\'' + data +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
		                 			'<li class="divider"></li>'+
		                 			'<li><a href="#" onclick="callcenter.dropTemplate(\'' + data + '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
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
saveCentconf = function(){
	var centconf = {
			id : "",
			agentid : "",
			queueid : "",
			extention : "",
			penalty : "",
			pbx	: $("#agentpbx-id").val()
			
	};
	var empty = core.testNotEmptyField("form-add-centconf");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-centconf", centconf);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "saveСentconf",
			  data: JSON.stringify( centconf ),
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
callcenter.dropCentconf = function( id ){
	var centconf = {
			pbx : $("#agentpbx-id").val(),
			id : id
	};
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
		  type: "POST",
		  url:  "dropCentconf",
		  data: JSON.stringify( centconf ),
		  contentType : 'application/json',
		  dataType: "json",
		  headers : headers ,    	
		  success: function(e){
			 
			  callcenter.action[e.message]();
			  
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});	
		
}
callcenter.editCentconf = function( id ){
	var pbxid = $("#agentpbx-id").val();
	$.ajax({
		type: "GET",
		url: "findCentconf?id=" + id + "&pbxid=" + pbxid,
		dataType: "json",
		success: function( centconf ){
			console.log(centconf);
			core.bindForm2Object("form-add-centconf",centconf);
			openForm("centconf-edit-container","btn-centconf-add-cancel");
		},
		error: function(e){
			 core.showStatus($error.network,"error");
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
	
	$("#btn-queue-add-cancel").click( function(){
		clearForm("form-add-queue");
		if ($("#queue-edit-container").hasClass("hidden")){
			openForm("queue-edit-container", "btn-queue-add-cancel");
			return;
		}
		if (!$("#queue-edit-container").hasClass("hidden") ){
			closeForm("queue-edit-container", "btn-queue-add-cancel" ,$button.addQueue );
			return;
		}
	});	
	$("#btn-centconf-add-cancel").click( function(){
		clearForm("form-add-centconf");
		if ($("#centconf-edit-container").hasClass("hidden")){
			openForm("centconf-edit-container", "btn-centconf-add-cancel");
			$("#centconf-penalty").val("0");
			return;
		}
		if (!$("#centconf-edit-container").hasClass("hidden") ){
			closeForm("centconf-edit-container", "btn-centconf-add-cancel" ,$button.addCentconf );
			return;
		}
	});	
	
	
	$("#btn-template-add-cancel").click( function(){
		clearForm("form-add-template");
		if ($("#template-edit-container").hasClass("hidden")){
			openForm("template-edit-container", "btn-template-add-cancel");
			
			return;
		}
		if (!$("#template-edit-container").hasClass("hidden") ){
			closeForm("template-edit-container", "btn-template-add-cancel" ,$button.addTemplate );
			return;
		}
	});	
	
	$("#btn-agent-save").click( function(){
		saveAgent();
	});
	$("#btn-peer-save").click( function(){
		savePeer();
	});
	$("#btn-queue-save").click( function(){
		saveQueue();
	});
	$("#btn-centconf-save").click( function(){
		saveCentconf();
	});
	$("#btn-template-save").click( function(){
		saveTemplate()
	});
}
$(document).ready(function() {

	callcenter.setupUI();
	callcenter.init();
	
});