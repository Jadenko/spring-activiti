<!doctype html>
<html ng-app="countriesModule">
<head>
<title>Hello AngularJS</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular.min.js"></script>
<script type="text/javascript"><%@include file="../scripts/js/main.js" %></script>
</head>

<body ng-controller="CountryListCtrl">

	<country-table rows="countries"></country-table>

</body>
</html>