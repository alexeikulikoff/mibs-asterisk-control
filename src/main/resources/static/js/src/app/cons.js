var cons = cons || {}, consTable = null;

cons.date1 = {};
cons.date2 = {};

cons.month = {
		
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
function format ( d ) {
	
	var s = '<table class="table dataTable no-footer" style="width: 100%;"><tbody>' + 
	         '<tr><td class="text-center"><b>' + $label.date + '</b></td><td class="text-center"><b>' + $label.enter + '</b></td><td class="text-center"><b>' +$label.connect + '</b></td><td class="text-center"><b>' + $label.abandon + '</b></td></tr>' ;
	
	
	 for(var i=0; i < d.consLine.length; i++){
		 s = s + '<tr><td class="text-center">' + d.consLine[i].mDate + '</td><td class="text-center">' +  d.consLine[i].accepted + '</td><td class="text-center">' +  d.consLine[i].answered + '</td><td class="text-center">' +  d.consLine[i].unanswered + '</td></tr>';  
	 }
	 s = s + '</tbody></table>';
	return s;
	
	
}
 
cons.createConsolidateReport = function(){
	if (consTable !=  null){
		consTable.destroy();
	}
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
					
				    $("#const-table-container").empty();
				    $("#const-table-container").append('<table class="table" id="cons-table" style="width: 100%"></table>');
				    
					
					consTable = $("#cons-table")
					.on('click', 'td.details-control', function () {
					        var tr = $(this).closest('tr');
					        var row = consTable.row( tr );
					 
					        if ( row.child.isShown() ) {
					            // This row is already open - close it
					            row.child.hide();
					            tr.removeClass('shown');
					        }
					        else {
					            // Open this row
					            row.child( format(row.data()) ).show();
					            tr.addClass('shown');
					        }
					  } )
					.DataTable({
						  data: e.data,
						  columns: [
					         
					            {
					                "className":      'details-control',
					                "orderable":      false,
					                "data":           null,
					                "defaultContent": ''
					            },
					            { title: "#", data : "id",  "orderable": true },
					            { title: $label.date, data : "month", render : function(data){
					            	return cons.month[data]();
					            } },
					            { title: $label.enter , className: "text-center",  data : "totalAccepted" },
					            { title: $label.connect, className: "text-center", data : "totalAnswered" },
					            { title: $label.abandon, className: "text-center", data : "totalUnanswered" }
					            
					        ],
					        paging: false,
							info:     false,
							searching : false 
					});
					
					/* $('#cons-table tbody').on('click', 'td.details-control', function () {
					        var tr = $(this).closest('tr');
					        var row = consTable.row( tr );
					 
					        if ( row.child.isShown() ) {
					            // This row is already open - close it
					            row.child.hide();
					            tr.removeClass('shown');
					        }
					        else {
					            // Open this row
					            row.child( format(row.data()) ).show();
					            tr.addClass('shown');
					        }
					    } );
					*/
					
				  
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