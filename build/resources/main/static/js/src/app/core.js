var core = core || {};

core.jsconfig = {
		serverContextPath : ""	
};

core.csrf = function() {
	var result = null;
	$.ajax({
		type : 'GET',
		url : 'csrf',
		dataType : 'json',
		async : false,
		success : function(e) {
			
			result = e;
		},

		error : function(e) {
			location.href = "/asterisk-control/login";
		}
	});
	return result;
}
core.getJSconfig = function(){
	$.ajax({
		  type: "GET",
		  url:  "jsconfig",
		  contentType : 'application/json',
		  dataType: "json",
		  success: function( responce ){
			  core.jsconfig.serverContextPath =  responce.serverContextPath;
			  
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});		
}
core.showStatus = function(msg, type) {
	setTimeout(function() {
		toastr.options = {
			"closeButton" : false,
			"debug" : false,
			"newestOnTop" : false,
			"progressBar" : false,
			"positionClass" : "toast-top-right",
			"preventDuplicates" : false,
			"onclick" : null,
			"showDuration" : "300",
			"hideDuration" : "1000",
			"timeOut" : "5000",
			"extendedTimeOut" : "1000",
			"showEasing" : "swing",
			"hideEasing" : "linear",
			"showMethod" : "fadeIn",
			"hideMethod" : "fadeOut"
		};
		toastr[type](msg);

		if ((type == "error") && (msg == $error.network)) {
			setTimeout(function() {
				location.href = "/asterisk-control/login";
			}, 1300);
		}

	}, 1300);
}
core.hideWaitDialog = function() {
	$("#wait_dialog").addClass("hidden");
}
core.showWaitDialog = function() {
	$("#wait_dialog").removeClass("hidden");
}
core.bindForm2Object = function(formId, obj) {
	$('#' + formId).find('input, select').each(function() {
		
		var i =  $(this).attr('id').split("-")[1];
		
		if ($(this).is("select")) {
			var idd = "#" + $(this).attr("id") + " option";
			$(idd).each(function() {
		       
		         $(this).removeAttr('selected'); 
		    });
			var txt = obj[ i ];
			
			$(this).find("option[value=" + txt + "]").attr("selected", true);
		}
		if ($(this).is("input")) {
			if ($(this).attr('type') == 'checkbox'){
				if (obj[ i ] == 'Yes'){
					$(this).prop('checked', true);
				}else{
					$(this).prop('checked', false);
				}
			}else{
				$(this).val(obj[ i ]);
			}
		}
	});

}
core.bindObject2Form = function(formId, obj) {
	$('#' + formId).find('input, select').each(function() {
		var i =  $(this).attr('id').split("-")[1];
		if ($(this).is("select")) {
			var id = $(this).attr('id');
			var selector = "#" + id + " option:selected";
			obj[id.split("-")[1]] = $(selector).val();
		}
		if ($(this).is("input")) {
			if ($(this).attr('type') == 'checkbox'){
				if ( $(this).is(':checked') ){
					obj[ i ] = 'Yes';
				}else{
					obj[ i ] = 'No';
				}
			}else{
				obj[ i ] = $(this).val();
			}	
		}
	});

}

core.testNotEmptyField = function( formId ){
	var result = false;
	$('#' + formId).find('input').each(function(){
		if ($(this).val().length == 0 ){
			if ($(this).attr('type') != "hidden"){
	             $(this).parent().addClass('has-error');
	             result =  true;
	        }
	    }else{
	       if ( $(this).parent().hasClass('has-error') ){
	           $(this).parent().removeClass('has-error');
	         }
	      }
	 });
	 return result;
}

core.getCurrentDate = function(){
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();
	if(dd<10) {
	    dd='0'+dd
	} 
	if(mm<10) {
	    mm='0'+mm
	} 
	today = dd + '.'+ mm + '.' + yyyy;
	return today;
}

core.clearForm = function( id ){
	$('#' + id).find('input').each(function(){
		  $(this).val("") ;
	});
}


core.disableElemtnt = function( id ){

	$("#" + id).attr("disabled", true);

}
core.enableElemtnt = function( id ){

	$("#" + id).removeAttr("disabled");

}