var cdr = cdr || {},
	soundpath = null,
	qplayer,
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
			soundpath = config.soundpath;
		
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
	
	$.ajax({
			  type: "POST",
			  url:  "showCDR",
			  data: JSON.stringify( query ),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				core.hideWaitDialog();
				 if(e.records == null ) {
					 	$("#cdr-table-wrapper").empty();
						$("#cdr-page-tab").empty();
						core.showStatus($error.showdata,"error");
				 }else{
					 cdr.createTable(e); 
				 }
				 
				  
			  	},error : function( e) {
				  //core.showStatus($error.network,"error");
			  		core.hideWaitDialog();
			  	}
		});	
	
	
}
cdr.createTable = function( e ){
	
	
	$("#cdr-table-wrapper").empty();
	$("#cdr-table-wrapper").append('<table class="table" id="cdr-table"></table>');

	if (cdrTable != null){
		cdrTable.destroy();
	}
	
	cdrTable = $("#cdr-table")
	  	 .on('draw.dt', function(){
				core.hideWaitDialog();
		 })
		 .on( 'click', 'tr', function () {
			 if ( $(this).hasClass('selected') ) {
				 $(this).removeClass('selected');
			 }
			 else {
				 cdrTable.$('tr.selected').removeClass('selected');
				 $(this).addClass('selected');
			}
		 })		 
	    .DataTable({
					 	data : e.records,
					 	columns : [
					 				{ title	 : "#", 	data : "id" },
					 				{title : $label.calldate,  data : "calldate"},
					 				{title : $label.src,  data : "src"},
					 				{title : $label.dst,  data : "dst"},
					 				{title : $label.channel,  data : "channel"},
					 				{title : $label.dstchannel,  data : "dstchannel"},
					 				{title : $label.sound,  data : "uniqueid", render : function(data){
						 			    var data1 = data.split("\.")[0] + "-" + data.split("\.")[1]; 
							 			return '<button  id="qplay-' + data1 + '" type="button" class="btn btn-success btn-xs" onclick="cdr.playSound(\'' + data1 +  '\')"><i class="fa fa-play"></i></button>' + 
							 		  		 '<div class="btn-toolbar btn-toolbar-sound"> '+
							 		  		 '<button id="qstop-'+ data1 + '" class="btn btn-danger btn-xs hidden" onclick="cdr.stopSound(\'' + data1 + '\')" ><i class="fa fa-stop"></i></button>' +
							 		  		 '<button id="qdownload-'+ data1 + '" class="btn btn-xs btn-success hidden" onclick="cdr.downloadSound(\'' + data + '\')" ><i class="fa fa-download"></i></button>'+
							 		  		 '<button id="qsound_error-' + data1 + '" class="btn btn-xs btn-danger hidden" role="alert"><i class="fa fa-exclamation-triangle"></i></button>'+
							 		  		 '</div>';
					 				}}
					 		],
					 		language: {
							  search: ''
							},
							paging: false,
							info:     false,
							searching : true 
							
	    });
	
	
	$("#cdr-page-tab").empty();
	
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
			    'html':'<a  href="#" aria-controls="tableCDR" onclick="cdr.showCDR(\'' + e.tabs[k].p + '\')" >' + e.tabs[k].caption + '</a>'
			}).appendTo('#cdr-page-tab');
	} 

	
	$("#cdr-table_filter").empty();
	$("#cdr-table_filter").append('<div class="input-group date" data-provide="datepicker">' + 
								   '<span class="input-group-addon" ><i class="fa fa-search"></i></span> ' +
								   '<input type="search" class="form-control form-control-sm" placeholder="" aria-controls="cdr-table" style="padding-left: 0px;">'+
								    '</div>');
	
	
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
cdr.createPlayer = function(data){
	if(qplayer != null){
		$("#cdr_player").jPlayer( "destroy" );
	}
	var sound_file = data.split("-")[0] + "." + data.split("-")[1]+".mp3";
	var sound_url = soundpath + sound_file;
	var stopId = "#qstop-" +sound_file;
	var playId = "#qplay-" +sound_file;
	qplayer = $("#cdr_player").jPlayer({
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

cdr.playSound = function(data){
	$("#qplay-" + data).addClass("hidden");
	$("#qstop-" + data).removeClass("hidden");
	
	if(qplayer != null){
		$("#cdr_player").jPlayer( "destroy" );
	}
	cdrTable.rows().eq(0).each( function ( index ) {
		var row = cdrTable.row( index );
		var tr = row.node();
		if ($(row.node().childNodes[6].childNodes[0]).attr('id') != "qplay-" + data){
			$(row.node().childNodes[6].childNodes[0]).removeClass("hidden");
			$(row.node().childNodes[6].childNodes[1].childNodes[1]).addClass("hidden");
			$(row.node().childNodes[6].childNodes[1].childNodes[2]).addClass("hidden");
			$(row.node().childNodes[6].childNodes[1].childNodes[3]).addClass("hidden");
		}
		
		
	});
	cdr.createPlayer(data);
}
cdr.stopSound = function(data){
	$("#qplay-" + data).removeClass("hidden");
	$("#qstop-" + data).addClass("hidden");
	$("#qdownload-" + data).addClass("hidden");
	
	if(qplayer != null){
		$("#cdr_player").jPlayer( "destroy" );
	}
}
cdr.downloadSound = function(filename) {
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

	cdr.setupUI();
	cdr.init();
	
});
