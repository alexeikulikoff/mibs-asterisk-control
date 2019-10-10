var perhours = perhours || {}, perhoursTable = null;

perhours.date1 = {};
perhours.date2 = {};

perhours.month = {
		
		"1" : function(){
			return $month.jan;
		},
		"2" : function(){
			return $month.feb;
		},
		"3" : function(){
			return $month.mar;
		},
		"4" : function(){
			return $month.apr;
		},
		"5" : function(){
			return $month.may;
		},
		"6" : function(){
			return $month.jun;
		},
		"7" : function(){
			return $month.jul;
		},
		"8" : function(){
			return $month.aug;
		},
		"9" : function(){
			return $month.sep;
		},
		"10" : function(){
			return $month.oct;
		},
		"11" : function(){
			return $month.nov;
		},
		"12" : function(){
			return $month.dec;
		},

		
}
perhours.initMonths = function( id ){
	
	$( id ).empty();
	var obj = perhours.month;
	for (var key in obj) {
	    if (obj.hasOwnProperty(key)) {
	        $(  id ).append(
	        		$('<option value="' +key + '" >' +  obj[key]() + '</option>')
	          );
	    }
	}
}
perhours.initQueus = function() {
	var id = $("#perhours-pbxid").val();
	$.ajax({
		type : "GET",
		url : "findAllQueues?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(queues) {
			$("#perhours-queuename").empty();
			for (var i = 0; i < queues.length; i++) {
				$("#perhours-queuename").append(
						$('<option value="' + queues[i].id + '" >'
								+ queues[i].name + '</option>'));
			}
		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
}
perhours.setEnable = function() {

	$("#perhours-btn-toolbar").find('input').each(function(index) {
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

}
 
perhours.setupUI = function() {

	$("#perhours-btn-apply").click(function() {
		perhours.createperhoursolidateReport();
	});
}
perhours.createperhoursolidateReport = function(){
	
	var query = {
			pbxid : "", 
			month1 : "",
			month2 : "",
			year : "",
			queuename : ""
	};
	
	core.bindObject2Form("form-show-perhours", query);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	core.showWaitDialog();
	
	$.ajax({
			  type: "POST",
			  url:  "showPerhours",
			  data: JSON.stringify( query ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  core.hideWaitDialog();
				  console.log(e);
			  },
			  error : function(e){
			
				  console.log(e); 
			  }
		});
	
}
perhours.setPBX = function(id) {

	$("#perhours-pbxid").val(id);
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {
			perhours.setEnable();
			$("#perhours-pbxname").val(config.astname);
			perhours.initQueus();

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
}
perhours.init = function() {

	$.ajax({
		type : "GET",
		url : "findAllconfig",
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {

			for (var i = 0; i < config.length; i++) {
				$("#perhours-pbx-ul").append(
						$('<li><a href="#" onclick="perhours.setPBX(\''
								+ config[i].id + '\')">' + config[i].astname
								+ '</a></li>'));
			}

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});

}
$(document).ready(function() {
	
	perhours.init();
	perhours.setupUI();
	perhours.initMonths( "#perhours-month1" );
	perhours.initMonths( "#perhours-month2" );

	console.log('perhours.js');
	
});