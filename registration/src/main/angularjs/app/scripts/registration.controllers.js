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
	
	.controller('ParticipantInputController', ['$scope', 'priceService', '$uibModal', function($scope, priceService, $uibModal) {
		$scope.prices = priceService.prices();
		$scope.birthdaypopup = {opened: false};
		$scope.showList = true;
		$scope.participant = {};
		$scope.cancelParticipant = undefined;
		
		$scope.initPopovers();
		
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
	.controller('ConfirmController', ['$scope', 'priceService', 'grouptypeService', '$translate', function($scope, priceService, grouptypeService, $translate) {
		$scope.initPopovers();
		$scope.hintPrivacyVisible = false;
		$scope.hintSupervisionVisible = false;
		$scope.privacyConfirmed = false;
		$scope.supervisionConfirmed = false;
				
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
				