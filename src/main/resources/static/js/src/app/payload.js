var payload = payload || {} , payloadTable = null ;

payload.date1 = {};
payload.date2 = {};

payload.setEnable = function() {

	$("#payload-btn-toolbar").find('input').each(function(index) {
		var id = $(this).attr("id").split("-")[1];
		if (id.startsWith("date")) {
			$(this).removeAttr("disabled");
			$(this).val(core.getCurrentDate());
		}
		

	}).end().find('button').each(function(index) {
		$(this).removeAttr("disabled");
	}).end().find('select').each(function(index) {
		$(this).removeAttr("disabled");
	});

}
payload.setPBX = function(id) {

	$("#payload-pbxid").val(id);
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {
			payload.setEnable();
			$("#payload-pbxname").val(config.astname);
			soundpath = config.soundpath;

			payload.initQueus();
			

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
}
payload.initQueus = function() {
	var id = $("#payload-pbxid").val();
	$.ajax({
		type : "GET",
		url : "findAllQueues?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success : function(queues) {
			$("#payload-queueid").empty();
			for (var i = 0; i < queues.length; i++) {
				$("#payload-queueid").append(
						$('<option value="' + queues[i].id + '" >'
								+ queues[i].name + '</option>'));
			}
		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
}
payload.init = function() {

	$.ajax({
		type : "GET",
		url : "findAllconfig",
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {

			for (var i = 0; i < config.length; i++) {
				$("#payload-pbx-ul").append(
						$('<li><a href="#" onclick="payload.setPBX(\''
								+ config[i].id + '\')">' + config[i].astname
								+ '</a></li>'));
			}

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});

}
payload.showAgentReport = function() {
	var pbxid = $("#payload-pbxid").val();
	var query = {
		date1 : "",
		date2 : "",
		peer : "",
		queue : "",
		pbxid : pbxid,
		page : ""
	};
	core.showWaitDialog();
	core.bindObject2Form("form-show-payload", query);
	var headers = {};
	var csrf = {};
	csrf = core.csrf();
	headers[csrf.headerName] = csrf.token;
	
	if (payloadTable != null) {
		payloadTable.destroy();
	}
	
	$.ajax({
				type : "POST",
				url : "showPayload",
				data : JSON.stringify(query),
				contentType : 'application/json',
				dataType : "json",
				headers : headers,
				success : function(dataSet) {
					console.log(dataSet);
					$("#pauload-container").removeClass("hidden");
					
					$("#payload-date").text(' ' + query.date1 + ' - ' + query.date2);
					$("#total-answered").text(dataSet.totalanswered);
					$("#total-auanswered").text(dataSet.totalunanswered);
					$("#total-persent").text(dataSet.totalpersent +"%");
					
					
					payloadTable = $("#payload-table")
					
					.on('draw.dt', function() {
						core.hideWaitDialog();
						
						
						
					})
					.DataTable(
							{
								data : dataSet.payload,
								columns : [
										{
											title : $label.id,
											data : "id"
										},
										{
											title : $label.calldate,
											data : "time1", render : function( data, type, row){
												return row.time1 + " " + row.time2;
											}
										},
										{
											title : $label.answered,
											data : "answered",
											className : "text-center"
										},
										{
											title : $label.unanswered,
											data : "unanswered",
											className : "text-center"
										},
										{
											title : "%",
											data : "persent",
											render : function( data, type, row){
												return data + "%" ;
											}
											
										},
										{
											title : $label.agents,
											data : "agents",
											className : "text-center"
										},
										{
											title : "+",
											data : "prediction"
										}
									 ],
								paging : false,
								info : false,
								searching : false
							});
					
					
				},
				error : function(e) {
					// core.showStatus($error.network,"error");
					core.hideWaitDialog();
				}
			});

}
payload.setupUI = function() {

	$("#payload-btn-apply").click(function() {

	
		payload.showAgentReport();

	});
	payload.date1 = jQuery('#payload-date1').datetimepicker({
		lang : 'ru',
		timepicker : false,
		format : 'd.m.Y',
		onChangeDateTime : function(dp, $input) {
			payload.date1 = $input.val();
		}
	});

	payload.date2 = jQuery('#payload-date2').datetimepicker({
		lang : 'ru',
		timepicker : false,
		format : 'd.m.Y',
		onChangeDateTime : function(dp, $input) {
			payload.date2 = $input.val();
		}
	});

}
$(document).ready(function() {
	payload.setupUI();
	payload.init();

});