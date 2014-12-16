angular.module("jReaderApp").controller("ArchivesNavbarCtrl", ["$scope", "$http", "$window", "ajaxService", "viewService", function ($scope, $http, $window, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.loading = false;
	$scope.entries = [];
	
	$scope.$watch("viewService.activeView", function() {
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
		$scope.ajaxService.loadArchivedEntries($scope.viewService.archiveFilter.get());
	};
	
	$scope.refreshEntries = function() {
		$scope.viewService.archiveFilter.resetPageIndex();
		$scope.loadEntries();
	};
	
	$scope.setAscendingOrder = function(ascending) {
		if (!angular.equals(this.viewService.archiveFilter.ascendingOrder, ascending)) {
			this.viewService.archiveFilter.ascendingOrder = ascending;
			$scope.refreshEntries();
		}
	};
	
}]);
