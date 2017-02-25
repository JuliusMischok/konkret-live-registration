'use strict';

var app = angular.module('registration')
	.controller('RegistrationController', ['$scope', function($scope) {
		$scope.group = {};
		$scope.leader = {};
		$scope.participants = [];
		
		$scope.initPopovers = function() {
			angular.element('[data-toggle="popover"]').popover();
		};

		$scope.tabs = {
			start: 0,
			group: 1,
			leader: 2,
			participants: 3,
			confirm: 4
		};
		
		$scope.activeTab = $scope.tabs.start;
		
		$scope.groupform = undefined;
		$scope.leaderform = undefined;
		
		$scope.setGroupform = function(groupform) {
			$scope.groupform = groupform;
		};

		$scope.setLeaderform = function(leaderform) {
			$scope.leaderform = leaderform;
		};
		
		$scope.setActiveTab = function(index) {
			$scope.activeTab = index;
		};
		
		$scope.groupReady = function() {
			return typeof $scope.groupform !== typeof undefined && $scope.groupform.$valid;
		};
		
		$scope.leaderReady = function() {
			return $scope.groupReady() && typeof $scope.leaderform !== typeof undefined && $scope.leaderform.$valid;
		};
		
		$scope.participantsReady = function() {
			return $scope.leaderReady() && $scope.participants.length > 0;
		};
	}])
	.controller('StartHintsController', ['$scope', function($scope) {
		$scope.proceed = function() {
			$scope.setActiveTab($scope.tabs.group);
		};
	}])
	
	.controller('GroupInputController', ['$scope', 'grouptypeService', 'districtService', function($scope, grouptypeService, districtService) {
		$scope.grouptypes = grouptypeService.grouptypes();
		$scope.districts = districtService.districts();
		
		$scope.initPopovers();
		
		$scope.proceed = function() {
			$scope.setGroupform($scope.groupform);
			$scope.setActiveTab($scope.tabs.leader);
		};
	}])
	
	.controller('LeaderInputController', ['$scope', 'priceService', function($scope, priceService) {
		$scope.prices = priceService.prices();
		$scope.birthdaypopup = {opened: false};

		$scope.initPopovers();
		
		var maxDateInclusive = new Date(1999, 5, 2); 
		
		$scope.validateBirthday = function() {
			var currentDate = typeof $scope.leader.birthday === typeof undefined ? undefined : Date.parse($scope.leader.birthday);
			
			$scope.leaderform.birthday.$setValidity("max", currentDate <= maxDateInclusive);
		};
		
		$scope.dateOptions = {
			formatYear : 'yyyy',
			startingDay : 1,
			maxDate : maxDateInclusive,
			initDate : maxDateInclusive
		};
		
		$scope.openBirthdayPopup = function() {
			$scope.birthdaypopup.opened = true;
		};
		
		$scope.proceed = function() {
			$scope.setLeaderform($scope.leaderform);
			$scope.setActiveTab($scope.tabs.participants);
		};
		
		$scope.back = function() {
			$scope.setActiveTab($scope.tabs.group);
		};
	}])
	
	.controller('ParticipantInputController', ['$scope', 'priceService', '$uibModal', function($scope, priceService, $uibModal) {
		$scope.prices = priceService.prices();
		$scope.birthdaypopup = {opened: false};
		$scope.showList = true;
		$scope.participant = {};
		$scope.cancelParticipant = undefined;
		
		$scope.initPopovers();
		
		$scope.back = function() {
			$scope.setActiveTab($scope.tabs.leader);
		};
		
		$scope.proceed = function() {
			$scope.setActiveTab($scope.tabs.confirm);
		};
		
		$scope.dateOptions = {
			formatYear : 'yyyy',
			startingDay : 1
		};
		
		$scope.openBirthdayPopup = function() {
			$scope.birthdaypopup.opened = true;
		};
		
		var showParticipantForm = function(participant) {
			$scope.participant = (typeof participant === typeof undefined) ? {} : participant;
			$scope.cancelParticipant = angular.copy(participant);
			
			$scope.showList = false;
			$scope.participantform.$setPristine();
			$scope.participantform.$setUntouched();
		};
		
		var showList = function(participantToBeAdded) {
			if (typeof participantToBeAdded !== typeof undefined) {
				$scope.participants.push(angular.copy(participantToBeAdded));
			}
			$scope.participant = {};
			$scope.showList = true;
			$scope.cancelParticipant = undefined;
		};
		
		$scope.showAddParticipantForm = function() {
			showParticipantForm(undefined);
		};
		
		$scope.saveParticipant = function() {
			showList(this.participant);
		};
		
		$scope.cancelEdit = function() {
			showList($scope.cancelParticipant);
		};
		
		$scope.editParticipant = function(index) {
			var participantToEdit = angular.copy($scope.participants[index]);
			$scope.participants.splice(index, 1);
			
			showParticipantForm(participantToEdit);
		};
		
		var confirm = function(yesHandler, noHandler) {
			var modalInstance = $uibModal.open({
				animation : true,
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'views/confirmDeleteParticipantModal.html',
				controller : 'ConfirmModalInstanceCtrl',
				size : 'sm',
			});

			modalInstance.result.then(yesHandler, noHandler);
		};
		
		var getDeleteParticipantWithoutConfirm = function(index) {
			return function() {
				$scope.participants.splice(index, 1);
			};
		};
		
		var doNothing = function() {};
		
		$scope.deleteParticipant = function(index) {
			confirm(getDeleteParticipantWithoutConfirm(index), doNothing);
		};
	}])
	.controller('ConfirmModalInstanceCtrl', ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
			$scope.yes = function() {
				$uibModalInstance.close();
			};

			$scope.no = function() {
				$uibModalInstance.dismiss('cancel');
			};
	}])
	.controller('ConfirmController', ['$scope', 'priceService', 'grouptypeService', '$translate', '$window', function($scope, priceService, grouptypeService, $translate, $window) {
		$scope.initPopovers();
		$scope.hintPrivacyVisible = false;
		$scope.hintSupervisionVisible = false;
		$scope.privacyConfirmed = false;
		$scope.supervisionConfirmed = false;
		
		$scope.back = function() {
			$scope.setActiveTab($scope.tabs.participants);
		};
		
		$scope.register = function() {
			$window.alert("Coming soon...");
		};
				
		$scope.getPrice = function(person) {
			return priceService.getPrice(person.price);
		};
		
		$scope.getWholeprice = function() {
			var add = function(a, b) {return a + b;};
			
			var sum = $scope.getPersons()
					.map(function(person) {return $scope.getPrice(person);})
					.reduce(add)
					;
			
			return sum;
		};
		
		$scope.getTransferUsagePlaceholder = function() {
			return grouptypeService.getGrouptype($scope.group.type) + ' ' + $scope.group.church;
		};
		
		$scope.getPersons = function() {
			return [].concat($scope.participants).concat($scope.leader);
		};
	
		$scope.toggleHintPrivacy = function() {
			$scope.hintPrivacyVisible = !$scope.hintPrivacyVisible;
		};
		
		$scope.toggleHintSupervision = function() {
			$scope.hintSupervisionVisible = !$scope.hintSupervisionVisible;
		};
		
		$scope.registrationPossible = function() {
			return $scope.privacyConfirmed && $scope.supervisionConfirmed;
		};
	}])
	;
				