(function() {

	var app = angular.module('countriesModule', []);

	//Directives
	app.directive('countryTable', function($compile) {
		return {
			restrict : 'E',
			link : function(scope, element, attrs) {

				scope.$watch('countries', function() {
					element.html(scope.countries);
					$compile(element.contents())(scope);
				});

			}
		}
	});

	//Services
	app.factory('httpGet', function($http) {
						return {
							get : function() {
								return $http.get('http://localhost:8060/spring-activiti-tutos/getAllCountries')
										// this will return a promise to controller
										.then(
												function(response) {
													if (typeof response.data === 'object') {
														return response.data;
													} else {
														// invalid response
														return $q.reject(response.data);
													}

												},
												function(response) {
													// something went wrong
													return $q.reject(response.data);
												});
							}
						}
					});

	//Controllers
	app.controller('CountryListCtrl', function($scope, httpGet) {

		httpGet.get().then(function(data) {
			// promise fulfilled

			var html = '<table>';
			angular.forEach(data, function(row, index) {
				if (index % 3 == 0) {
					html += '<tr>';
				}

				html += '<td>' + row.name + '</td>';

				if ((index + 1) % 3 == 0) {
					html += '</tr>';
				}
			})

			html += '</table>';

			$scope.countries = html;

		}, function(error) {
			// promise rejected, could log the error with: console.log('error',
			// error);
			console.log("Damn it");
		});

	});

})();