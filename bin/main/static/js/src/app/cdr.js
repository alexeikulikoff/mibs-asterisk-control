var cdr = cdr || {};

cdr.setPBX = function(id) {
	$("#cdr-pbx-id").val(id);
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success: function(config){
			$("#cdr-pbx-name").val(config.astname);
			
		
		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
}

cdr.init = function() {
	$.ajax({
		type : "GET",
		url : "findAllconfig",
		contentType : 'application/json',
		dataType : "json",
		success : function(config) {
		
			for (var i = 0; i < config.length; i++) {
				$("#cdr-ul").append(
						$('<li><a href="#" onclick="cdr.setPBX(\''
								+ config[i].id + '\')">' + config[i].astname
								+ '</a></li>'));
			}

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});

}
cdr.setupUI = function(){
	  $('#data_1 .input-group.date').datepicker({
          todayBtn: "linked",
          keyboardNavigation: false,
          forceParse: false,
          calendarWeeks: true,
          autoclose: true
      });

}
$(document).ready(function() {

	cdr.setupUI();
	cdr.init();
});
