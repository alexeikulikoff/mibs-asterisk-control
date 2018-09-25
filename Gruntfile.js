/*global module:false*/
module.exports = function(grunt) {

	 grunt.initConfig({
		 	concat_css: {
			    options: {},
			    all: {
			        src: ['src/main/resources/static/css/src/bootstrap.min.css', 
			        	  'src/main/resources/static/css/src/bootstrap.min.css.map',
			              'src/main/resources/static/css/src/animate.css',
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
		 		js: ['dist/*.js', '!dist/*.min.js'],
		 		css: ['src/main/resources/static/css/dist/*.css', '!src/main/resources/static/css/dist/*.min.css']
			},
	        concat: {
	            "options": { },
	            build_1: {
	                src: [ "src/main/resources/static/js/src/jquery/jquery-3.1.1.min.js",  
	                	    "src/main/resources/static/js/src/bootstrap/bootstrap.min.js" 
	                		 ],
	                dest: "src/main/resources/static/js/dist/asterisk-control-login.js"
	            },
	            build_2: {
	                src: [ "src/main/resources/static/js/src/jquery/jquery-3.1.1.min.js",  
	                	    "src/main/resources/static/js/src/bootstrap/bootstrap.min.js",
	                	    "src/main/resources/static/js/src/inspinia/inspinia.js",
	                		 ],
	                dest: "src/main/resources/static/js/dist/asterisk-control-dashboard.js"
	            },

	        },
	        uglify: {
	        	options: {
	        		mangle: false
	        		
	        	},
	        js: {
	          files: {
	        	
	        	  "src/main/resources/static/js/dist/asterisk-control-login.min.js" : ["src/main/resources/static/js/dist/asterisk-control-login.js"],
	        	  "src/main/resources/static/js/dist/asterisk-control-dashboard.min.js" : ["src/main/resources/static/js/dist/asterisk-control-dashboard.js"]
	        	  
	            }
	         }
	       }
	 
	    });
	    grunt.loadNpmTasks("grunt-contrib-concat");
	    grunt.loadNpmTasks('grunt-contrib-uglify');
	    grunt.loadNpmTasks('grunt-concat-css');
	    grunt.loadNpmTasks('grunt-contrib-cssmin');
	    grunt.loadNpmTasks('grunt-contrib-clean');
	    grunt.registerTask("default", ["concat:build_1", "concat:build_2", "uglify", "concat_css", "cssmin", "clean"]);

};
