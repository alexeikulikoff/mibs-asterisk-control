var cdr = cdr || {},
		cdrTable = null;



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
	core.showWaitDialog();
	
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
				  cdr.createTable(e);
				
				  
			  	},error : function( e) {
				  //core.showStatus($error.network,"error");
			  		core.hideWaitDialog();
			  	}
		});	
	
	
}
cdr.createTable = function( dataSet  ){
	if (cdrTable != null){
		cdrTable.destroy();
	}
	var data = [];
	data.push({id : "12345"});
	data.push({id : "12346"});			
	
	cdrTable = $("#cdr-table")
	  	.on('draw.dt', function(){
				core.hideWaitDialog();
		 })
	    .DataTable({
					 	data : dataSet,
					 	columns : [
					 				{ title	 : "#", 	data : "id" },
					 				{title : $label.calldate,  data : "calldate"},
					 				{title : $label.src,  data : "src"},
					 				{title : $label.dst,  data : "dst"},
					 				{title : $label.channel,  data : "channel"},
					 				{title : $label.dstchannel,  data : "dstchannel"},
					 				{title : $label.sound,  data : "uniqueid", render : function(data){
					 					return '<div class="btn-group">' + 
					 							'<button data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle" aria-expanded="false">' + 
					 							'<span class="caret"></span></button>' + 
					 							'<ul class="dropdown-menu pull-right"><li>' + 
					 							'<a href="#" onclick="cdr.Play(\'' + data + '\')"><i class="fa fa-play"></i>' + 
					 							'<span style="padding-left: 5px;">' + 
					 							$label.play+
					 							'</span></a></li><li class="divider"></li><li>' + 
					 							'<a href="#" onclick="cdr.download(\'' + data + '\')"><i class="fa fa-download"></i>' + 
					 							'<span style="padding-left: 5px;">'+
					 							$label.download +
					 							'</span></a></li></ul></div>';
					 					
					 					
					 				}}
					 				
					 				
					 		],
							paging: false,
							info:     false,
							searching : true 
							
	    });
}
cdr.setupUI = function(){
	
	  
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
	$('[data-toggle="tooltip"]').tooltip(); 
	
}
$(document).ready(function() {

	cdr.setupUI();
	cdr.init();
	
});
