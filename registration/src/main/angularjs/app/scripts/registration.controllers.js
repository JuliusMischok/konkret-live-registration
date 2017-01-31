'use strict';

var app = angular.module('registration')
	.controller('RegistrationController', ['$scope', function($scope) {
		$scope.group = {};
		$scope.leader = {};
		$scope.participants = [];
		
		$scope.initPopovers = function() {
			angular.element('[data-toggle="popover"]').popover();
		};
	}])

	.controller('GroupInputController', ['$scope', 'grouptypeService', 'districtService', function($scope, grouptypeService, districtService) {
		$scope.grouptypes = grouptypeService.grouptypes();
		$scope.districts = districtService.districts();
		
		$scope.initPopovers();
	}])
	
	.controller('LeaderInputController', ['$scope', 'priceService', function($scope, priceService) {
		$scope.prices = priceService.prices();
		$scope.birthdaypopup = {opened: false};

		$scope.initPopovers();
		
		$scope.dateOptions = {
			formatYear : 'yyyy',
			startingDay : 1
		};
		
		$scope.openBirthdayPopup = function() {
			$scope.birthdaypopup.opened = true;
		};
	}])
	
	.controller('ParticipantInputController', ['$scope', 'priceService', function($scope, priceService) {
		$scope.prices = priceService.prices();
		$scope.birthdaypopup = {opened: false};
		$scope.showList = true;
		$scope.participant = {};
		
		$scope.initPopovers();
		
		$scope.dateOptions = {
			formatYear : 'yyyy',
			startingDay : 1
		};
		
		$scope.openBirthdayPopup = function() {
			$scope.birthdaypopup.opened = true;
		};
		
		$scope.showAddParticipantForm = function() {
			$scope.participant = {};
			$scope.showList = false;
			$scope.participantform.$setPristine();
			$scope.participantform.$setUntouched();
		};
		
		$scope.backToList = function() {
			$scope.participant = {};
			$scope.showList = true;
		};
		
		$scope.saveParticipant = function() {
			$scope.participants.push(angular.copy($scope.participant));
			$scope.participant = {};
			$scope.showList = true;
		};
	}])
	;
				