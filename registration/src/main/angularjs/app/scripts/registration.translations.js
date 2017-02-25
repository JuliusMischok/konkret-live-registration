'use strict';

var app = angular.module('registration')
	.config(['$translateProvider', function($translateProvider) {
		$translateProvider.translations('de', {
			HEADLINE_HINTS_START:					'Start',
			HEADLINE_WELCOME:						'Herzlich Willkommen!',
			HEADLINE_GROUP_INPUT:					'Gruppe',
			HEADLINE_GROUP_DATA:					'Gruppendaten',
			HEADLINE_LEADER_INPUT:					'Leiter',
			HEADLINE_LEADER_DATA:					'Daten des Gruppenleiters',
			HEADLINE_PARTICIPANTS_INPUT:			'Teilnehmer',
			HEADLINE_PARTICIPANTS_DATA:				'Teilnehmer',
			HEADLINE_CONFIRM:						'Abschluss',
			HEADLINE_REGISTERED_PERSONS:			'Eingetragene Personen',
			HEADLINE_PRICE:							'Teilnehmerbeitrag',
			HEADLINE_REGISTER:						'Verbindliche Anmeldung',
			
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
			FORM_CHECKBOX_PRIVACY:					'Ich habe die Hinweise zum Datenschutz gelesen.',
			FORM_CHECKBOX_SUPERVISION:				'Ich habe die Hinweise zur Aufsichtspflicht gelesen.',
			
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
			BUTTON_YES:								'Ja',
			BUTTON_NO:								'Nein',
			BUTTON_REGISTER:						'Verbindlich anmelden',
			
			MESSAGE_VALIDATION_REQUIRED:			'Eingabe von "{{field}}" ist verpflichtend',
			MESSAGE_VALIDATION_INVALID:				'Eingegebener Wert für "{{field}}" ist ungültig',
			MESSAGE_VALIDATION_TOO_LONG:			'Eingegebener Wert für "{{field}}" ist zu lang',
			MESSAGE_VALIDATION_INVALID_DATE:		'{{field}} ist kein gültiges Datum',
			MESSAGE_NO_PARTICIPANTS:				'Keine Teilnehmer eingegeben',
			MESSAGE_VALIDATION_LEADER_FULLAGE:		'Gruppenleiter muss zu Festivalbeginn volljähring sein',
			
			HINT_GROUP_DISTRICT:					'Diese Eingabe nutzen wir zur Berechnung unserer Zuschüsse.',
			HINT_LEADER_MOBILE:						'Diese Handynummer dient uns im Bedarfsfall zur Kontaktaufnahme auf dem Festivalgelände. Die Handynumer wird während des Festivalzeitraums ggf. für den Versand von SMS genutzt.',
			HINT_PARTICIPANT_MOBILE:				'Diese Handynummer nutzen wir im Vorfeld zur Abklärung der Lebensmittelallergien. Die Handynumer wird während des Festivalzeitraums ggf. für den Versand von SMS genutzt.',
			HINT_PERSON_EMAIL:						'Diese E-Mail Adresse dient uns zur Kontaktaufnahme im Vorfeld des Festivals.',
			HINT_PERSON_PRICE:						'Bitte sei bei der Eingabe der Preisgruppe ehrlich, der Teilnehmerbeitrag ist so günstig wie möglich kalkuliert!<br/><strong>Nichtverdiener:</strong> Schüler, Studenten, Hausfrauen, ...<br/><strong>Geringverdiener:</strong> Auszubildende, Bufdis, Arbeitslose, ...<br/><strong>Vollverdiener:</strong> Alle anderen...',
			HINT_LEADER_FOODALLERGY:				'Wenn du dieses Feld aktivierst, setzen wir uns mit dir in Verbindung, um deine Lebensmittelallergien abzuklären.',
			HINT_PARTICIPANT_FOODALLERGY:			'Wenn du dieses Feld aktivierst, setzen wir uns mit dem Teilnehmer in Verbindung, um die Lebensmittelallergien abzuklären.',
			HINT_PERSON_MEDICALHINTS:				'Bitte alle bekannten Allergien und chronische Erkrankungen aufzählen, ebenso alle Medikamente, die regelmäßig eingenommen werden müssen. Diese Informationen werden an den Leiter der Ersten Hilfe weitergegeben, um auf dem Festivalgelände eine optimale medizinische Versorgung gewährleisten zu können.',
			HINT_CONFIRM_PRIVACY:					'Die eingegebenen Daten werden von uns gespeichert und nur für Zwecke der Kreisjugendarbeit der Freien evangelischen Gemeinden Bayern verwendet. Mit einem formlosen Anschreiben an <a href="mailto:office@konkretlive.de">office@konkretlive.de</a> kann der Speicherung der Daten widersprochen werden, diese werden dann nach Ablauf des Festivals gelöscht.<br/>Die auf dem Festivalgelände angefertigten Fotos und Videoaufnahmen werden zur Nachberichterstattung und zu Werbezwecken in Digital- und Printmedien weiterverwendet. Möchte eine angemeldete Person der Veröffentlichung von Fotos und Videoaufnahmen widersprechen, ist dies durch formloses Anschreiben an <a href="mailto:office@konkretlive.de">office@konkretlive.de</a> und Einsendung eines Porträtfotos der entsprechenden Person möglich. Dieser Ausschluss bezieht sich allerdings nur auf die offiziellen Veröffentlichungen des Festivals; gegen eine Veröffentlichung durch FestivalteilnehmerInnen muss persönlich vorgegangen werden.',
			HINT_CONFIRM_SUPERVISION:				'Natürlich gelten auch für euch auf dem Festival die Bestimmungen des Jugendschutzes. Die Erziehungsberechtigten von Minderjährigen übertragen durch die Unterschrift auf dem Anmeldeflyer ihre Aufsichtspflicht an dich als Leiter.<br/>Wichtig: Informiere die Eltern mittels Flyer, für was die Teens sich anmelden. Du gewährst keine Einzelbeaufsichtigung bei Einzelunternehmungen auf dem Festival-Gelände. Vor Ort sind durch uns alle nötigen Sicherheitseinrichtungen gegeben (Erste Hilfe, Ordnungsteam, Telefon). Wir gehen davon aus, dass du deine Verantwortung wahrnimmst! Triff dazu die nötigen Abmachungen mit deinen Leuten, bevor das Festival losgeht.',
			
			TABLE_HEADER_FIRSTNAME:					'Vorname',
			TABLE_HEADER_LASTNAME:					'Nachname',
			TABLE_HEADER_ADDRESS:					'Adresse',
			TABLE_HEADER_MOBILE:					'Handynummer',
			TABLE_HEADER_EMAIL:						'E-Mail',
			TABLE_HEADER_BIRTHDAY:					'Geburtstag',					
			TABLE_HEADER_PRICE:						'Teilnehmerbeitrag',
			TABLE_HEADER_VEGETARIAN:				'Vegetarier',
			TABLE_HEADER_FOOD_ALLERGIES:			'Lebensmittelallergien',
			
			TITLE_EDIT:								'Bearbeiten',
			TITLE_DELETE:							'Entfernen',
			TITLE_CONFIRM:							'Bestätigen',
			
			TEXT_CONFIRM_DELETE_PARTICIPANT:		'Möchtest du den Teilnehmer wirklich löschen? Die Daten können nicht wiederhergestellt werden!',
			TEXT_SHOW_PRIVACY:						'Datenschutzhinweise anzeigen',
			TEXT_HIDE_PRIVACY:						'Ausblenden',
			TEXT_SHOW_SUPERVISION:					'Hinweise zur Aufsichtspflicht anzeigen',
			TEXT_HIDE_SUPERVISION:					'Ausblenden',
			TEXT_PRICE_BANK_TRANSFER:				'Bitte überweise den gesamten Teilnehmerbeitrag in Höhe von <strong>{{wholeprice}}&nbsp;&euro;</strong> auf das untenstehende Konto:<br/><br/><strong>IBAN:</strong>TODO<br/><strong>Verwendungszweck:</strong> Teilnehmerbeitrag {{transferusage}}',
			TEXT_START:								'Schön, dass du mit deiner Gruppe bei konkret live dabei sein willst!<br/>Bitte beachte die folgenden Hinweise:<ul><li>Die Anmeldung ist nur als komplette Gruppe möglich.</li><li>Jede Gruppe benötigt einen volljährigen Gruppenleiter.</li><li>Änderung von Daten, Ab- oder Nachmeldungen sind nur über <a href="mailto:office@konkretlive.de">office@konkretlive.de</a> möglich.</li></ul>',
			
		});
		
		$translateProvider.preferredLanguage('de');
	}]);
