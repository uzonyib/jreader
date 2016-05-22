angular.module("jReaderApp").controller("ArchivesCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isArchivesSelected();
	});
	
	$scope.toggleExpansion = function(post) {
		post.expanded = !post.expanded;
	};
	
	$scope.deletePost = function(post) {
		post.deleting = true;
		$scope.ajaxService.deleteArchivedPost(post.archiveId, post.id).then($scope.archivedPosts.refresh, function() {
			post.deleting = false;
			$scope.alertService.add("Error occured while deleting '" + post.title + "'.");
		});
	};
	
}]);
