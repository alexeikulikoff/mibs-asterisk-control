var start ={};

start.showAvailableAstersisk = function(){
	$("#start-select-asterisks").empty();
	$.ajax({
		type: "GET",
		url: "showAvailableAstersisk",
		dataType: "json",
		success: function(e){
			for(var i=0; i < e.asterisks.length; i++){
				$("#start-select-asterisks").append($("<option></option>")
		                    .attr("value",e.asterisks[i].id)
		                    .text(e.asterisks[i].name)); 
			}
		},error : function(e){
			core.showStatus($error.network,"error");
		}
	});	
}
start.init = function(){
	$("body").addClass("modal-open");
	$("body").attr("style","padding-right: 14px;");
	$("body").append('<div id="modal-backdrop" class="modal-backdrop in"></div>');
	
	$( "#start-modal-form" ).addClass("in");
	$( "#start-modal-form" ).attr("style","display: block;");
	
	start.showAvailableAstersisk();
}
start.go = function(){
	$( "body" ).removeClass("modal-open");
	$( "body" ).attr("style","");
	$( "#modal-backdrop" ).remove();
	
	$( "#start-modal-form" ).removeClass("in");
	$( "#start-modal-form" ).attr("style","display: none;");
	
	var selector = "#start-select-asterisks option:selected";
	
	$("#start-asterisk-name").text( $(selector).text() );
	$("#start-asterisk-id").val($(selector).attr("value"));
	
}
$(document).ready( function()
{
	start.init();
	
	$("#btn-close-start").click( function(){
		location.href = "/asterisk-control/login";
	});
	$("#btn-start-go").click( function(){
		start.go();
	})
	
});