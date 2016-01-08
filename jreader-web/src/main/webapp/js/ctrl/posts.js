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
		post.bookmarked = true;
		$scope.ajaxService.bookmark(post);
	};
	
	$scope.deleteBookmark = function(post) {
		post.bookmarked = false;
		$scope.ajaxService.deleteBookmark(post);
	};
	
	$scope.archive = function(post) {
		post.archived = true;
		$scope.ajaxService.archive(post, post.archive);
	};
	
}]);
