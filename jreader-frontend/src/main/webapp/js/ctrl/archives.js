angular.module("jReaderApp").controller("ArchivesCtrl", ["$scope", "$http", "$window", "ajaxService", "viewService", function ($scope, $http, $window, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.entries = [];
	
	$scope.filter = {};
	$scope.filter.archiveId = null;
	$scope.filter.pageIndex = 0;
	$scope.filter.ascendingOrder = true;
	$scope.filter.pageSize = Math.ceil($window.innerHeight / 29 / 10) * 10;
	$scope.filter.initialPagesToLoad = 2;
	
	$scope.loading = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.filter.archiveId = $scope.viewService.getArchiveId();
		$scope.active = $scope.viewService.isArchivesSelected();
		if ($scope.active) {
			$scope.refreshEntries();
		}
	});
	
	$scope.$watch("ajaxService.archivedEntries", function(entries) {
		$scope.entries = angular.copy(entries);
	});
	
	$scope.$watch("ajaxService.loadingArchivedEntries", function(loadingEntries) {
		$scope.loading = loadingEntries;
	});
	
	$scope.loadEntries = function() {
		$scope.ajaxService.loadArchivedEntries($scope.filter);
	};
	
	$scope.refreshEntries = function() {
		$scope.filter.pageIndex = 0;
		$scope.loadEntries();
	};
	
	$scope.toggleCollapsion = function(entry) {
		entry.uncollapsed = !entry.uncollapsed;
	};
	
	$scope.setAscendingOrder = function(ascending) {
		if (!angular.equals($scope.filter.ascendingOrder, ascending)) {
			$scope.filter.ascendingOrder = ascending;
			$scope.refreshEntries();
		}
	};
	
	$scope.loadMoreEntries = function() {
		if ($scope.ajaxService.moreArchivedEntriesAvailable) {
			++$scope.filter.pageIndex;
			$scope.loadEntries();
		}
	};
	
	$scope.deleteEntry = function(entry) {
		$scope.ajaxService.deleteArchivedEntry(entry.archiveId, entry.id).success(function() {
			$scope.refreshEntries();
		});
	};
	
}]);
