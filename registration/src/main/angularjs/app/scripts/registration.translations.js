'use strict';

var app = angular.module('registration')
	.config(['$translateProvider', function($translateProvider) {
		$translateProvider.translations('de', {
			HEADLINE_HINTS_START:					'Start',
			HEADLINE_GROUP_INPUT:					'Gruppe',
			HEADLINE_LEADER_INPUT:					'Leiter',
			HEADLINE_PARTICIPANTS_INPUT:			'Teilnehmer',
			HEADLINE_CONFIRM:						'Abschluss',
			
			FORM_LABEL_CHURCH:						'Gemeinde',
			FORM_LABEL_GROUPTYPE:					'Gruppenart',
			FORM_LABEL_DISTRICT:					'FeG Bundeskreis',
			
			FORM_PLACEHOLDER_CHURCH:				'Eure Gemeinde...',
			FORM_PLACEHOLDER_GROUPTYPE:				'Eure Gruppenart...',
			FORM_PLACEHOLDER_DISTRICT:				'Euer Bundeskreis...',
			
			BUTTON_PROCEED:							'Weiter',
			
			MESSAGE_VALIDATION_REQUIRED:			'Eingabe von "{{field}}" ist verpflichtend',
			MESSAGE_VALIDATION_TOO_LONG:			'Eingegebener Wert für "{{field}}" ist zu lang',
		});
		
		$translateProvider.preferredLanguage('de');
	}]);
