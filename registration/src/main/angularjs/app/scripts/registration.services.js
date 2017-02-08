'use strict';

var app = angular.module('registration')
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
		// TODO: Datumsgrenze!!!!
		
		var prices = [
			{id: 'price.nichtverdiener', title: 'Nichtverdiener', price: 99.00},
			{id: 'price.geringverdiener', title: 'Geringverdiener', price: 109.00},
			{id: 'price.vollverdiener', title: 'Vollverdiener', price: 119.00}
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
	;
				