angular.module("jReaderApp").controller("PostsCtrl", ["$scope", "ajaxService", "viewService", "alertService", function ($scope, ajaxService, viewService, alertService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	$scope.alertService = alertService;
	
	$scope.active = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isPostsSelected();
	});
	
	$scope.toggleExpansion = function(post) {
		post.expanded = !post.expanded;
	};
	
	$scope.bookmark = function(post) {
		post.bookmarking = true;
		$scope.ajaxService.bookmark(post).then(function() {
			post.bookmarked = true;
			post.bookmarking = false;
		}, function(e) {
			post.bookmarked = false;
			post.bookmarking = false;
			$scope.alertService.add("Error occured while bookmarking '" + post.title + "'.");
		});
	};
	
	$scope.deleteBookmark = function(post) {
		post.bookmarking = true;
		$scope.ajaxService.deleteBookmark(post).then(function() {
			post.bookmarked = false;
			post.bookmarking = false;
		}, function() {
			post.bookmarked = true;
			post.bookmarking = false;
			$scope.alertService.add("Error occured while removing bookmark '" + post.title + "'.");
		});
	};
	
	$scope.archive = function(post) {
		post.archiving = true;
		$scope.ajaxService.archive(post, post.archive).then(function() {
			post.archived = true;
			post.archiving = false;
		}, function() {
			post.archived = false;
			post.archiving = false;
			$scope.alertService.add("Error occured while archiving '" + post.title + "'.");
		});
	};
	
}]);
