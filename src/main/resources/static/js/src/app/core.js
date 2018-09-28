var core = core || {};

core.csrf = function() {
	var result = null;
	$.ajax({
		type : 'GET',
		url : 'csrf',
		dataType : 'json',
		async : false,
		success : function(e) {
			console.log(e);
			result = e;
		},

		error : function(e) {
			location.href = "/asterisk-control/login";
		}
	});
	return result;
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
				location.href = "/mars/login";
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
		
		if ($(this).is("select")) {
			var idd = "#" + $(this).attr("id") + " option";
			$(idd).each(function() {
		         console.log( $(this).val() );
		         $(this).removeAttr('selected'); 
		    });
			var txt = obj[$(this).attr('id').split("-")[1]];
			$(this).find("option[value=" + txt + "]").attr("selected", true);
		}
		if ($(this).is("input")) {
			$(this).val(obj[$(this).attr('id').split("-")[1]]);
		}
	});
	//$('#' + formId).hide().fadeIn('fast');
}
core.bindObject2Form = function(formId, obj) {
	$('#' + formId).find('input, select').each(function() {
		if ($(this).is("select")) {
			var id = $(this).attr('id');
			var selector = "#" + id + " option:selected";
			obj[id.split("-")[1]] = $(selector).text() + "!!!";
		}
		if ($(this).is("input")) {
			obj[$(this).attr('id').split("-")[1]] = $(this).val() + "???";
		}

	});
	//$('#' + formId).hide().fadeIn('fast');
}
