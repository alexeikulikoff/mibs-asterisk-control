var queues = queues || {};

queues.date1 = {};
queues.date2 = {};

queues.setEnable = function(){
	
	$( "#queues-btn-toolbar" ).find('input').each(function( index ) {
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
	
	
}
queues.setPBX = function(id){
	
	$("#queues-pbx-id").val(id);
	
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success: function(config){
			queues.setEnable();
			$("#queues-pbx-name").val(config.astname);
			queues.initQueus();
			queues.initAgents();
			
		},
 	    error : function(e) {
		  core.showStatus($error.network, "error");
		}
	});
}
queues.initQueus = function(){
	var id = $("#queues-pbx-id").val();
	$.ajax({
		type : "GET",
		url : "findAllQueues?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function( queues ) {
			$("#queues-queue").empty();
			for (var i = 0; i < queues.length; i++) {
				$("#queues-queue").append( $('<option value="' + queues[i].id + '" >' + queues[i].name + '</option>') ); 
			}
		},error : function(e) {
			core.showStatus($error.network, "error");
		}
	});	
}
queues.initAgents = function(){
	var id = $("#queues-pbx-id").val();
	$.ajax({
		type : "GET",
		url : "findAllAgents?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(agents) {
		
			$("#queues-agent").empty();
			
			for (var i = 0; i < agents.length; i++) {
				$("#queues-agent").append( $('<option value="' + agents[i].id + '" >' + agents[i].name + '</option>') ); 
			}
		},error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
			
}
queues.init = function(){
	
	$.ajax({
		type : "GET",
		url : "findAllconfig",
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {
		
			for (var i = 0; i < config.length; i++) {
				$("#queues-pbx-ul").append(
						$('<li><a href="#" onclick="queues.setPBX(\''
								+ config[i].id + '\')">' + config[i].astname
								+ '</a></li>'));
			}

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});

}
queues.setupUI = function(){
	  
	queues.date1 = jQuery('#queues-date1').datetimepicker({
		lang:'ru',
		timepicker: false,
		format: 'd.m.Y',
		onChangeDateTime: function(dp,$input){
			queues.date1 = $input.val();
		}					 
	 });
	 
	queues.date2 = jQuery('#queues-date2').datetimepicker({
			lang:'ru',
			timepicker: false,
			format: 'd.m.Y',
			onChangeDateTime: function(dp,$input){
				queues.date2 = $input.val();
			}					 
	 });
	 
}
$(document).ready(function() {
	queues.setupUI();
	queues.init();
	
	
});