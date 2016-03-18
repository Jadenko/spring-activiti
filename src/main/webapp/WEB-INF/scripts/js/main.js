(function() {

	var app = angular.module('countriesModule', [])
					 .controller('CountryListCtrl', ['$scope', '$http', CountryListCtrl]);

	function CountryListCtrl($scope, $http) {

		//this.countries = {};

		$http.get('http://localhost:8060/spring-activiti-tutos/getAllCountries').success(
				function(data) {
					$scope.countries = data;
				});
	}
	
	app.directive('countryTable', function() {
		return {
			restrict : 'E',
			link : function(scope, element, attrs) {

				var html = '<table>';

				angular.forEach(scope[attrs.elements], function(row, index) {
					if (index % 3 == 0) {
						html += '<tr>';
					}

					html += '<td>' + row.name + '</td>';

					if ((index + 1) % 3 == 0) {
						html += '</tr>';
					}
				})

				html += '</table>';

				element.replaceWith(html);
			}
		}
	});

})();