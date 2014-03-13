<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="jReaderApp">
	<head ng-controller="HeadCtrl">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title ng-bind="title">jReader</title>
		<link rel="stylesheet" type="text/css" href="/css/reset.css">
		<link rel="stylesheet" type="text/css" href="/css/reader.css">
		<link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular.min.js"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-sanitize.min.js"></script>
		<script type="text/javascript" src="/js/moment.min.js"></script>
		<script type="text/javascript" src="/js/reader.js"></script>
	</head>
	<body>
		<div id="menu" ng-controller="MenuCtrl">
			<jsp:include page="menu.jsp" />
		</div>
		<div id="main-area">
			<div id="home-contents" ng-controller="HomeCtrl" ng-show="active">
				<jsp:include page="home.jsp" />
			</div>
			<div id="settings-contents" ng-controller="SettingsCtrl" ng-show="active">
				<jsp:include page="settings.jsp" />
			</div>
			<div id="items-contents" ng-controller="EntriesCtrl" ng-show="active">
				<jsp:include page="entries.jsp" />
			</div>
		</div>
	</body>
</html>
