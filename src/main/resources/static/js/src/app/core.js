var core = core || {};


core.hideWaitDialog = function(){
	$("#wait_dialog").addClass("hidden");
}
core.showWaitDialog = function(){
	$("#wait_dialog").removeClass("hidden");
}