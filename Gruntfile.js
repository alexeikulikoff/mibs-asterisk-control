/*global module:false*/
module.exports = function(grunt) {

	 grunt.initConfig({
		 	concat_css: {
			    options: {},
			    all: {
			        src: [ 
			        		'src/main/resources/static/css/src/animate.css',
						  //'src/main/resources/static/css/src/bootstrap.css',
						  //'src/main/resources/static/css/src/bootstrap.css.map',
							'src/main/resources/static/css/src/bootstrap.min.css',
							'src/main/resources/static/css/src/bootstrap.min.css.map',
							'src/main/resources/static/css/src/datatables.min.css',
							'src/main/resources/static/css/src/metisMenu.min.css',
							'src/main/resources/static/css/src/metisMenu.min.css.map',
						  //'src/main/resources/static/css/src/toastr.css',
							'src/main/resources/static/css/src/toastr.js.map',
							'src/main/resources/static/css/src/toastr.min.css',
							'src/main/resources/static/css/src/style.css'
			        
			        ],
			        dest: "src/main/resources/static/css/dist/asterisk-control.css"
			      },
			  },
			cssmin: {
				  target: {
				    files: [{
				      expand: true,
				      cwd: 'src/main/resources/static/css/dist',
				      src: ['*.css', '!*.min.css'],
				      dest: 'src/main/resources/static/css/dist',
				      ext: '.min.css'
				    }]
				  }
				},  
		 	clean: {
		 		options: { force: true },
		 		js:  ['src/main/resources/static/js/dist/*.js', '!src/main/resources/static/js/dist/*.min.js'],
		 		css: ['src/main/resources/static/css/dist/*.css', '!src/main/resources/static/css/dist/*.min.css']
			},
	        concat: {
	            "options": { },
	            build_boot_js: {
	                src: [ "src/main/resources/static/js/src/jquery/jquery-3.3.1.min.js",  
	                	    "src/main/resources/static/js/src/bootstrap/bootstrap.min.js" 
	                		 ],
	                dest: "src/main/resources/static/js/dist/boot.js"
	            },
	            build_setting_js: {
	                src: [ "src/main/resources/static/js/src/jquery/jquery-3.3.1.min.js",  
	                	    "src/main/resources/static/js/src/bootstrap/bootstrap.min.js",
	                	    "src/main/resources/static/js/src/plugins/metisMenu/metisMenu.min.js",
	                	    "src/main/resources/static/js/src/plugins/slimscroll/jquery.slimscroll.min.js",
	                	    "src/main/resources/static/js/src/plugins/pace/pace.min.js",
	                	    "src/main/resources/static/js/src/plugins/DataTables/datatables.min.js",
	                	    "src/main/resources/static/js/src/inspinia/inspinia.js",
	                	    
	                	    "src/main/resources/static/js/src/app/core.js",
	                	    "src/main/resources/static/js/src/app/users.js",
	                	    "src/main/resources/static/js/src/app/config.js",
	                	    "src/main/resources/static/js/src/app/setting.js"
	                		 ],
	                dest: "src/main/resources/static/js/dist/setting.js"
	            },
	            build_start_js: {
	                src: [ "src/main/resources/static/js/src/jquery/jquery-3.3.1.min.js",  
	                	    "src/main/resources/static/js/src/bootstrap/bootstrap.min.js",
	                	    "src/main/resources/static/js/src/plugins/metisMenu/metisMenu.min.js",
	                	    "src/main/resources/static/js/src/plugins/slimscroll/jquery.slimscroll.min.js",
	                	    "src/main/resources/static/js/src/plugins/pace/pace.min.js",
	                	    "src/main/resources/static/js/src/plugins/DataTables/datatables.min.js",
	                	    "src/main/resources/static/js/src/inspinia/inspinia.js",
	                	    
	                	    "src/main/resources/static/js/src/app/core.js",
	                	    "src/main/resources/static/js/src/app/start.js"
	                		 ],
	                dest: "src/main/resources/static/js/dist/start.js"
	            },
	            build_units_js: {
	                src: [ "src/main/resources/static/js/src/jquery/jquery-3.3.1.min.js",  
	                	    "src/main/resources/static/js/src/bootstrap/bootstrap.min.js",
	                	    "src/main/resources/static/js/src/plugins/metisMenu/metisMenu.min.js",
	                	    "src/main/resources/static/js/src/plugins/slimscroll/jquery.slimscroll.min.js",
	                	    "src/main/resources/static/js/src/plugins/pace/pace.min.js",
	                	    "src/main/resources/static/js/src/plugins/DataTables/datatables.min.js",
	                	    "src/main/resources/static/js/src/inspinia/inspinia.js",
	                	    
	                	    "src/main/resources/static/js/src/app/core.js",
	                	    "src/main/resources/static/js/src/app/units.js"
	                		 ],
	                dest: "src/main/resources/static/js/dist/units.js"
	            }
	        },
	        uglify: {
	        	options: {
	        		mangle: false
	        		
	        	},
	        js: {
	          files: {
	        	  "src/main/resources/static/js/dist/boot.min.js" : ["src/main/resources/static/js/dist/boot.js"],
	        	  "src/main/resources/static/js/dist/setting.min.js" : ["src/main/resources/static/js/dist/setting.js"],
	        	  "src/main/resources/static/js/dist/start.min.js" : ["src/main/resources/static/js/dist/start.js"]
	        	
	        	 // "src/main/resources/static/js/dist/units.min.js" : ["src/main/resources/static/js/dist/units.js"]
	            }
	         }
	       }
	 
	    });
	    grunt.loadNpmTasks("grunt-contrib-concat");
	    grunt.loadNpmTasks('grunt-contrib-uglify');
	    grunt.loadNpmTasks('grunt-concat-css');
	    grunt.loadNpmTasks('grunt-contrib-cssmin');
	    grunt.loadNpmTasks('grunt-contrib-clean');
	    grunt.registerTask("default", ["concat:build_boot_js", "concat:build_setting_js", "concat:build_start_js", "concat:build_units_js", "uglify", "concat_css", "cssmin", "clean"]);
	    grunt.registerTask("js1", ["concat:build_boot_js", "concat:build_setting_js", "concat:build_start_js", "concat:build_units_js", "uglify",  "clean"]);
	    grunt.registerTask("start", ["concat:build_start_js", "uglify",  "clean"]);
	    //grunt.registerTask("units", ["concat:build_units_js", "uglify",  "clean"]);
	    grunt.registerTask("units", ["concat:build_units_js"]);

};
