angular.module("jReaderApp").controller("EntriesNavbarCtrl", ["$scope", "$window", "ajaxService", "viewService", function ($scope, $window, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.loading = false;
	$scope.entries = [];
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isEntriesSelected();
		if ($scope.active) {
			$scope.refreshEntries();
		}
	});
	
	$scope.$watch("ajaxService.entries", function(entries) {
		$scope.entries = angular.copy(entries);
	});
	
	$scope.$watch("ajaxService.loadingEntries", function(loadingEntries) {
		$scope.loading = loadingEntries;
	});
	
	$scope.loadEntries = function() {
		$scope.ajaxService.loadEntries($scope.viewService.entryFilter.get());
	};
	
	$scope.markAllRead = function() {
		var unreads = [];
		angular.forEach($scope.entries, function(entry) {
			if (!entry.read) {
				entry.read = true;
				unreads.push(entry);
			}
		});
		$scope.viewService.entryFilter.resetPageIndex();
		$scope.ajaxService.markAllRead(unreads, $scope.viewService.entryFilter.get());
	};
	
	$scope.refreshEntries = function() {
		$scope.viewService.entryFilter.resetPageIndex();
		$scope.loadEntries();
	};
	
	$scope.setAscendingOrder = function(ascending) {
		if (!angular.equals(this.viewService.entryFilter.ascendingOrder, ascending)) {
			$scope.viewService.entryFilter.ascendingOrder = ascending;
			$scope.refreshEntries();
		}
	};
	
	$scope.setSelection = function(s) {
		if (!angular.equals(this.viewService.entryFilter.selection, s)) {
			$scope.viewService.entryFilter.selection = s;
			$scope.refreshEntries();
		}
	};
	
}]);
