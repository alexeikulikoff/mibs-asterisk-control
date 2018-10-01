var action = action || {};

action = {
	 'USER_SAVED' : function(){
		 core.showStatus($success.saveUser,"success");
	 },
	'USER_NOT_SAVED' : function(){
		  core.showStatus($error.saveUser,"error");
	}
}