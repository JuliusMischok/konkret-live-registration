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
	.controller('ConfirmController', ['$scope', 'priceService', function($scope, priceService) {
		$scope.initPopovers();
		$scope.hintPrivacyVisible = false;
		$scope.hintSupervisionVisible = false;
		$scope.privacyConfirmed = false;
		$scope.supervisionConfirmed = false;
				
		$scope.getPrice = function(person) {
			var filteredPrices = priceService.prices().filter(function(price) {console.log('comparing', price.id, person.price, typeof price.id, typeof person.price); return price.id === person.price;});
			
			if (filteredPrices.length === 1) {
				return filteredPrices[0].price;
			} else {
				return undefined;
			}
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
				