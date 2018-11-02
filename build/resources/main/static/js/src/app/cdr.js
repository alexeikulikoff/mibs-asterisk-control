var cdr = cdr || {};



cdr.date1 = {};
cdr.date2 = {};


cdr.setPBX = function(id) {
	$("#cdr-pbx-id").val(id);
	$.ajax({
		type : "GET",
		url : "findConfig?id=" + id,
		contentType : 'application/json',
		dataType : "json",
		success: function(config){
			
			cdr.setEnable();
			$("#cdr-pbx-name").val(config.astname);
			
		
		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});
}

function enable(){
	var attr = $(this).attr('disabled');
	if (typeof attr !== typeof undefined && attr !== false) {
		$(this).removeAttr("disabled");
	}
}
cdr.setEnable = function(){
	console.log("fuck");
	$( "#cdr-btn-toolbar" ).find('input').each(function( index ) {
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
	 console.log('setup UI');
	  
	 cdr.date1 = jQuery('#cdr-date1').datetimepicker({
		lang:'ru',
		timepicker: false,
		format: 'd.m.Y',
		onChangeDateTime: function(dp,$input){
		  cdr.date1 = $input.val();
		}					 
	 });
	 
	 cdr.date2 = jQuery('#cdr-date2').datetimepicker({
			lang:'ru',
			timepicker: false,
			format: 'd.m.Y',
			onChangeDateTime: function(dp,$input){
			  cdr.date2 = $input.val();
			}					 
	 });
	 
	
	 
}
$(document).ready(function() {

	cdr.setupUI();
	cdr.init();
});
