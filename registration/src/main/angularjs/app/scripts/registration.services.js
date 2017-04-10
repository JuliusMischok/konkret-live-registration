'use strict';

var app = angular.module('registration')
	.service('registrationService', ['$resource', 'backendServiceUrl', function($resource, backendServiceUrl) {
			this.registration = function() {
				return $resource(
						backendServiceUrl + 'registration');
			};
			this.staffregistration = function() {
				return $resource(backendServiceUrl + 'registration/staff');
			};
	}])
	.factory('grouptypeService', function() {
		var grouptypes = [
			{id: 'grouptype.teenkreis', title: 'Teenkreis'},
			{id: 'grouptype.jugendkreis', title: 'Jugendkreis'},
			{id: 'grouptype.kje', title: 'KJE'},
			{id: 'grouptype.bu', title: 'BU'},
			{id: 'grouptype.sonstiges', title: 'Sonstiges'}
		];
		
		return {
			grouptypes: function() {
				return [].concat(grouptypes);
			},
			getGrouptype: function(id) {
				var filteredGrouptypes = [].concat(grouptypes).filter(function(type) {return type.id === id;});
				
				if (filteredGrouptypes.length === 1) {
					return filteredGrouptypes[0].title;
				} else {
					return 'n/a';
				}
			}
		};
	})
	.factory('districtService', function() {
		var districts = [
			{id: 'district.suedbayern', title: 'Südbayerischer Kreis'},
			{id: 'district.nordbayern', title: 'Nordbayerischer Kreis'},
			{id: 'district.suedbawue', title: 'Baden-Württemberg Süd Kreis'},
			{id: 'district.nordbawue', title: 'Baden-Württemberg Nord Kreis'},
			{id: 'district.sonstiges', title: 'Sonstiges'}
		];
		
		return {
			districts: function() {
				return [].concat(districts);
			}
		};
	})
	.factory('priceService', function() {
		var priceChange = new Date(2017, 3, 30, 0, 0, 0);
		var now = Date.now();
		
		var prices = (now < priceChange) ? [
			{id: 'price.nichtverdiener', title: 'Nichtverdiener', price: 89.00},
			{id: 'price.geringverdiener', title: 'Geringverdiener', price: 99.00},
			{id: 'price.vollverdiener', title: 'Vollverdiener', price: 109.00}
		] : [
			{id: 'price.nichtverdiener.spaet', title: 'Nichtverdiener', price: 99.00},
			{id: 'price.geringverdiener.spaet', title: 'Geringverdiener', price: 109.00},
			{id: 'price.vollverdiener.spaet', title: 'Vollverdiener', price: 119.00}
		];
		
		return {
			prices: function() {
				return [].concat(prices);
			},
			getPrice: function(id) {
				var filteredPrices = [].concat(prices).filter(function(price) {return price.id === id;});
				
				if (filteredPrices.length === 1) {
					return filteredPrices[0].price;
				} else {
					return undefined;
				}
			}
		};
	})
	
	.factory('housingService', function() {
		var housings = [
			{id: 'housing.self', title: 'Ich kümmere mich selbst'},
			{id: 'housing.tent', title: 'Mitarbeiterschlafzelt'},
			{id: 'housing.bed', title: 'Zimmer mit Bett'},
			{id: 'housing.other', title: 'Sonstiges'}
		];
		
		return {
			housings: function() {
				return [].concat(housings);
			}
		};
	})
	
	.factory('ouService', function() {
		var ous = [
			{id: 'ou.decentral', title: 'Dezentrales Programm'},
			{id: 'ou.general', title: 'Gesamtleitung'},
			{id: 'ou.hosting', title: 'Hosting'},
			{id: 'ou.logistics', title: 'Logistik'},
			{id: 'ou.staffservice', title: 'Mitarbeiter-Service'},
			{id: 'ou.pr', title: 'Öffentlichkeitsarbeit'},
			{id: 'ou.office', title: 'Office'},
			{id: 'ou.counseling', title: 'Seelsorge'},
			{id: 'ou.soundandlight', title: 'Technik'},
			{id: 'ou.central', title: 'Zentrales Programm'},
			{id: 'ou.other', title: 'Sonstiges'}
		]; 
			
		return {
			ous: function() {
				return [].concat(ous);
			}
		};
	})
	;
				