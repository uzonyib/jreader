angular.module("jReaderApp").controller("ArchivesCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isArchivesSelected();
	});
	
	$scope.toggleCollapsion = function(entry) {
		entry.uncollapsed = !entry.uncollapsed;
	};
	
	$scope.deleteEntry = function(entry) {
		$scope.ajaxService.deleteArchivedEntry(entry.archiveId, entry.id).success($scope.archivedEntries.refresh);
	};
	
}]);
