angular.module("jReaderApp").controller("PostsCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
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
		});
	};
	
	$scope.deleteBookmark = function(post) {
		post.bookmarking = true;
		$scope.ajaxService.deleteBookmark(post).then(function() {
			post.bookmarking = false;
			post.bookmarked = false;
		});
	};
	
	$scope.archive = function(post) {
		post.archived = true;
		$scope.ajaxService.archive(post, post.archive);
	};
	
}]);
