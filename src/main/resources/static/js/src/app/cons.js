var cons = cons || {}, consTable = null;

cons.date1 = {};
cons.date2 = {};

cons.initQueus = function() {
	var id = $("#cons-pbxid").val();
	$.ajax({
		type : "GET",
		url : "findAllQueues?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(queues) {
			$("#cons-queuename").empty();
			for (var i = 0; i < queues.length; i++) {
				$("#cons-queuename").append(
						$('<option value="' + queues[i].id + '" >'
								+ queues[i].name + '</option>'));
			}
		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
}
cons.setEnable = function() {

	$("#cons-btn-toolbar").find('input').each(function(index) {
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
cons.createConsolidateReport = function(){
	var id = $("#cons-pbxid").val();
	var query = {
			id 	  : id,
			date1 : "",
			date2 : "",
			queuename : ""
			
	};
	core.bindObject2Form("form-show-cons", query);
	console.log(query);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	core.showWaitDialog();
	$.ajax({
			  type: "POST",
			  url:  "showConsolidate",
			  data: JSON.stringify( query ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				core.hideWaitDialog();
					console.log(e);
				  
			  	},error : function( e) {
				  //core.showStatus($error.network,"error");
			  		core.hideWaitDialog();
			  	}
		});	
}
cons.setupUI = function() {

	$("#cons-btn-apply").click(function() {

		cons.createConsolidateReport();

	});
	cons.date1 = jQuery('#cons-date1').datetimepicker({
		lang : 'ru',
		timepicker : false,
		format : 'd.m.Y',
		onChangeDateTime : function(dp, $input) {
			cons.date1 = $input.val();
		}
	});

	cons.date2 = jQuery('#cons-date2').datetimepicker({
		lang : 'ru',
		timepicker : false,
		format : 'd.m.Y',
		onChangeDateTime : function(dp, $input) {
			cons.date2 = $input.val();
		}
	});

}
cons.setPBX = function(id) {

	$("#cons-pbxid").val(id);
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {
			cons.setEnable();
			$("#cons-pbxname").val(config.astname);
			cons.initQueus();

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
}
cons.init = function() {

	$.ajax({
		type : "GET",
		url : "findAllconfig",
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {

			for (var i = 0; i < config.length; i++) {
				$("#cons-pbx-ul").append(
						$('<li><a href="#" onclick="cons.setPBX(\''
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
	
	cons.init();
	cons.setupUI();
	console.log('cons.js');
	
});