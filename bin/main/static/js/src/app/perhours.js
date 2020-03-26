var perhours = perhours || {}, perhoursTable = null;

perhours.date1 = {};
perhours.date2 = {};

perhours.cellColor = {
		1 : function(){
			  return "#e8792a";
		},
		2 : function(){
			  return "#e85a2a";
		},  
		3 : function(){
			  return "#e8332a";
		},  
		4 : function(){
			  return "#c70704";
		},  
		5 : function(){
			  return "#b50f0d";
		},  
		6 : function(){
			  return "#941918";
		},  
		7 : function(){
			  return "#8a0b0a";
		}  

}
perhours.hours = {
		0 : function(){
			  return "7:00-8:00";
		},
		1 : function(){
			  return "8:00-9:00";
		},	
		2 : function(){
			  return "9:00-10:00";
		},	
		3 : function(){
			  return "10:00-11:00";
		},	
		4 : function(){
			  return "11:00-12:00";
		},	
		5 : function(){
			  return "12:00-13:00";
		},	
		6 : function(){
			  return "13:00-14:00";
		},	
		7 : function(){
			  return "14:00-15:00";
		},	
		8 : function(){
			  return "15:00-16:00";
		},	
		9 : function(){
			  return "16:00-17:00";
		},	
		10 : function(){
			  return "17:00-18:00";
		},	
		11 : function(){
			  return "18:00-19:00";
		},	
		12 : function(){
			  return "19:00-20:00";
		},	
		13 : function(){
			  return "20:00-21:00";
		},	
		14 : function(){
			  return "21:00-22:00";
		},	
		15 : function(){
			  return "22:00-23:00";
		}

}

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
	$("#export-to-excel").click(function(){
		$('table').tblToExcel();
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
				  createTable(e.cells);
			  },
			  error : function(e){
			
				  console.log(e); 
			  }
		});
	
}
createTable = function( data ) {
	 var elem = document.getElementById("perhours-table-container");
	 elem.innerHTML = "";
	 
	 var table  = document.createElement("table");
	 table.setAttribute("id","hours-table");
	 table.setAttribute("class","table table-bordered table-striped");
	 table.setAttribute("style","width:100%");
	 
	 
	 var tblBody = document.createElement("tbody");
	 var tr = document.createElement("tr");
	 tr.className="text-center";
	 var cell = document.createElement("td");
	 var cellText = document.createTextNode( "" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);	 
	 
	 for(var j=0; j < 31; j++ ){
		 var cell = document.createElement("td");
		 var cellText = document.createTextNode( j+1 );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);	 
	 }
	 cell = document.createElement("td");
	 cell.setAttribute("style","background-color: #f29705; color : #FFFFFF; font-weight: 600;");
	 cellText = document.createTextNode( "A" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);	
	 
	 cell = document.createElement("td");
	 cell.setAttribute("style","background-color: #06a125; color : #FFFFFF; font-weight: 600;");
	 cellText = document.createTextNode( "B" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);	
	 
	 cell = document.createElement("td");
	 cell.setAttribute("style","background-color: #f20505; color : #FFFFFF; font-weight: 600;");
	 cellText = document.createTextNode( "C" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);	
	 
	 cell = document.createElement("td");
	 cell.setAttribute("style","background-color: #0717f7; color : #FFFFFF; font-weight: 600;");
	 cellText = document.createTextNode( "%" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);	


	 tblBody.appendChild(tr);
	 
	 for(var i=0; i < 16; i++ ){
		 var tr  = document.createElement("tr");
		 var cell = document.createElement("td");
		 var cellText = document.createTextNode( perhours.hours[i]() );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);	 
		 for(var j=0; j < 31; j++ ){
			 var cell1 = document.createElement("td");
			 cell1.className="text-center";
			 var payload =  parseInt(data[i][j].payload);
			 var cellText1;
			 if (payload > 0 ){
				 var color = perhours.cellColor[payload]();
				 cell1.setAttribute("style","background-color: " + color + "; color : #FFFFFF; font-weight: 600;");
				 cellText1 = document.createTextNode( data[i][j].payload );
			 }else{
				 cellText1 = document.createTextNode( " ");
			 }
			 cell1.appendChild(cellText1);
			 tr.appendChild(cell1);
		 }
		 
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #f29705; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( data[i][j].eneter );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);
		 
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #06a125; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( data[i][j].connect );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);

		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #f20505; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( data[i][j].abandon );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);
		 
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #0717f7; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( data[i][j].value1 );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);
		 tblBody.appendChild(tr);
	 }
	 
		 
		 tr = document.createElement("tr");
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #f29705; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( $label.enter );
		 cell.appendChild(cellText);
		 tr.appendChild(cell); 
		 
		 for(var j=0; j < 31; j++ ){
			 cell = document.createElement("td");
			 cell.className="text-center";
			 cell.setAttribute("style","background-color: #f29705; color : #FFFFFF; font-weight: 600;");
			 cellText =  document.createTextNode( data[17][j].eneter );
			 cell.appendChild(cellText);
	  	     tr.appendChild(cell); 
		 	}
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #f29705; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode(  data[17][31].eneter );
		 cell.appendChild(cellText);
		 tr.appendChild(cell); 
		 
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #06a125; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( "" );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);
		 
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #f20505; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( "" );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);
		 
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #0717f7; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( "" );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);
	 
	 tblBody.appendChild(tr);
	 
	 
	 tr = document.createElement("tr");
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #06a125; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode(  $label.connect );
	 cell.appendChild(cellText);
	 tr.appendChild(cell); 
	 
	 for(var j=0; j < 31; j++ ){
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #06a125; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( data[17][j].connect );
		 cell.appendChild(cellText);
  	     tr.appendChild(cell); 
	 	}
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #06a125; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode( "" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell); 
	 
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #06a125; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode( data[17][31].connect );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);
	 
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #f20505; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode( "" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);
 
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #0717f7; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode( "" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);
	 
	 tblBody.appendChild(tr);

	 
	 tr = document.createElement("tr");
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #06a125; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode(  $label.abandon );
	 cell.appendChild(cellText);
	 tr.appendChild(cell); 
	 
	 for(var j=0; j < 31; j++ ){
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #f20505; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( data[17][j].abandon );
		 cell.appendChild(cellText);
  	     tr.appendChild(cell); 
	 	}
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #f20505; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode( "" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell); 
	 
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #f20505; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode( "" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);
	 
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #f20505; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode( data[17][31].abandon );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);
 	 
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #0717f7; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode( "" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);
	 
	 tblBody.appendChild(tr);

	 
	 
	 tr = document.createElement("tr");
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #0717f7; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode(  "%" );
	 cell.appendChild(cellText);
	 tr.appendChild(cell); 
	 
	 for(var j=0; j < 31; j++ ){
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #0717f7; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode( data[17][j].value1 );
		 cell.appendChild(cellText);
  	     tr.appendChild(cell); 
	 }
	 for(var i=0; i < 3; i++ ){
		 cell = document.createElement("td");
		 cell.className="text-center";
		 cell.setAttribute("style","background-color: #0717f7; color : #FFFFFF; font-weight: 600;");
		 cellText =  document.createTextNode(  ""  );
		 cell.appendChild(cellText);
		 tr.appendChild(cell);
	 }
	 
	 cell = document.createElement("td");
	 cell.className="text-center";
	 cell.setAttribute("style","background-color: #0717f7; color : #FFFFFF; font-weight: 600;");
	 cellText =  document.createTextNode(  data[17][31].value1  );
	 cell.appendChild(cellText);
	 tr.appendChild(cell);
	 
	 tblBody.appendChild(tr);
	 
	 table.appendChild(tblBody);
	 elem.appendChild(table);
	 
	
	  
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
//	perhours.initMonths( "#perhours-month2" );

	console.log('perhours.js');
	
});