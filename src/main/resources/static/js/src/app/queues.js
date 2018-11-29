var queues = queues || {},
	soundpath = null,
	qplayer,
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
			soundpath = config.soundpath;
			
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
	var page = 1;
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
		 		  return '<button type="button" class="btn btn-primary btn-xs" onclick="queues.queueDetail(\'' + page + '\',\'' + row.date +'\',\'' +  row.enterTime + '\',\'' +  row.exitTime  + '\',\'' +  row.peer +  '\',\'' + dataSet.queue + '\')">' + $button.show + '</button>';
		 		}}
			 ],
			paging: false,
			info: false,
			searching: false 
	    });	
}
queues.queueDetail = function(page, date,enterTime,exitTime,peer,queue){
	var pbxid = $("#queues-pbxid").val();
	var query = {
			date1 : date + " " + enterTime,
			date2 : date + " " + exitTime,
			peer : peer,
			queue  : queue,
			pbxid : pbxid,
			page : page
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
			  success: function( dataSet ){
				  
					 if(dataSet.records == null ) {
						 
						 $("#inbound-queues-detail-container").empty();
						 
						  core.showStatus($error.showdata,"error");
					 }else{
						  queues.createQueueDetailTable( dataSet, page, date,enterTime,exitTime,peer,queue );
					 }
					 
		
				  core.hideWaitDialog();
				  
			  	},error : function( e) {
				  //core.showStatus($error.network,"error");
			  		core.hideWaitDialog();
			  	}
		});	
} 
queues.createQueueDetailTable = function(e, page, date,enterTime,exitTime,peer,queue){
	
	$("#inbound-queues-detail-container").empty();
	$("#inbound-queues-detail-container").append('<div class="col-md-12"><div id="tableQueues_paginate class="dataTables_paginate paging_simple_numbers"><ul id="queues-page-tab" class="pagination"></ul></div></div>');
	$("#inbound-queues-detail-container").append('<table class="table" id="queues-detail-table"></table>');
	
	
	if (queueDetailTable != null){
		queueDetailTable.destroy();
	}
	queueDetailTable = $("#queues-detail-table")
	  	.on('draw.dt', function(){
		  core.hideWaitDialog();
		 })
		 .on( 'click', 'tr', function () {
			 if ( $(this).hasClass('selected') ) {
				 $(this).removeClass('selected');
			 }
			 else {
				 queueDetailTable.$('tr.selected').removeClass('selected');
				 $(this).addClass('selected');
			 }
		 })		 
	    .DataTable({
			data: e.records,
		 	columns:
		 	  [
			    { title : $label.date,  data : "callTime"},
				{ title : $label.phone,  data : "src"},
		 		{ title : $label.duration,  data : "duration"},
		 		{ title : $label.record,  data : "uniqueid", render : function(data, type, row ){
		 		 
		 			 var data1 = data.split("\.")[0] + "-" + data.split("\.")[1]; 
		 			 
		 			return '<button  id="qplay-' + data1 + '" type="button" class="btn btn-success btn-xs" onclick="queues.playSound(\'' + data1 +  '\')"><i class="fa fa-play"></i></button>' + 
		 		  		 '<div class="btn-toolbar btn-toolbar-sound"> '+
		 		  		 '<button id="qstop-'+ data1 + '" class="btn btn-danger btn-xs hidden" onclick="queues.stopSound(\'' + data1 + '\')" ><i class="fa fa-stop"></i></button>' +
		 		  		 '<button id="qdownload-'+ data1 + '" class="btn btn-success btn-xs hidden" onclick="queues.downloadSound(\'' + data + '\')" ><i class="fa fa-download"></i></button>' +
		 		  		 '<button id="qsound_error-' + data1 + '" class="btn btn-xs btn-danger hidden" ><i class="fa fa-exclamation-triangle"></i></button>'+
		 		  		 '</div>';
		 		  
		 		}}
			 ],
			language: {
			  search: ""
			},
			paging: false,
			info: false,
			searching: true 
	    });	
	
	
	$("#queues-page-tab").empty();
	
	for(var k=0; k < e.tabs.length; k++){
		
		
			if (e.tabs[k].caption == "Next"){
			//	e.pageTab[k].caption = button_next;
			}
			if (e.tabs[k].caption == "Previous"){
			//	data[0][k].caption = button_previous;
			}
		var a = $('<li/>', {
			    'id':'page-' + e.tabs[k].p,
			    'class': e.tabs[k].cssClass,
			    'html':'<a  href="#" aria-controls="queues-detail-table" onclick="queues.queueDetail(\''+ e.tabs[k].p + '\',\'' + date +'\',\'' + enterTime + '\',\'' + exitTime  + '\',\'' + peer +  '\',\'' + queue + '\')" >' + e.tabs[k].caption + '</a>'
			}).appendTo('#queues-page-tab');
	} 
	
	$("#queues-detail-table_filter").empty();
	$("#queues-detail-table_filter").append('<div class="input-group date" data-provide="datepicker">' + 
								   '<span class="input-group-addon" ><i class="fa fa-search"></i></span> ' +
								   '<input type="search" class="form-control form-control-sm" placeholder="" aria-controls="cdr-table" style="padding-left: 0px;">'+
								    '</div>');
	
	
	
}
queues.createPlayer = function(data){
	if(qplayer != null){
		$("#queue_player").jPlayer( "destroy" );
	}
	var sound_file = data.split("-")[0] + "." + data.split("-")[1]+".mp3";
	var sound_url = soundpath + sound_file;
	var stopId = "#qstop-" +sound_file;
	var playId = "#qplay-" +sound_file;
	qplayer = $("#queue_player").jPlayer({
		 errorAlerts: true,
          ready: function () {
            $(this).jPlayer("setMedia", {
              mp3: sound_url
            	  
            }).jPlayer("play");
          },
          error: function (event) {
              $("#qsound_error-" + data).removeClass("hidden");
          	  $("#qplay-" + data).addClass("hidden");
			  $("#qstop-" + data).addClass("hidden");
			  $("#qdownload-" + data).addClass("hidden");
              
          },
          swfPath: "/js",
          supplied: "mp3",
	          cssSelectorAncestor: "",
	          cssSelector: {
//	        	play: playId,
	            stop: stopId
	          }
      });
	  $("#qdownload-" + data).removeClass("hidden");
}	

