angular.module("jReaderApp").controller("PostsCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isPostsSelected();
	});
	
	$scope.toggleCollapsion = function(post) {
		post.uncollapsed = !post.uncollapsed;
	};
	
	$scope.star = function(post) {
		post.starred = true;
		$scope.ajaxService.star(post);
	};
	
	$scope.unstar = function(post) {
		post.starred = false;
		$scope.ajaxService.unstar(post);
	};
	
	$scope.archive = function(post) {
		post.archived = true;
		$scope.ajaxService.archive(post, post.archive);
	};
	
}]);
