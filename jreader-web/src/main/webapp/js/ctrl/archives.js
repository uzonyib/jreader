angular.module("jReaderApp").controller("ArchivesCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isArchivesSelected();
	});
	
	$scope.toggleCollapsion = function(post) {
		post.uncollapsed = !post.uncollapsed;
	};
	
	$scope.deletePost = function(post) {
		$scope.ajaxService.deleteArchivedPost(post.archiveId, post.id).success($scope.archivedPosts.refresh);
	};
	
}]);