queues.playSound = function(data){
	$("#qplay-" + data).addClass("hidden");
	$("#qstop-" + data).removeClass("hidden");
	
	if(qplayer != null){
		$("#queue_player").jPlayer( "destroy" );
	}
	queueDetailTable.rows().eq(0).each( function ( index ) {
		var row = queueDetailTable.row( index );
		var tr = row.node();
		if ($(row.node().childNodes[3].childNodes[0]).attr('id') != "qplay-" + data){
			$(row.node().childNodes[3].childNodes[0]).removeClass("hidden");
			$(row.node().childNodes[3].childNodes[1].childNodes[1]).addClass("hidden");
			$(row.node().childNodes[3].childNodes[1].childNodes[2]).addClass("hidden");
			$(row.node().childNodes[3].childNodes[1].childNodes[3]).addClass("hidden");
		}
		
		
	});
	queues.createPlayer(data);
}
queues.stopSound = function(data){
	$("#qplay-" + data).removeClass("hidden");
	$("#qstop-" + data).addClass("hidden");
	$("#qdownload-" + data).addClass("hidden");
	
	if(qplayer != null){
		$("#queue_player").jPlayer( "destroy" );
	}
}
queues.downloadSound = function(filename) {
	var curURL = window.location.href;
	var ind = curURL.lastIndexOf("/");
	var ur = curURL.substring(0,ind+1);
	var url = soundpath  +   filename + '.mp3';
    var pom = document.createElement('a');
    pom.setAttribute('href', url);
    pom.setAttribute('download', 'sound_' + filename+'.mp3');
    if (document.createEvent) {
        var event = document.createEvent('MouseEvents');
        event.initEvent('click', true, true);
        pom.dispatchEvent(event);
    }
    else {
        pom.click();
    }
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