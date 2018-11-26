var queues = queues || {},
	queueTable = null,
	queueDetailTable = null;

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
	
	$("#queues-pbxid").val(id);
	
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success: function(config){
			queues.setEnable();
			$("#queues-pbxname").val(config.astname);
			
			
			queues.initQueus();
			queues.initAgents();
			
		},
 	    error : function(e) {
		  core.showStatus($error.network, "error");
		}
	});
}
queues.initQueus = function(){
	var id = $("#queues-pbxid").val();
	$.ajax({
		type : "GET",
		url : "findAllQueues?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function( queues ) {
			$("#queues-queueid").empty();
			for (var i = 0; i < queues.length; i++) {
				$("#queues-queueid").append( $('<option value="' + queues[i].id + '" >' + queues[i].name + '</option>') ); 
			}
		},error : function(e) {
			core.showStatus($error.network, "error");
		}
	});	
}
queues.initAgents = function(){
	var id = $("#queues-pbxid").val();
	$.ajax({
		type : "GET",
		url : "findAllAgents?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(agents) {
		
			$("#queues-agentid").empty();
			
			for (var i = 0; i < agents.length; i++) {
				$("#queues-agentid").append( $('<option value="' + agents[i].id + '" >' + agents[i].name + '</option>') ); 
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
	
	$("#queues-btn-apply").click(function(){
		queues.showQueueSpell(1);
	});
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
queues.createTable = function( dataSet ){
	if (queueTable != null){
		queueTable.destroy();
	}
	queueTable = $("#queues-table")
	  	.on('draw.dt', function(){
		  core.hideWaitDialog();
		 })
	    .DataTable({
			data: dataSet.records,
		 	columns:
		 	  [
			    { title : $label.date,  data : "date"},
				{ title : $label.enter,  data : "enterTime"},
		 		{ title : $label.exit,  data : "exitTime"},
		 		{ title : $label.phone,  data : "peer"},
		 		{ title : $label.calls,  data : "calls"},
		 		{ title : $label.show,  data : "date", render : function(data, type, row ){
		 		  return '<button type="button" class="btn btn-primary btn-xs" onclick="queues.queueDetail(\'' + row.date +'\',\'' +  row.enterTime + '\',\'' +  row.exitTime  + '\',\'' +  row.peer +  '\',\'' + dataSet.queue + '\')">' + $button.show + '</button>';
		 		}}
			 ],
			paging: false,
			info: false,
			searching: false 
	    });	
}
queues.queueDetail = function(date,enterTime,exitTime,peer,queue){
	var pbxid = $("#queues-pbxid").val();
	var query = {
			date1 : date + " " + enterTime ,
			date2 : date + " " + exitTime ,
			peer : peer,
			queue  : queue,
			pbxid : pbxid 
	};
	core.showWaitDialog();
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "queueDetail",
			  data: JSON.stringify( query ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function( report ){
				
				  queues.createQueueDetailTable( report );
			//	  $("#inbound-queues-container").removeClass("hidden");
				  
			//	  $("#queue-report-header").text($label.operator + ": " +  report.agent + ",  " + $label.queue + ": " + report.queue );
				  core.hideWaitDialog();
				  
			  	},error : function( e) {
				  //core.showStatus($error.network,"error");
			  		core.hideWaitDialog();
			  	}
		});	
} 
queues.createQueueDetailTable = function(dataSet){
	
	$("#inbound-queues-detail-container").empty();
	$("#inbound-queues-detail-container").append('<table class="table" id="queues-detail-table"></table>');
	
	if (queueDetailTable != null){
		queueDetailTable.destroy();
	}
	queueDetailTable = $("#queues-detail-table")
	  	.on('draw.dt', function(){
		  core.hideWaitDialog();
		 })
	    .DataTable({
			data: dataSet,
		 	columns:
		 	  [
			    { title : $label.date,  data : "callTime"},
				{ title : $label.phone,  data : "src"},
		 		{ title : $label.duration,  data : "duration"},
		 		{ title : $label.record,  data : "uniqueid", render : function(data, type, row ){
		 		  return '<button type="button" class="btn btn-primary btn-xs" onclick="queues.playSound(\'' + data + '\')">' + $button.listen + '</button>';
		 		}}
			 ],
			paging: false,
			info: false,
			searching: false 
	    });	
	
}
queues.showQueueSpell = function( page ){
	
		var query = {
				pbxid 	  : "",
				date1 : "",
				date2 : "",
				page  : page,
				agentid : "",
				queueid : ""
		};
		var empty = core.testNotEmptyField("form-show-queues");
		if ( empty ) {
			return ;
		}
		core.showWaitDialog();
		core.bindObject2Form("form-show-queues", query);
		var headers = {};
		var csrf = {};
		csrf = core.csrf(); 
		headers[csrf.headerName] = csrf.token;
		$.ajax({
				  type: "POST",
				  url:  "showQueueReport",
				  data: JSON.stringify( query ),
				  contentType : 'application/json',
				  dataType: "json",
				  headers : headers ,    	
				  success: function( report ){
					
					  queues.createTable( report );
					  $("#inbound-queues-container").removeClass("hidden");
					  
					  $("#queue-report-header").text($label.operator + ": " +  report.agent + ",  " + $label.queue + ": " + report.queue );
					  core.hideWaitDialog();
					  
				  	},error : function( e) {
					  //core.showStatus($error.network,"error");
				  		core.hideWaitDialog();
				  	}
			});	
}
$(document).ready(function() {
	queues.setupUI();
	queues.init();
	
	
});