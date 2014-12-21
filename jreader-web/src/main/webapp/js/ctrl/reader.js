angular.module("jReaderApp").controller("ReaderCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.feedEntries = [];
	$scope.archivedEntries = [];
	$scope.archives = [];
	
	$scope.$watch("viewService.activeView", function() {
		if ($scope.viewService.isEntriesSelected()) {
			$scope.refreshFeedEntries();
		} else if ($scope.viewService.isArchivesSelected()) {
			$scope.refreshArchivedEntries();
		}
	});
	
	// TODO eliminate watch
	$scope.$watch("ajaxService.entries", function(entries) {
		$scope.feedEntries = angular.copy(entries);
		angular.forEach($scope.feedEntries, function(entry) {
			entry.archived = false;
			entry.archive = $scope.archives[0];
		});
	});
	
	// TODO eliminate watch
	$scope.$watch("ajaxService.archivedEntries", function(entries) {
		$scope.archivedEntries = angular.copy(entries);
	});
	
	$scope.$watch("ajaxService.archives", function(archives) {
		$scope.archives = angular.copy(archives);
	});
	
	$scope.markAllFeedEntriesRead = function() {
		var unreads = [];
		angular.forEach($scope.feedEntries, function(entry) {
			if (!entry.read) {
				entry.read = true;
				unreads.push(entry);
			}
		});
		$scope.viewService.entryFilter.resetPageIndex();
		$scope.ajaxService.markAllRead(unreads, $scope.viewService.entryFilter.get());
	};
	
	$scope.loadFeedEntries = function() {
		$scope.ajaxService.loadEntries($scope.viewService.entryFilter.get());
	};
	
	$scope.loadMoreFeedEntries = function() {
		if ($scope.ajaxService.moreEntriesAvailable) {
			$scope.viewService.entryFilter.incrementPageIndex();
			$scope.ajaxService.loadEntries($scope.viewService.entryFilter.get());
		}
	};
	
	$scope.refreshFeedEntries = function() {
		$scope.viewService.entryFilter.resetPageIndex();
		$scope.loadFeedEntries();
	};
	
	$scope.loadArchivedEntries = function() {
		$scope.ajaxService.loadArchivedEntries($scope.viewService.archiveFilter.get());
	};
	
	$scope.loadMoreArchivedEntries = function() {
		if ($scope.ajaxService.moreArchivedEntriesAvailable) {
			$scope.viewService.archiveFilter.incrementPageIndex();
			$scope.ajaxService.loadArchivedEntries($scope.viewService.archiveFilter.get());
		}
	};
	
	$scope.refreshArchivedEntries = function() {
		$scope.viewService.archiveFilter.resetPageIndex();
		$scope.loadArchivedEntries();
	};
	
	$scope.setFeedEntryOrder = function(ascending) {
		if (!angular.equals($scope.viewService.entryFilter.ascendingOrder, ascending)) {
			$scope.viewService.entryFilter.ascendingOrder = ascending;
			$scope.refreshFeedEntries();
		}
	};
	
	$scope.setFeedEntrySelection = function(s) {
		if (!angular.equals($scope.viewService.entryFilter.selection, s)) {
			$scope.viewService.entryFilter.selection = s;
			$scope.refreshFeedEntries();
		}
	};
	
	$scope.setArchivedEntryOrder = function(ascending) {
		if (!angular.equals($scope.viewService.archiveFilter.ascendingOrder, ascending)) {
			$scope.viewService.archiveFilter.ascendingOrder = ascending;
			$scope.refreshArchivedEntries();
		}
	};
	
}]);
