var setting ={};

setting.init = function(){
	users.init();
	config.init();
} 
$(document).ready( function()
{
	setting.init();
});