angular.module("jReaderApp").controller("ArchivesCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.entries = [];
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isArchivesSelected();
	});
	
	$scope.$watch("ajaxService.archivedEntries", function(entries) {
		$scope.entries = angular.copy(entries);
	});
	
	$scope.refreshEntries = function() {
		$scope.viewService.archiveFilter.resetPageIndex();
		$scope.ajaxService.loadArchivedEntries($scope.viewService.archiveFilter.get());
	};
	
	$scope.toggleCollapsion = function(entry) {
		entry.uncollapsed = !entry.uncollapsed;
	};
	
	$scope.loadMoreEntries = function() {
		if ($scope.ajaxService.moreArchivedEntriesAvailable) {
			$scope.viewService.archiveFilter.incrementPageIndex();
			$scope.ajaxService.loadArchivedEntries($scope.viewService.archiveFilter.get());
		}
	};
	
	$scope.deleteEntry = function(entry) {
		$scope.ajaxService.deleteArchivedEntry(entry.archiveId, entry.id).success(function() {
			$scope.refreshEntries();
		});
	};
	
}]);
