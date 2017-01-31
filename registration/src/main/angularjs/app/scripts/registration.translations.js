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
			FORM_CHECKBOX_COUNSELLING:				'Mitarbeit B-Team Seelsorge',
			
			FORM_PLACEHOLDER_CHURCH:					'Eure Gemeinde...',
			FORM_PLACEHOLDER_GROUPTYPE:					'Eure Gruppenart...',
			FORM_PLACEHOLDER_DISTRICT:					'Euer Bundeskreis...',
			FORM_PLACEHOLDER_LEADER_FIRSTNAME:			'Dein Vorname...',
			FORM_PLACEHOLDER_LEADER_LASTNAME:			'Dein Nachname...',
			FORM_PLACEHOLDER_LEADER_STREET:				'Deine Straße und Hausnummer...',
			FORM_PLACEHOLDER_LEADER_ZIPCODE:			'Deine PLZ...',
			FORM_PLACEHOLDER_LEADER_CITY:				'Dein Ort...',
			FORM_PLACEHOLDER_LEADER_ADDRESSEXTRA:		'Dein Adresszusatz...',
			FORM_PLACEHOLDER_LEADER_MOBILE:				'Deine Handynummer...',
			FORM_PLACEHOLDER_LEADER_EMAIL:				'Deine E-Mailadresse...',
			FORM_PLACEHOLDER_LEADER_PRICE:				'Deine Preisgruppe...',
			FORM_PLACEHOLDER_LEADER_MEDICALHINTS:		'Hinweise zu Allergien, chronischen Erkrankungen und Medikamenten...',
			FORM_PLACEHOLDER_LEADER_BIRTHDAY:			'Dein Geburtsdatum...',
			FORM_PLACEHOLDER_PARTICIPANT_FIRSTNAME:		'Vorname...',
			FORM_PLACEHOLDER_PARTICIPANT_LASTNAME:		'Nachname...',
			FORM_PLACEHOLDER_PARTICIPANT_MOBILE:		'Handynummer...',
			FORM_PLACEHOLDER_PARTICIPANT_EMAIL:			'E-Mailadresse...',
			FORM_PLACEHOLDER_PARTICIPANT_PRICE:			'Preisgruppe...',
			FORM_PLACEHOLDER_PARTICIPANT_MEDICALHINTS:	'Hinweise zu Allergien, chronischen Erkrankungen und Medikamenten...',
			FORM_PLACEHOLDER_PARTICIPANT_BIRTHDAY:		'Geburtsdatum...',
		
			BUTTON_PROCEED:							'Weiter',
			BUTTON_BACK:							'Zurück',
			BUTTON_ADD_PARTICIPANT:					'Teilnehmer hinzufügen',
			BUTTON_CANCEL:							'Abbrechen',
			BUTTON_SAVE:							'Speichern',
			
			MESSAGE_VALIDATION_REQUIRED:			'Eingabe von "{{field}}" ist verpflichtend',
			MESSAGE_VALIDATION_TOO_LONG:			'Eingegebener Wert für "{{field}}" ist zu lang',
			MESSAGE_VALIDATION_INVALID_DATE:		'{{field}} ist kein gültiges Datum',
			MESSAGE_NO_PARTICIPANTS:					'Keine Teilnehmer eingegeben',
			
			HINT_GROUP_DISTRICT:					'Diese Eingabe nutzen wir zur Berechnung unserer Zuschüsse.',
			HINT_PERSON_MOBILE:						'Diese Handynummer dient uns im Bedarfsfall zur Kontaktaufnahme auf dem Festivalgelände. Die Handynumer wird während des Festivalzeitraums ggf. für den Versand von SMS genutzt.',
			HINT_PERSON_EMAIL:						'Diese E-Mail Adresse dient uns zur Kontaktaufnahme im Vorfeld des Festivals.',
			HINT_PERSON_PRICE:						'Bitte sei bei der Eingabe der Preisgruppe ehrlich, der Teilnehmerbeitrag ist so günstig wie möglich kalkuliert!<br/><strong>Nichtverdiener:</strong> Schüler, Studenten, Hausfrauen, ...<br/><strong>Geringverdiener:</strong> Auszubildende, Bufdis, Arbeitslose, ...<br/><strong>Vollverdiener:</strong> Alle anderen...',
			HINT_LEADER_FOODALLERGY:				'Wenn du dieses Feld aktivierst, setzen wir uns mit dir in Verbindung, um deine Lebensmittelallergien abzuklären.',
			HINT_PARTICIPANT_FOODALLERGY:			'Wenn du dieses Feld aktivierst, setzen wir uns mit dem Teilnehmer in Verbindung, um die Lebensmittelallergien abzuklären.',
			HINT_PERSON_MEDICALHINTS:				'Bitte alle bekannten Allergien und chronische Erkrankungen aufzählen, ebenso alle Medikamente, die regelmäßig eingenommen werden müssen. Diese Informationen werden an den Leiter der Ersten Hilfe weitergegeben, um auf dem Festivalgelände eine optimale medizinische Versorgung gewährleisten zu können.',
			
			TABLE_HEADER_FIRSTNAME:					'Vorname',
			TABLE_HEADER_LASTNAME:					'Nachname',
			
			TITLE_EDIT:								'Bearbeiten',
			TITLE_DELETE:							'Entfernen',
		});
		
		$translateProvider.preferredLanguage('de');
	}]);
