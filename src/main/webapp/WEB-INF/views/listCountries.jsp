<!doctype html>
<html ng-app="countriesModule">
	<head>
		<title>Hello AngularJS</title>
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular.min.js"></script>
    	<script type="text/javascript"><%@include file="../scripts/js/main.js" %></script>
	</head>

	<body ng-controller="CountryListCtrl">
	
	<country-table elements="countries"></country-table>
	
	<!-- <table>
		<tr>
			<td ng-repeat="country in countries">
				<p>The country is {{country.name}}</p>
				<p>The capital is {{country.capital}}</p>
				<p>People who go there : {{country.visitors.length}}</p>
			</td>
		</tr>		
	</table> -->
		
	</body>
</html>