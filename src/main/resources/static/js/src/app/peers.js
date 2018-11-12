var peers = peers || {},
		  peerTable = null
;

peers.action = {
		'PEER_SAVED' : function(){
			 core.showStatus($success.saveEquipment,"success");
			 peers.setupAgentsTable();
			 closeForm();
		 },
		'PEER_NOT_SAVED' : function(){
			  core.showStatus($error.saveEquipment,"error");
		},
		'PEER_DROPED'  : function(){
			 core.showStatus($success.dropEquipment,"success");
			 peers.setupAgentsTable();
		},
		'PEER_NOT_DROPED'  : function(){
			 core.showStatus($error.dropEquipment,"error");
			
		}
	}

/*peers.setEnable = function(){
	
	$( "#peers-btn-toolbar" ).find('input').each(function( index ) {
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
	
	$("#peer-table-container").removeClass("hidden");
	
}*/

peers.dropPeer = function(id){
	var peer = {
			pbxid : $("#agentpbx-id").val(),
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
			 
			  peers.action[e.message]();
			  
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});	
	
}


peers.setupPeersTable = function(){
	
	
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
		                 			'<li><a href="#" onclick="peers.editPeer(\'' + data +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
		                 			'<li class="divider"></li>'+
		                 			'<li><a href="#" onclick="peers.dropPeer(\'' + data + '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
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
	$("#peer-edit-container").removeClass( "hidden" );
	$("#btn-peer-add-cancel").removeClass("btn-primary");
	$("#btn-peer-add-cancel").addClass("btn-warning");
	$("#btn-peer-add-cancel").text( $button.cancel	 );
}
function closeForm(){
	$("#peer-edit-container").addClass( "hidden" )
	$("#btn-peer-add-cancel").addClass("btn-primary");
	$("#btn-peer-add-cancel").removeClass("btn-warning");
	$("#btn-peer-add-cancel").text( $button.addAgent	);
}
function clearForm(){
	$('#form-add-peer').find('input').each(function(){
		  $(this).val("") ;
	});
}
function savePeer(){
	var peer = {
			id : "",
			name : "",
			pbxid	: $("#agentpbx-id").val()
			
	};
	var empty = core.testNotEmptyField("form-add-peer");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-add-peer", agent);
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
				  peers.action[e.message]();
			  	},error : function( e) {
			  	//	console.log(e);
				  core.showStatus($error.network,"error");
			  	}
		});	
	
}
peers.editPeer = function( id ){
	
	var pbxid = $("#agentpbx-id").val();
	
	$.ajax({
		type: "GET",
		url: "findPeer?id=" + id + "&pbxid=" + pbxid,
		dataType: "json",
		success: function( peer ){
			console.log(peer);
			if (peer.name != null){
				core.bindForm2Object("form-add-peer",peer);
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
peers.setupUI = function(){
	
	
	$("#btn-peer-add-cancel").click( function(){
		console.log("setupGUI peers");
		clearForm();
		if ($("#peer-edit-container").hasClass("hidden")){
			openForm();
			return;
		}
		if (!$("#peer-edit-container").hasClass("hidden") ){
			closeForm();
			return;
		}
	});	
	
	$("#btn-peer-save").click( function(){
		savepeer();
	});
}
$(document).ready(function() {

	peers.setupUI();
	peers.init();
	
});