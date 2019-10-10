var outbound = outbound || {};

outbound.date1 = {}; 
outbound.date2 = {};

var selectTable = null;
var targetTable = null;
var outboundConsolidateTable = null;
var detailedReport = null;
var selectPhone = [];
var targetPhone = [];
var qplayer;

outbound.setEnable = function() {

	$("#outbound-btn-toolbar").find('input').each(function(index) {
		var id = $(this).attr("id").split("-")[1];
		if (id.startsWith("date")) {
			$(this).removeAttr("disabled");
			$(this).val(core.getCurrentDate());
		}
		if (id.startsWith("phone")) {
			$(this).removeAttr("disabled");
		}

	}).end().find('button').each(function(index) {
		$(this).removeAttr("disabled");
	}).end().find('select').each(function(index) {
		$(this).removeAttr("disabled");
	});
	
	$("#outbound-btn-apply").removeAttr("disabled");

}
outbound.setPBX = function(id) {

	$("#outbound-pbxid").val(id);
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {
			outbound.setEnable();
			$("#outbound-pbxname").val(config.astname);
			soundpath = config.soundpath;

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
}

outbound.init = function() {

	$.ajax({
		type : "GET",
		url : "findAllconfig",
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {

			for (var i = 0; i < config.length; i++) {
				$("#outbound-pbx-ul").append(
						$('<li><a href="#" onclick="outbound.setPBX(\''
								+ config[i].id + '\')">' + config[i].astname
								+ '</a></li>'));
			}

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
	

}

outbound.updateTargetTable = function( data ){
	if (targetTable != null) {
		targetTable.destroy();
	}
	$("#target-table-container").empty();
	$("#target-table-container").append('<table class="table" id="target-phone-table" style="width: 100%"></table>');
	
	targetTable = $("#target-phone-table")
	.on('draw.dt', function() {
		core.hideWaitDialog();
	})
	.DataTable({
				data : data,
				columns : [
							{
								title : '-',
								data : "id",
								render : function(data, type, row) {
									return '<button type="button" class="btn btn-primary btn-xs" onclick="outbound.removeTarget(\''
												+ data
												+ '\')">'
												+ '<i class="fa fa-arrow-left"></i>'
												+ '</button>';
									}
						    },
							{
								title : $label.phone,
								data : "phone"
							},
							{
								title : $label.name,
								data : "office"
							}
						
						],
						iDisplayLength : 100,
						scrollY: 250,
						paging : false,
						info : false,
						searching : true,
						language: {
						     search: "",
						     zeroRecords: "Записи отсутствуют.",
						     emptyTable: "В таблице отсутствуют данные",
						  }
						
	});
		
}

outbound.updateSelectTable = function( data ){
	if (selectTable != null) {
		selectTable.destroy();
	}
	$("#select-table-container").empty();
	$("#select-table-container").append('<table class="table" id="outbound-select-phone-table" style="width: 100%"></table>');
	
	selectTable = $("#outbound-select-phone-table")
	.on('draw.dt', function() {
		core.hideWaitDialog();
	})
	.DataTable({
				data : data,
				columns : [
							
							 {
								title : $label.phone,
								data : "phone"
							},
							{
								title : $label.name,
								data : "office"
							},
							{
								title : '+',
								data : "id",
								render : function(data, type, row) {
									return '<button type="button" class="btn btn-primary btn-xs" onclick="outbound.addSelected(\''
												+ data
												+ '\')">'
												+ '<i class="fa fa-arrow-right"></i>'
												+ '</button>';
									}
						    }
						],
						iDisplayLength : 100,
						scrollY: 250,
						paging : false,
						info : false,
						searching : true,
						language: {
						     search: "",
						     zeroRecords: "Записи отсутствуют.",
						     emptyTable: "В таблице отсутствуют данные",
						  }
						
	});
		
}
outbound.removeTarget = function(id){
	
	var elem = targetPhone.filter(s=>{
		return s.id == id ;
	});
	
	var rs = selectPhone.filter((s)=>{
		return s.id == elem[0].id;
	});
	if (rs.length == 0 ){
		selectPhone = [elem[0],...selectPhone];
		outbound.updateSelectTable(selectPhone);

	}
	
	targetPhone = targetPhone.filter(s=>{
		return s.id != id ;
	}).map((s)=>{
		return s;
	});	
	outbound.updateTargetTable(targetPhone);
}
outbound.addSelected = function(id){
	var elem = selectPhone.filter(s=>{
		return s.id == id ;
	});
	targetPhone = [elem[0], ...targetPhone];
	outbound.updateTargetTable(targetPhone);

	selectPhone = selectPhone.filter(s=>{
		return s.id != id ;
	}).map((s)=>{
		return s;
	});
	outbound.updateSelectTable(selectPhone);
	
}
outbound.initSelectTable = function(){
	
	core.showWaitDialog();
	var headers = {};
	var csrf = {};
	csrf = core.csrf();
	headers[csrf.headerName] = csrf.token;
	$.ajax({
		type : "POST",
		url : "getAllEquipments",
		data : JSON.stringify(''),
		contentType : 'application/json',
		dataType : "json",
		headers : headers,
		success : function(data) {
			
			selectPhone = data.map((s)=>{
				return {id: s.id, phone: s.phone, office : s.office};
			});
			console.log(selectPhone);
			outbound.updateSelectTable(selectPhone);
		
		},
		error : function(e) {
			// core.showStatus($error.network,"error");
			core.hideWaitDialog();
			
		}
	});	
	
}
outbound.initTargetTable = function(){
	core.showWaitDialog();
	var headers = {};
	var csrf = {};
	csrf = core.csrf();
	headers[csrf.headerName] = csrf.token;
	$.ajax({
		type : "POST",
		url : "getSavedPhones",
		data : JSON.stringify(''),
		contentType : 'application/json',
		dataType : "json",
		headers : headers,
		success : function(data) {
			
			targetPhone = data;
			outbound.updateTargetTable(targetPhone);
		
		},
		error : function(e) {
			// core.showStatus($error.network,"error");
			core.hideWaitDialog();
			
		}
	});		
}
outbound.saveSelectedPhones = function(){

	var query = { container : targetPhone};
	
	var headers = {};
	var csrf = {};
	csrf = core.csrf();
	headers[csrf.headerName] = csrf.token;
	
	$.ajax({
				type : "POST",
				url : "saveSelectedPhones",
				data : JSON.stringify(query),
				contentType : 'application/json',
				dataType : "json",
				headers : headers,
				success : function(e) {
					console.log(e);
				},
				error : function(e) {
					// core.showStatus($error.network,"error");
					core.hideWaitDialog();
				}
	});		

}
outbound.showDetailedReport = function(data){
	console.log(data);
	
   var query = data;
	
	var headers = {};
	var csrf = {};
	csrf = core.csrf();
	headers[csrf.headerName] = csrf.token;
	
	$.ajax({
				type : "POST",
				url : "showDetailedReport",
				data : JSON.stringify(query),
				contentType : 'application/json',
				dataType : "json",
				headers : headers,
				success : function(e) {
					outbound.createDetailedReport(e);
				},
				error : function(e) {
					// core.showStatus($error.network,"error");
					core.hideWaitDialog();
				}
	});		
}
outbound.createDetailedReport = function( data ){
	
	if (detailedReport != null) {
		detailedReport.destroy();
	}
	
	$("#outbound-container").removeClass("hidden");
	$("#outbound-table-container").empty();
	$("#outbound-table-container").append('<table class="table" id="outbound-detail-table"></table>');
	
	
	detailedReport = $("#outbound-detail-table")
	.on('draw.dt', function() {
			core.hideWaitDialog();
	})
	.DataTable({
			data : data,
			columns : [
				{ title	 : "#", 	data : "id" },
 				{title : $label.calldate,  data : "calldate"},
 				{title : $label.dst,  data : "dst"},
 				{title : $label.duration,  data : "duration"},
 				{title : '*', data : "disposition", render : function(data){
 					
 					switch(data){
 						case 'ANSWERED': return '<span class="text-info">' + data + '</span>';
 						case 'BUSY' : return '<span class="text-warning">' + data + '</span>';
 						case  'NO ANSWER' : return '<span class="text-danger">' + data + '</span>';
 						default:
 							return data;
 					}
 				}},
 				{title : $label.sound,  data : "uniqueid", render : function(data){
	 			    var data1 = data.split("\.")[0] + "-" + data.split("\.")[1]; 
		 			return '<button  id="outbound_qplay-' + data1 + '" type="button" class="btn btn-success btn-xs" onclick="outbound.playSound(\'' + data1 +  '\')"><i class="fa fa-play"></i></button>' + 
		 		  		 '<div class="btn-toolbar btn-toolbar-sound"> '+
		 		  		 '<button id="outbound_qstop-'+ data1 + '" class="btn btn-danger btn-xs hidden" onclick="outbound.stopSound(\'' + data1 + '\')" ><i class="fa fa-stop"></i></button>' +
		 		  		 '<button id="outbound_qdownload-'+ data1 + '" class="btn btn-xs btn-success hidden" onclick="outbound.downloadSound(\'' + data + '\')" ><i class="fa fa-download"></i></button>'+
		 		  		 '<button id="outbound_qsound_error-' + data1 + '" class="btn btn-xs btn-danger hidden" role="alert"><i class="fa fa-exclamation-triangle"></i></button>'+
		 		  		 '</div>';
 				}}
						
					],
					iDisplayLength : 100,
					scrollY: 400,
					paging : false,
					info : false,
					searching : true,
					language: {
					     search: "",
					     zeroRecords: "Записи отсутствуют.",
					     emptyTable: "В таблице отсутствуют данные",
					  }
					
});
	
	
	
}
outbound.createConsolidateReportTable = function( data ){
	
	if (outboundConsolidateTable != null) {
		outboundConsolidateTable.destroy();
	}
	$("#outbound-consolidate-report-container").empty();
	$("#outbound-consolidate-report-container").append('<table class="table" id="outbound-consolidate-report-table"></table>');
	
	
	outboundConsolidateTable = $("#outbound-consolidate-report-table")
		.on('draw.dt', function() {
				core.hideWaitDialog();
		})
		.DataTable({
				data : data.records,
				columns : [
							
							 {
								title : $label.phone,
								data : "phone"
							},
							{
								title : $label.calls,
								data : "calls"
							},
							{
								title : $label.duration,
								data : "duration2"
							},
							{
								title : '+',
								data : "phone",
								render : function(data, type, row) {
									return '<button type="button" class="btn btn-primary btn-xs" onclick="outbound.showDetailedReport(\''
												+ data
												+ '\')">'
												+ $label.show
												+ '</button>';
									}
						    }
						],
						iDisplayLength : 100,
						scrollY: 400,
						paging : false,
						info : false,
						searching : false,
						language: {
						     search: "",
						     zeroRecords: "Записи отсутствуют.",
						     emptyTable: "В таблице отсутствуют данные",
						  }
						
	});
}
outbound.createOutboundConsolidateReport = function(){
	var id = $("#outbound-pbxid").val();
	var d1 = $("#outbound-date1").val();
	var d2 = $("#outbound-date2").val();
	
	core.showWaitDialog();
	
	var query = { pbxid : id,
			 date1 : d1,
			 date2 : d2,
			 phones : targetPhone};

		var headers = {};
		var csrf = {};
		csrf = core.csrf();
		headers[csrf.headerName] = csrf.token;

		$.ajax({
			type : "POST",
			url : "createOutboundConsolidateReport",
			data : JSON.stringify(query),
			contentType : 'application/json',
			dataType : "json",
			headers : headers,
			success : function(e) {
				outbound.createConsolidateReportTable(e);
				$("#total-calls").text(e.totalCalls);
				$("#total-duration").text(e.totalduration2);
				$("#outbound-detail-container").removeClass("hidden");
			},
			error : function(e) {
				// core.showStatus($error.network,"error");
				core.hideWaitDialog();
			}
});		
	
}
outbound.setupUI = function() {
	
	$("#save-selected-phones").click(function(){
		outbound.saveSelectedPhones();
	});
	$("#outbound-apply").click(function(){
		outbound.createOutboundConsolidateReport();
	});
	outbound.date1 = jQuery('#outbound-date1').datetimepicker({
		lang : 'ru',
		timepicker : false,
		format : 'd.m.Y',
		onChangeDateTime : function(dp, $input) {
			outbound.date1 = $input.val();
		}
	});

	outbound.date2 = jQuery('#outbound-date2').datetimepicker({
		lang : 'ru',
		timepicker : false,
		format : 'd.m.Y',
		onChangeDateTime : function(dp, $input) {
			outbound.date2 = $input.val();
		}
	});	
}
outbound.createPlayer = function(data){
	if(qplayer != null){
		$("#outbound_player").jPlayer( "destroy" );
	}
	var sound_file = data.split("-")[0] + "." + data.split("-")[1]+".mp3";
	var sound_url = soundpath + sound_file;
	var stopId = "#outbound_qstop-" +sound_file;
	var playId = "#outbound_qplay-" +sound_file;
	qplayer = $("#outbound_player").jPlayer({
		 errorAlerts: true,
          ready: function () {
            $(this).jPlayer("setMedia", {
              mp3: sound_url
            	  
            }).jPlayer("play");
          },
          error: function (event) {
        	 
              $("#outbound_qsound_error-" + data).removeClass("hidden");
          	  $("#outbound_qplay-" + data).addClass("hidden");
			  $("#outbound_qstop-" + data).addClass("hidden");
			  $("#outbound_qdownload-" + data).addClass("hidden");
              
             
          },
          swfPath: "/js",
          supplied: "mp3",
	          cssSelectorAncestor: "",
	          cssSelector: {
//	        	play: playId,
	            stop: stopId
	          }
      });
	  $("#outbound_qdownload-" + data).removeClass("hidden");
}	

outbound.playSound = function(data){
	$("#outbound_qplay-" + data).addClass("hidden");
	$("#outbound_qstop-" + data).removeClass("hidden");
	
	if(qplayer != null){
		$("#outbound_player").jPlayer( "destroy" );
	}
	detailedReport.rows().eq(0).each( function ( index ) {
		var row = detailedReport.row( index );
		var tr = row.node();
		if ($(row.node().childNodes[5].childNodes[0]).attr('id') != "outbound_qplay-" + data){
			$(row.node().childNodes[5].childNodes[0]).removeClass("hidden");
			$(row.node().childNodes[5].childNodes[1].childNodes[1]).addClass("hidden");
			$(row.node().childNodes[5].childNodes[1].childNodes[2]).addClass("hidden");
			$(row.node().childNodes[5].childNodes[1].childNodes[3]).addClass("hidden");
		}
		
		
	});
	outbound.createPlayer(data);
}
outbound.stopSound = function(data){
	$("#outbound_qplay-" + data).removeClass("hidden");
	$("#outbound_qstop-" + data).addClass("hidden");
	$("#outbound_qdownload-" + data).addClass("hidden");
	
	if(qplayer != null){
		$("#outbound_player").jPlayer( "destroy" );
	}
}
outbound.downloadSound = function(filename) {
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

$(document).ready(function() {
	outbound.setupUI();
	outbound.init();
	outbound.initSelectTable();
	outbound.initTargetTable();
	
	console.log('hello');

});