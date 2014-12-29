angular.module("jReaderApp").controller("ReaderCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.feedEntries = {};
	$scope.feedEntries.items = [];
	$scope.feedEntries.loading = false;
	$scope.feedEntries.moreItemsAvailable = true;
	$scope.feedEntries.visible = false;
	
	$scope.archivedEntries = {};
	$scope.archivedEntries.items = [];
	$scope.archivedEntries.loading = false;
	$scope.archivedEntries.moreItemsAvailable = true;
	$scope.archivedEntries.visible = false;
	
	$scope.archives = [];
	
	$scope.$watch("viewService.activeView", function() {
		if ($scope.viewService.isEntriesSelected()) {
			$scope.feedEntries.visible = true;
			$scope.archivedEntries.visible = false;
			$scope.feedEntries.refresh();
		} else if ($scope.viewService.isArchivesSelected()) {
			$scope.feedEntries.visible = false;
			$scope.archivedEntries.visible = true;
			$scope.archivedEntries.refresh();
		} else {
			$scope.feedEntries.visible = false;
			$scope.archivedEntries.visible = false;
		}
	});
	
	$scope.feedEntries.append = function(entries) {
		var copiedEntries = angular.copy(entries);
		angular.forEach(copiedEntries, function(entry) {
			entry.archived = false;
			entry.archive = $scope.archives[0];
			$scope.feedEntries.items.push(entry);
		});
	};
	
	$scope.feedEntries.reset = function() {
		$scope.feedEntries.items = [];
		$scope.feedEntries.moreItemsAvailable = true;
	};
	
	$scope.feedEntries.load = function() {
    	if ($scope.feedEntries.loading) {
    		return;
    	}
    	$scope.feedEntries.loading = true;
    	var filter = $scope.viewService.entryFilter.get();
    	if (filter.pageIndex === 0) {
    		$scope.feedEntries.reset();
    	}
    	
    	filter.offset = filter.pageIndex > 0 ? (filter.initialPagesToLoad - 1 + filter.pageIndex) * filter.pageSize : 0;
    	filter.count = filter.pageIndex > 0 ? filter.pageSize : filter.initialPagesToLoad * filter.pageSize;
    	
    	$scope.ajaxService.loadEntries(filter).success(function(data) {
    		$scope.feedEntries.moreItemsAvailable = data.length === filter.count;
    		$scope.feedEntries.append(data);
    		$scope.feedEntries.loading = false;
        });
    };
	
	$scope.feedEntries.loadMore = function() {
		if ($scope.feedEntries.moreItemsAvailable) {
			$scope.viewService.entryFilter.incrementPageIndex();
			$scope.feedEntries.load();
		}
	};
	
	$scope.feedEntries.markRead = function(entry) {
		if (!entry.read) {
			entry.read = true;
			$scope.ajaxService.markRead(entry);
		}
	};
	
	$scope.feedEntries.markAllRead = function() {
		var unreads = [];
		angular.forEach($scope.feedEntries.items, function(entry) {
			if (!entry.read) {
				entry.read = true;
				unreads.push(entry);
			}
		});
		$scope.feedEntries.loading = true;
		$scope.viewService.entryFilter.resetPageIndex();
		$scope.ajaxService.markAllRead(unreads, $scope.viewService.entryFilter.get()).success(function(response) {
			$scope.feedEntries.loading = false;
			$scope.feedEntries.load();
        });
	};
	
	$scope.feedEntries.refresh = function() {
		$scope.viewService.entryFilter.resetPageIndex();
		$scope.feedEntries.load();
	};
	
	$scope.feedEntries.setAscendingOrder = function(ascending) {
		if (!angular.equals($scope.viewService.entryFilter.ascendingOrder, ascending)) {
			$scope.viewService.entryFilter.ascendingOrder = ascending;
			$scope.feedEntries.refresh();
		}
	};
	
	$scope.feedEntries.setSelection = function(s) {
		if (!angular.equals($scope.viewService.entryFilter.selection, s)) {
			$scope.viewService.entryFilter.selection = s;
			$scope.feedEntries.refresh();
		}
	};
	
	$scope.archivedEntries.load = function() {
		$scope.ajaxService.loadArchivedEntries($scope.viewService.archiveFilter.get());
	};
	
	$scope.archivedEntries.loadMore = function() {
		if ($scope.ajaxService.moreArchivedEntriesAvailable) {
			$scope.viewService.archiveFilter.incrementPageIndex();
			$scope.archivedEntries.load();
		}
	};
	
	$scope.archivedEntries.refresh = function() {
		$scope.viewService.archiveFilter.resetPageIndex();
		$scope.archivedEntries.load();
	};
	
	$scope.archivedEntries.setAscendingOrder = function(ascending) {
		if (!angular.equals($scope.viewService.archiveFilter.ascendingOrder, ascending)) {
			$scope.viewService.archiveFilter.ascendingOrder = ascending;
			$scope.archivedEntries.refresh();
		}
	};
	
	$scope.archivedEntries.append = function(entries) {
		var copiedEntries = angular.copy(entries);
		angular.forEach(copiedEntries, function(entry) {
			$scope.archivedEntries.items.push(entry);
		});
	};
	
	$scope.archivedEntries.reset = function() {
		$scope.archivedEntries.items = [];
		$scope.archivedEntries.moreItemsAvailable = true;
	};
    
    $scope.archivedEntries.load = function(filter) {
    	if ($scope.archivedEntries.loading) {
    		return;
    	}
    	$scope.archivedEntries.loading = true;
    	var filter = $scope.viewService.archiveFilter.get();
    	if (filter.pageIndex === 0) {
    		$scope.archivedEntries.reset();
    	}
    	
    	var items = "";
    	if (filter.archiveId != null) {
    		items = "/" + filter.archiveId;
    	}
    	
    	filter.offset = filter.pageIndex > 0 ? (filter.initialPagesToLoad - 1 + filter.pageIndex) * filter.pageSize : 0;
    	filter.count = filter.pageIndex > 0 ? filter.pageSize : filter.initialPagesToLoad * filter.pageSize;
    	
    	$scope.ajaxService.loadArchivedEntries(filter).success(function(data) {
    		$scope.archivedEntries.moreItemsAvailable = data.length === filter.count;
    		$scope.archivedEntries.append(data);
    		$scope.archivedEntries.loading = false;
        });
    };
    
	$scope.setArchives = function(archives) {
		$scope.archives = archives;
    	angular.forEach($scope.archives, function(archive) {
			archive.editingTitle = false;
			archive.newTitle = archive.title;
		});
	};
	
	$scope.refreshArchives = function() {
		$scope.ajaxService.refreshArchives().success(function(archives) {
        	$scope.setArchives(archives);
        });
    };
	
    $scope.refreshArchives();
}]);
