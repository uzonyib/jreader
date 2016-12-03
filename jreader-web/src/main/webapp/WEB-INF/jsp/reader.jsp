<!DOCTYPE html>
<html data-ng-app="jReaderApp" data-ng-controller="ReaderCtrl">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title data-ng-bind="head.title">jReader</title>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="/css/reader-all.css">
		<link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
		<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
		<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular.min.js"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.5/angular-animate.min.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/ngInfiniteScroll/1.2.2/ng-infinite-scroll.min.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/angular-chart.js/1.0.3/angular-chart.min.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.13.0/moment.min.js"></script>
		<script type="text/javascript" src="/js/reader-all.js"></script>
	</head>
	<body data-ng-keyup="shortcuts.handle($event)">
		<div id="navbar">
			<jsp:include page="navbar.jsp" />
		</div>
		<jsp:include page="alerts.jsp" />
		<div class="container-fluid">
			<div class="row">
				<div id="menu" class="reader-menu col-lg-2 col-md-2 col-sm-3 hidden-xs">
					<jsp:include page="menu.jsp" />
				</div>
				<div id="main-area" class="col-lg-10 col-md-10 col-sm-9 col-xs-12 col-lg-offset-2 col-md-offset-2 col-sm-offset-3">
					<div id="home-contents" data-ng-controller="HomeCtrl" data-ng-show="active">
						<jsp:include page="home.jsp" />
					</div>
					<div id="settings-contents" data-ng-controller="SettingsCtrl" data-ng-show="active">
						<jsp:include page="settings.jsp" />
					</div>
					<div id="posts-contents" data-ng-controller="PostsCtrl" data-ng-show="active">
						<jsp:include page="posts.jsp" />
					</div>
					<div id="archives-contents" data-ng-controller="ArchivesCtrl" data-ng-show="active">
						<jsp:include page="archives.jsp" />
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
