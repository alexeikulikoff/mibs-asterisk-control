var cons = cons || {}, consTable = null;

cons.date1 = {};
cons.date2 = {};

var svgHeight = 300;
var my = 25;
var mx = 25;
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
	
	
	 for(var i=0; i < d.calls.length; i++){
		 s = s + '<tr><td class="text-center">' + d.calls[i].mDate + '</td><td class="text-center">' +  d.calls[i].accepted + '</td><td class="text-center">' +  d.calls[i].answered + '</td><td class="text-center">' +  d.calls[i].unanswered + '</td></tr>';  
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
					console.log(e);
					
					$("#cons-container").removeClass("hidden");
					
					$("#cons-report-date").html(query.date1 + '-' + query.date2);
					
				    $("#const-table-container").empty();
				    $("#const-table-container").append('<table class="table" id="cons-table" style="width: 100%"></table>');
				    
				    $("#cons-table").append('<tfoot><th></th><th></th><th></th><th></th><th></th><th></th></tfoot>');
				    
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
						"footerCallback": function ( row, data, start, end, display ) {
							 var api = this.api();
							  $( api.column( 2 ).footer() ).html($label.total);
							  
							  $( api.column( 3 ).footer() ).html(api
						                .column( 3 )
						                .data()
						                .reduce( function (a, b) {
						                    return a + b;
						                }, 0 )
						         );
							  
							  $( api.column( 4 ).footer() ).html(api
						                .column( 4 )
						                .data()
						                .reduce( function (a, b) {
						                    return a + b;
						                }, 0 )
						         );
							  $( api.column( 5 ).footer() ).html(api
						                .column( 5 )
						                .data()
						                .reduce( function (a, b) {
						                    return a + b;
						                }, 0 )
						         );
						},
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
			   
				cons.drawMonthGraph( e.data.map((s)=> ( { "x" : s.id, "y" :  s.totalAccepted, "m" : cons.month[s.month]()}) )  );
				
			  	},error : function( e) {
			  		$("#cons-container").addClass("hidden");
				    core.showStatus($error.network,"error");
			  		core.hideWaitDialog();
			  	}
		});	
}
cons.drawMonthGraph = function( data ){
	
	console.log(data);
	
	 $("#cons-graph-container").empty();
	 
	 var svgWidth = parseInt(d3.select('#cons-graph-container').style('width'), 10);
	 
	 var margin = {top: 20, right: 10, bottom: 20, left: 60},
	    width =  svgWidth  - margin.left - margin.right,
	    height = svgHeight - margin.top - margin.bottom;
	 
	 var svg = d3.select("#cons-graph-container").append("svg").attr("height", svgHeight).attr("width",svgWidth );
	 
	 var xsc = d3.scaleLinear().domain([d3.min( data.map((s)=> s.x) ) , d3.max( data.map((s)=> s.x) )]).range([margin.left, width]);
	 //var xsc = d3.scaleLinear().domain([1 , ( data.map((s)=> s.x) ).length]).range([margin.left, width]);
	 
	 
	 var ysc = d3.scaleLinear().domain([0,d3.max( data.map((s)=> s.y) )]).range([height, margin.top]);

	
	 svg.append("g").attr("transform", "translate(0," + (height+5) + ")").call(d3.axisBottom(xsc)
	 						//.ticks( data.length )
			 				.tickValues([])
	 						//.tickSizeOuter(0)
	 						);
	 
	 svg.append("g").attr("transform", "translate(55,0)").call(d3.axisLeft(ysc).tickSizeOuter(0));
	 
	 svg.append("g").selectAll("text")
	 				.data(data)
	 				.enter()
	 				.append("text")
	 				.attr("x", function(d) { return xsc(d.x)  })
	 				.attr("y", function(d) { return svgHeight-15; })
	 				.text(function(d) { return d.m });
	 
	 var area1 = svg.append("path")
     				.datum(data)
     				.attr("fill", "#cce5df")
     				.attr("stroke", "#69b3a2")
     				.attr("stroke-width", 1.5)
     				.attr("d", d3.area()
     						.x(function(d) { return xsc(d.x) })
     						.y0( height )
     						.y1(function(d) { return ysc(d.y) })
     						.curve(d3.curveCatmullRom.alpha(0.75))
     					);
    
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