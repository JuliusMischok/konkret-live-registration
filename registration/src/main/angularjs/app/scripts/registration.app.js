'use strict';

var app = angular.module('registration', ['ngResource', 'ui.bootstrap', 'ngMessages', 'pascalprecht.translate'])
				.value('backendServiceUrl', undefined)	// Parameter 'konkretlive.registration.backendServiceUrl' from application.properties or command line. Always ends with "/"
				;
				