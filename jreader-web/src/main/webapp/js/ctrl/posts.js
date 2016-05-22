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
			post.bookmarking = false;
			post.bookmarked = true;
		}, function(e) {
			$scope.alertService.add("Error occured while bookmarking '" + post.title + "'.");
			console.log(e)
		});
	};
	
	$scope.deleteBookmark = function(post) {
		post.bookmarking = true;
		$scope.ajaxService.deleteBookmark(post).then(function() {
			post.bookmarking = false;
			post.bookmarked = false;
		}, function() {
			$scope.alertService.add("Error occured while removing bookmark '" + post.title + "'.");
		});
	};
	
	$scope.archive = function(post) {
		post.archived = true;
		$scope.ajaxService.archive(post, post.archive);
	};
	
}]);
