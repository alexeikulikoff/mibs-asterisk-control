var cdr = cdr || {};



cdr.date1 = {};
cdr.date2 = {};


cdr.setPBX = function(id) {
	$("#pbx-id").val(id);
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
	}).end().find('select').each(function( index ) {
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

cdr.showCDR = function( page ){
	
	var id = $("#pbx-id").val();
	
	var query = {
			id 	  : id,
			date1 : "",
			date2 : "",
			phone : "",
			page  : page,
			disposition : "",
			direction : ""
	};
	var empty = core.testNotEmptyField("form-show-cdr");
	if ( empty ) {
		return ;
	}
	core.bindObject2Form("form-show-cdr", query);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	console.log(query);
	$.ajax({
			  type: "POST",
			  url:  "showCDR",
			  data: JSON.stringify( query ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				 
				 console.log(e);
				  
			  	},error : function( e) {
				  //core.showStatus($error.network,"error");
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
	 
	$("#cdr-btn-apply").click( function(){
		cdr.showCDR(1);
	});
	 
}
$(document).ready(function() {

	cdr.setupUI();
	cdr.init();
});
