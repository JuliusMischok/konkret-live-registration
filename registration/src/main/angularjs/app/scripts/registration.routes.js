'use strict';

var app = angular.module('registration')
	.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
		$urlRouterProvider.otherwise('/');
		
		$stateProvider
				.state('group', {
					url: '/',
					templateUrl: 'views/group.html',
					controller: 'GroupRegistrationController'
				})
				.state('staff', {
					url: '/staff',
					templateUrl: 'views/staff.html',
					controller: 'StaffRegistrationController'
				})
				;
	}])
	;
