'use strict';

var app = angular.module('registration')
	.controller('PageController', ['$scope', function($scope) {
		$scope.alerts = [];
		
		$scope.initPopovers = function() {
			angular.element('[data-toggle="popover"]').popover();
		};

		$scope.addAlert = function(type, title, detail) {
			var alert = {title: title, detail: detail, type: type, timeout: type === 'danger' ? 10000 : 5000};
			$scope.alerts.push(alert);
			
			angular.element('#sf-message-container').css('display', 'block');
		};

		$scope.closeAlert = function(index) {
			$scope.alerts.splice(index, 1);
		};
		
		$scope.closeAllAlerts = function() {
			$scope.alerts = [];
		};
	}])
	.controller('GroupRegistrationController', ['$scope', function($scope) {
		$scope.group = {};
		$scope.leader = {};
		$scope.participants = [];
		
		$scope.registrationPerformed = false;
		
		$scope.setRegistrationPerformed = function(state) {
			$scope.registrationPerformed = state;
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
	.controller('ConfirmController', ['$scope', 'priceService', 'grouptypeService', '$translate', '$window', 'registrationService', function($scope, priceService, grouptypeService, $translate, $window, registrationService) {
		$scope.initPopovers();
		$scope.hintPrivacyVisible = false;
		$scope.hintSupervisionVisible = false;
		$scope.privacyConfirmed = false;
		$scope.supervisionConfirmed = false;
		
		$scope.registrationPending = false;
		
		$scope.registration = {
			group: $scope.group,
			leader: $scope.leader,
			participants: $scope.participants
		};
		
		$scope.back = function() {
			$scope.setActiveTab($scope.tabs.participants);
		};
		
		var registrationSuccessHandler = function(response) {
			$scope.addAlert('success', $translate.instant('TITLE_REGISTRATION_SUCCESS'), $translate.instant('DETAIL_REGISTRATION_SUCCESS'));
			
			$scope.registrationPending = false;
			$scope.setRegistrationPerformed(true);
		};
		
		var registrationFailureHandler = function(response) {
			console.log('registration failed: ', response);
			console.log(typeof $scope.addAlert);
			
			if (response.status === 520) {
				$scope.addAlert('danger', $translate.instant('TITLE_MAIL_NOT_SENT'), $translate.instant('DETAIL_MAIL_NOT_SENT'));
			} else if (response.status === 400) {
				$scope.addAlert('danger', $translate.instant('TITLE_INVALID_DATA'), $translate.instant('DETAIL_INVALID_DATA'));
			} else if (response.status === 404) {
				$scope.addAlert('danger', $translate.instant('TITLE_BACKEND_UNAVAILABLE'), $translate.instant('DETAIL_BACKEND_UNAVAILABLE'));
			} else {
				$scope.addAlert('danger', $translate.instant('TITLE_UNKNOWN_ERROR'), $translate.instant('DETAIL_UNKNOWN_ERROR'));
			}
			
			$scope.registrationPending = false;
		};
		
		$scope.register = function() {
			$scope.registrationPending = true;
			
			registrationService.registration().save($scope.registration)
					.$promise.then(registrationSuccessHandler, registrationFailureHandler);
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
	.controller('StaffRegistrationController', ['$scope', function($scope) {
		$scope.staff = {
			arrivalconfirmed: false,
			departureconfirmed: false,
			admission: 35.00
		};
		
		$scope.registrationPerformed = false;
		
		$scope.setRegistrationPerformed = function(state) {
			$scope.registrationPerformed = state;
		};
		
		$scope.tabs = {
			start: 0,
			person: 1,
			confirm: 2
		};
		
		$scope.activeTab = $scope.tabs.start;
		
		$scope.personform = undefined;
		
		$scope.setPersonform = function(personform) {
			$scope.personform = personform;
		};

		$scope.setActiveTab = function(index) {
			$scope.activeTab = index;
		};
		
		$scope.personReady = function() {
			return typeof $scope.personform !== typeof undefined && $scope.personform.$valid && $scope.staff.arrivalconfirmed && $scope.staff.departureconfirmed;
		};
		
		$scope.showPopover = function($event) {
			$event.stopPropagation();
		};
	}])
	.controller('StaffStartHintsController', ['$scope', function($scope) {
		console.log('Staff start');
		
		$scope.proceed = function() {
			$scope.setActiveTab($scope.tabs.person);
		};
	}])
	.controller('StaffPersonInputController', ['$scope', 'housingService', 'ouService', 'districtService', function($scope, housingService, ouService, districtService) {
		console.log('Staff person');
		
		$scope.birthdaypopup = {opened: false};
		$scope.arrivalpopup = {opened: false};
		$scope.departurepopup = {opened: false};
		
		$scope.arrivalMax = new Date(2017, 5, 1, 8, 0, 0);
		$scope.departureMin = new Date(2017, 5, 5, 12, 0, 0);
		
		$scope.housings = housingService.housings();
		$scope.ous = ouService.ous();
		$scope.districts = districtService.districts();
		
		$scope.initPopovers();
		
		$scope.openBirthdayPopup = function() {
			$scope.birthdaypopup.opened = true;
		};
		
		$scope.openArrivalPopup = function() {
			$scope.arrivalpopup.opened = true;
		};
		
		$scope.openDeparturePopup = function() {
			$scope.departurepopup.opened = true;
		};
		
		$scope.dateOptionsBirthday = {
			formatYear : 'yyyy',
			startingDay : 1,
			maxDate : new Date(),
			initDate : new Date()
		};
		
		$scope.dateOptionsArrival = {
				formatYear : 'yyyy',
				startingDay : 1,
				minDate : new Date(2017, 4, 30, 0, 0, 0),
				maxDate : new Date(2017, 5, 6, 23, 59, 59),
				initDate : new Date(2017, 4, 31, 12, 0, 0)
		};
		
		$scope.validateArrival = function() {
			$scope.staff.arrivalconfirmed = ($scope.staff.arrival < $scope.arrivalMax);
		};
		
		$scope.dateOptionsDeparture = {
				formatYear : 'yyyy',
				startingDay : 1,
				minDate : new Date(2017, 4, 30, 0, 0, 0),
				maxDate : new Date(2017, 5, 6, 23, 59, 59),
				initDate : new Date(2017, 4, 31, 12, 0, 0)
		};
		
		$scope.validateDeparture = function() {
			$scope.staff.departureconfirmed = ($scope.staff.departure > $scope.departureMin);
		};
		
		$scope.proceed = function() {
			$scope.setPersonform($scope.staffform);
			$scope.setActiveTab($scope.tabs.confirm);
		};
	}])
	.controller('StaffConfirmController', ['$scope', '$translate', 'registrationService', function($scope, $translate, registrationService) {
		$scope.privacyConfirmed = false;
		$scope.hintPrivacyVisible = false;
		$scope.registrationPending = false;
		
		$scope.toggleHintPrivacy = function($event) {
			$event.stopPropagation();
			$scope.hintPrivacyVisible = !$scope.hintPrivacyVisible;
		};
		
		$scope.back = function() {
			$scope.setActiveTab($scope.tabs.person);
		};
		
		var registrationSuccessHandler = function(response) {
			$scope.addAlert('success', $translate.instant('TITLE_REGISTRATION_SUCCESS'), $translate.instant('DETAIL_REGISTRATION_SUCCESS'));
			
			$scope.registrationPending = false;
			$scope.setRegistrationPerformed(true);
		};
		
		var registrationFailureHandler = function(response) {
			if (response.status === 520) {
				$scope.addAlert('danger', $translate.instant('TITLE_MAIL_NOT_SENT'), $translate.instant('DETAIL_MAIL_NOT_SENT'));
			} else if (response.status === 400) {
				$scope.addAlert('danger', $translate.instant('TITLE_INVALID_DATA'), $translate.instant('DETAIL_INVALID_DATA'));
			} else if (response.status === 404) {
				$scope.addAlert('danger', $translate.instant('TITLE_BACKEND_UNAVAILABLE'), $translate.instant('DETAIL_BACKEND_UNAVAILABLE'));
			} else {
				$scope.addAlert('danger', $translate.instant('TITLE_UNKNOWN_ERROR'), $translate.instant('DETAIL_UNKNOWN_ERROR'));
			}
			
			$scope.registrationPending = false;
		};
		
		$scope.register = function() {
			$scope.staff.price = $scope.staff.admission;
			
			$scope.registrationPending = true;
			
			registrationService.staffregistration().save($scope.staff)
				.$promise.then(registrationSuccessHandler, registrationFailureHandler);
		};
		
		$scope.showAdmissionHints = function() {
			return !isNaN(parseFloat($scope.staff.admission)) && isFinite($scope.staff.admission) && $scope.staff.admission > 0;
		};
		
		$scope.getAdmission = function() {
			return $scope.staff.admission;
		};
		
		$scope.getTransferUsageDonation = function() {
			var address = $scope.staff.street +
						((typeof $scope.staff.addressextra !== typeof undefined && $scope.staff.addressextra.length > 0) ? ' (' + $scope.staff.addressextra + ')' : '') + ', ' +
						$scope.staff.zipcode + ' ' + $scope.staff.city; 
			return $scope.staff.firstname + ' ' + $scope.staff.lastname + ', ' + address;
		};
		
		$scope.getTransferUsageAdmission = function() {
			return $scope.staff.firstname + ' ' + $scope.staff.lastname;
		};
		
		$scope.registrationPossible = function() {
			return $scope.privacyConfirmed;
		};
	}])
	;
				