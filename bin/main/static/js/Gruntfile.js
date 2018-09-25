/*global module:false*/
module.exports = function(grunt) {

	 grunt.initConfig({
		 	concat_css: {
			    options: {},
			    all: {
			        src: ['../css/src/bootstrap.min.css', 
			        	  '../css/src/bootstrap.min.css.map',
			              '../css/src/animate.css',
			        	  '../css/src/style.css'
			              ],
			              
			        dest: "../css/dist/asterisk-control.css"
			      },
			  },
			cssmin: {
				  target: {
				    files: [{
				      expand: true,
				      cwd: '../css/dist',
				      src: ['*.css', '!*.min.css'],
				      dest: '../css/dist',
				      ext: '.min.css'
				    }]
				  }
				},  
		 	clean: {
		 		options: { force: true },
		 		js: ['dist/*.js', '!dist/*.min.js'],
		 		css: ['../css/dist/*.css', '!../css/dist/*.min.css']
			},
	        concat: {
	            "options": { },
	            build_1: {
	                src: [ "src/jquery/jquery-3.1.1.min.js",  
	                	    "src/bootstrap/bootstrap.min.js" 
	                		 ],
	                dest: "dist/asterisk-control-login.js"
	            },
	            build_2: {
	                src: [ "src/jquery/jquery-3.1.1.min.js",  
	                	    "src/bootstrap/bootstrap.min.js",
	                	    "src/inspinia/inspinia.js",
	                		 ],
	                dest: "dist/asterisk-control-dashboard.js"
	            },

	        },
	        uglify: {
	        	options: {
	        		mangle: false
	        		
	        	},
	        js: {
	          files: {
	        	
	        	  "dist/asterisk-control-login.min.js" : ["dist/asterisk-control-login.js"],
	        	  "dist/asterisk-control-dashboard.min.js" : ["dist/asterisk-control-dashboard.js"]
	        	  
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
