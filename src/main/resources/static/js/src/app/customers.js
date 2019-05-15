var customers = customers || {};
var customersTable = null; 

customers.action = {
		'CUSTOMER_SAVED' : function(){
			 core.showStatus($success.saveCustomer,"success");
			 customers.setupCustomersTable();
			 clearConfigForm();
			 closeCustomersForm();
		},
		'CUSTOMER_SAVE_ERROR' : function(){
			core.showStatus($error.saveCustomer,"error");
			customers.setupCustomersTable();
		},
		'CUSTOMER_NOT_DROPED' : function(){
			core.showStatus($error.dropCustomer,"error");
		},
		'CUSTOMER_DROPED' : function(){
			 core.showStatus($success.dropCustomer,"success");
			 customers.setupCustomersTable();
		}
	
}
function deleteCustomer(customer){
	
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
		  type: "POST",
		  url:  "deleteCustomer",
		  data: JSON.stringify(customer),
		  contentType : 'application/json',
		  dataType: "json",
		  headers : headers ,    	
		  success: function(e){
			  customers.action[e.message];
		  },error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});	
}
customers.dropCustomers = function(id){
	$.ajax({
		  type: "GET",
		  url:  "getCustomer?id=" + id,
		  dataType: "json",
		  success: function(customer){
			  if (customer != null){
				  if (confirm($label.delCustWarn + customer.phone + ' ?' )) {
					    
					  deleteCustomer(customer);
					  
					} else {
					    // Do nothing!
				    }
			  }
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});	
}
customers.editCustomers = function(id){
	$.ajax({
		  type: "GET",
		  url:  "getCustomer?id=" + id,
		  dataType: "json",
		  success: function(customer){
			  if (customer != null){
				  core.bindForm2Object("form-add-customers", customer); 
				  openCustomersForm();
			  }
		  	},error : function( e) {
			  core.showStatus($error.network,"error");
		  	}
	});	
}

customers.saveCustomer=function(){
	var data = {
			id : "",
			phone : "",
			name : ""
	};
	core.bindObject2Form("form-add-customers", data);
	var headers = {};
	var csrf = {};
	csrf = core.csrf(); 
	headers[csrf.headerName] = csrf.token;
	$.ajax({
			  type: "POST",
			  url:  "saveCustomer",
			  data: JSON.stringify(data),
			  contentType : 'application/json',
			  dataType: "json",
			  headers : headers ,    	
			  success: function(e){
				  customers.action[ e.message ]();
				
			  	},error : function( e) {
				  core.showStatus($error.network,"error");
			  	}
		});	
}

customers.init = function(){
	
	customers.setupCustomersTable();
	customers.setupGUI();
	
	
}
function openCustomersForm(){
	$("#customers-edit-container").removeClass( "hidden" );
	$("#btn-customers-add-cancel").removeClass("btn-primary");
	$("#btn-customers-add-cancel").addClass("btn-warning");
	$("#btn-customers-add-cancel").text( $button.cancel	 );
}
function closeCustomersForm(){
	$("#customers-edit-container").addClass( "hidden" )
	$("#btn-customers-add-cancel").addClass("btn-primary");
	$("#btn-customers-add-cancel").removeClass("btn-warning");
	$("#btn-customers-add-cancel").text( $button.addCustomer	);
}
function clearConfigForm(){
	$('#form-add-customers').find('input').each(function(){
		  $(this).val("") ;
	});
}
customers.setupGUI = function(){
	$("#btn-customers-save").click( function(){
		customers.saveCustomer();
	});
	
	$("#btn-customers-add-cancel").click( function(){
		clearConfigForm();
		if ($("#customers-edit-container").hasClass("hidden")){
			openCustomersForm();
			return;
		}
		if (!$("#customers-edit-container").hasClass("hidden") ){
			closeCustomersForm();
			return;
		}
	});
	
}
customers.setupCustomersTable = function(){

	$.ajax({
		type : "GET",
		url : "findAllCustomers",
		contentType : 'application/json',
		dataType : "json",
		success : function(customers) {
		
			if (customersTable!=null){
				customersTable.destroy();
			}
			
			customersTable = $("#customers-table")
				.on('draw.dt', function(){
					core.hideWaitDialog();
				})
				.DataTable({
					data : customers,
					columns : 
						[
					{ title	: $label.id, data : "id" , className : "", render : function( data, type, row){
						return  data  ;
					} },
					{ title	: $label.phone, data : "phone" , className : "col-md-3", render : function( data, type, row){
						return  data  ;
					} },
					{ title	: $label.name, data : "name" , className : "col-md-6", render : function( data, type, row){
						return  data  ;
					} },

					{ title	: "+", data : "id" , className : "col-md-2", render : function( data, type, row){
					 return '<div class="btn-group">' + 
	                 		'<button id="actionBtn-"'+ row.id + '"  data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle " aria-expanded="false"><i class="fa fa-edit"></i>' 
	                 			+ '<span class="caret"></span></button>' 
		                 		+ '<ul class="dropdown-menu pull-right">' + 
		                 			'<li><a href="#" onclick="customers.editCustomers(\'' + data +  '\')"><i class="fa fa-edit"></i><span style="padding-left: 5px;">' + $button.edit + '</span></a></li>' +
		                 			'<li class="divider"></li>'+
		                 			'<li><a href="#" onclick="customers.dropCustomers(\'' + data + '\')"><i class="fa fa-cut"></i><span style="padding-left: 5px;">' + $button.drop + '</span></a></li>' +
		                 		 '</ul>' + 
		                 		'</div>' ;
						} }
					],
					order: [[0, 'asc']],
					paging    : false,
					info 	 : false,
					searching : false, 
					iDisplayLength : 100,
					scrollY : 300
				});	
		     console.log(customers);

		},
		error : function(e) {
			core.showStatus($error.network, "error");
		}
	});

}
$(document).ready( function()
{
			
	customers.init();
});