'use strict';

module.exports = function(grunt) {
	// Time how long tasks take. Can help when optimizing build times
	require('time-grunt')(grunt);

	// Automatically load required Grunt tasks
	require('jit-grunt')(grunt, {
		useminPrepare : 'grunt-usemin'
	});
	
	var currentworkingdir = '../';
	var distdir = currentworkingdir + '/resources/static';
	var appdir = currentworkingdir + '/angularjs/app';

	// Define the configuration for all the tasks
	grunt.initConfig({
		pkg : grunt.file.readJSON('package.json'),

		clean : {
			options: {
			    force: true
			},
			build : {
				src : [ distdir + '/*/', distdir + '/**/*.html' ]
			}
		},
		// Make sure code styles are up to par and there are no obvious mistakes
		jshint : {
			options : {
				jshintrc : '.jshintrc',
				reporter : require('jshint-stylish')
			},
			all : {
				src : [ 'Gruntfile.js', appdir + '/scripts/{,*/}*.js' ]
			}
		},
		copy : {
			dist : {
				cwd : appdir,
				src : [ '**', '!styles/**/*.css', '!scripts/**/*.js', '!favicon/**' ],
				dest : distdir,
				expand : true
			},
			favicon: {
				cwd : appdir + '/favicon',
				src : [ '*' ],
				dest : distdir,
				expand : true
			},
			fonts : {
				files : [ {
					//for bootstrap fonts
					expand : true,
					dot : true,
					cwd : 'bower_components/bootstrap/dist',
					src : [ 'fonts/*.*' ],
					dest : distdir
				}, {
					//for font-awesome
					expand : true,
					dot : true,
					cwd : 'bower_components/font-awesome',
					src : [ 'fonts/*.*' ],
					dest : distdir
				} ]
			}
		},
		useminPrepare : {
			html : appdir + '/index.html',
			options : {
				dest : distdir
			}
		},
		concat : {
			options : {},
			dist : {}
		},
		uglify : {
			dist : {}
		},
		cssmin : {
			dist : {}
		},
		filerev : {
			options : {
				encoding : 'utf8',
				algorithm : 'md5',
				length : 20
			},
			release : {
				files : [ {
					src : [ distdir + '/scripts/*.js', distdir + '/styles/*.css' ]
				} ]
			}
		},
		usemin : {
			html : [ distdir + '/*.html' ],
			css : [ distdir + '/styles/*.css' ],
			options : {
				assetsDirs : [ distdir, distdir + '/styles' ]
			}
		},
	    watch: {
	        copy: {
	            files: [ appdir + '/**', '!' + appdir + '/**/*.css', '!' + appdir + '/**/*.js', '!' + appdir + '/**/*.html'],
	            tasks: [ 'build' ]
	        },
	        scripts: {
	            files: [ appdir + '/scripts/**/*.js'],
	            tasks:[ 'build']
	        },
	        styles: {
	            files: [ appdir + '/styles/*.css'],
	            tasks:['build']
	        },
	        views: {
	            files: [ appdir + '/views/**/*.html', appdir + '/*.html'],
	            tasks:['build']
	        },
	        livereload: {
	            files: [
	                appdir + '/{,*/}*.html',
	                '.tmp/styles/{,*/}*.css',
	                appdir + '/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}'
	            ]
	      }
	    }
	});

	grunt.registerTask('default', [ 'build' ]);
		
	grunt.registerTask('build', [ 'clean', 'jshint', 'useminPrepare', 'concat', 'cssmin', 'uglify', 'copy', 'filerev', 'usemin' ]);
};
