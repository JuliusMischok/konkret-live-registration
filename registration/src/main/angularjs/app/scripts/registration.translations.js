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
			FORM_LABEL_FIRSTNAME:					'Vorname',
			FORM_LABEL_LASTNAME:					'Nachname',
			FORM_LABEL_STREET:						'Straße Nr.',
			FORM_LABEL_ZIPCODE:						'PLZ',
			FORM_LABEL_CITY:						'Ort',
			FORM_LABEL_ADDRESSEXTRA:				'Adresszusatz',
			FORM_LABEL_MOBILE:						'Handy',
			FORM_LABEL_EMAIL:						'E-Mail',
			FORM_LABEL_PRICE:						'Preisgruppe',
			FORM_LABEL_MEDICALHINTS:				'Medizinische Hinweise',
			FORM_LABEL_BIRTHDAY:					'Geburtsdatum',
			
			FORM_CHECKBOX_VEGETARIAN:				'Vegetarier',
			FORM_CHECKBOX_FOODALLERGY:				'Lebensmittelallergien',
			FORM_CHECKBOX_COUNSELLING:				'Mitarbeite B-Team Seelsorge',
			
			FORM_PLACEHOLDER_CHURCH:				'Eure Gemeinde...',
			FORM_PLACEHOLDER_GROUPTYPE:				'Eure Gruppenart...',
			FORM_PLACEHOLDER_DISTRICT:				'Euer Bundeskreis...',
			FORM_PLACEHOLDER_LEADER_FIRSTNAME:		'Dein Vorname...',
			FORM_PLACEHOLDER_LEADER_LASTNAME:		'Dein Nachname...',
			FORM_PLACEHOLDER_LEADER_STREET:			'Deine Straße und Hausnummer...',
			FORM_PLACEHOLDER_LEADER_ZIPCODE:		'Deine PLZ...',
			FORM_PLACEHOLDER_LEADER_CITY:			'Dein Ort...',
			FORM_PLACEHOLDER_LEADER_ADDRESSEXTRA:	'Dein Adresszusatz...',
			FORM_PLACEHOLDER_LEADER_MOBILE:			'Deine Handynummer...',
			FORM_PLACEHOLDER_LEADER_EMAIL:			'Deine E-Mailadresse...',
			FORM_PLACEHOLDER_LEADER_PRICE:			'Deine Preisgruppe...',
			FORM_PLACEHOLDER_LEADER_MEDICALHINTS:	'Hinweise zu Allergien, chronischen Erkrankungen und Medikamenten...',
			FORM_PLACEHOLDER_LEADER_BIRTHDAY:		'Dein Geburtsdatum...',
			
			BUTTON_PROCEED:							'Weiter',
			BUTTON_BACK:							'Zurück',
			
			MESSAGE_VALIDATION_REQUIRED:			'Eingabe von "{{field}}" ist verpflichtend',
			MESSAGE_VALIDATION_TOO_LONG:			'Eingegebener Wert für "{{field}}" ist zu lang',
			MESSAGE_VALIDATION_INVALID_DATE:		'{{field}} ist kein gültiges Datum',
		});
		
		$translateProvider.preferredLanguage('de');
	}]);
