var outbound = outbound || {};

outbound.date1 = {}; 
outbound.date2 = {};

var selectTable = null;
var targetTable = null;
var selectPhone = [];
var targetPhone = [];

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

			queues.initQueus();
			queues.initAgents();

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
outbound.removeTarget = function(id){
	
	var elem = targetPhone.filter(s=>{
		return s.id == id ;
	});
	selectPhone = [elem[0],...selectPhone];
	outbound.updateSelectTable(selectPhone);
	
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
			
			selectPhone = data;
			outbound.updateSelectTable(selectPhone);
		
		},
		error : function(e) {
			// core.showStatus($error.network,"error");
			core.hideWaitDialog();
			
		}
	});	
	
}
outbound.setupUI = function() {
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
$(document).ready(function() {
	outbound.setupUI();
	outbound.init();
	outbound.initSelectTable();
	
	console.log('hello');

});