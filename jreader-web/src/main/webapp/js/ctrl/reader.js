angular.module("jReaderApp").controller("ReaderCtrl", ["$scope", "$sce", "$interval", "ajaxService", "viewService", function ($scope, $sce, $interval, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.head = {};
	$scope.head.titlePrefix = "jReader";
	$scope.head.title = $scope.head.titlePrefix;
	
	$scope.groups = {};
	$scope.groups.items = [];
	$scope.groups.unreadCount = 0;
	
	$scope.groups.setItemsFromPayLoad = function(response) {
		$scope.groups.setItems(response.payload);
	};
	
	$scope.groups.setItems = function(items) {
		$scope.groups.items = items;
		var count = 0;
		angular.forEach($scope.groups.items, function(group) {
			count += group.unreadCount;
			group.editingTitle = false;
			group.newTitle = group.title;
			angular.forEach(group.subscriptions, function(subscription) {
				subscription.editingTitle = false;
				subscription.newTitle = subscription.title;
			});
		});
		
		$scope.groups.unreadCount = count;
		if (count > 0) {
			$scope.head.title = $scope.head.titlePrefix + " (" + count + ")";
		} else {
			$scope.head.title = $scope.head.titlePrefix;
		}
		
		$scope.menu.refreshSelection();
		$scope.menu.refreshCollapsion();
	};
	
	$scope.groups.refresh = function() {
		$scope.ajaxService.refreshSubscriptions().success(function(data) {
        	$scope.groups.setItemsFromPayLoad(data);
        });
    };
    
    $interval($scope.groups.refresh, 1000 * 60 * 5);
    $scope.groups.refresh();
	
	$scope.feedEntries = {};
	$scope.feedEntries.items = [];
	$scope.feedEntries.loading = false;
	$scope.feedEntries.moreItemsAvailable = true;
	$scope.feedEntries.visible = false;
	
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
		$scope.menu.refreshSelection();
	});
	
	$scope.feedEntries.append = function(entries) {
		var copiedEntries = angular.copy(entries);
		angular.forEach(copiedEntries, function(entry) {
			entry.description = $sce.trustAsHtml(entry.description);
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
    	
    	$scope.ajaxService.loadEntries(filter).success(function(response) {
    		$scope.feedEntries.moreItemsAvailable = response.payload.length === filter.count;
    		$scope.feedEntries.append(response.payload);
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
			$scope.ajaxService.markRead(entry).success($scope.groups.setItemsFromPayLoad);
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
		if (unreads.length > 0) {
			$scope.feedEntries.loading = true;
			$scope.viewService.entryFilter.resetPageIndex();
			$scope.ajaxService.markAllRead(unreads, $scope.viewService.entryFilter.get()).success(function(response) {
				$scope.groups.setItemsFromPayLoad(response);
				$scope.feedEntries.loading = false;
				$scope.feedEntries.load();
	        });
		}
	};
	
	$scope.feedEntries.refreshWithSubscriptions = function() {
		$scope.feedEntries.refresh();
		$scope.groups.refresh();
	};
	
	$scope.feedEntries.refresh = function() {
		$scope.viewService.entryFilter.resetPageIndex();
		$scope.feedEntries.load();
	};
	
	$scope.feedEntries.setOrderToAscending = function() {
		$scope.feedEntries.setAscendingOrder(true);
	};
	
	$scope.feedEntries.setOrderToDescending = function() {
		$scope.feedEntries.setAscendingOrder(false);
	};
	
	$scope.feedEntries.setAscendingOrder = function(ascending) {
		if (!angular.equals($scope.viewService.entryFilter.ascendingOrder, ascending)) {
			$scope.viewService.entryFilter.ascendingOrder = ascending;
			$scope.feedEntries.refresh();
		}
	};
	
	$scope.feedEntries.setSelectionToAll = function() {
		$scope.feedEntries.setSelection("all");
	};
	
	$scope.feedEntries.setSelectionToUnread = function() {
		$scope.feedEntries.setSelection("unread");
	};
	
	$scope.feedEntries.setSelectionToStarred = function() {
		$scope.feedEntries.setSelection("starred");
	};
	
	$scope.feedEntries.setSelection = function(s) {
		if (!angular.equals($scope.viewService.entryFilter.selection, s)) {
			$scope.viewService.entryFilter.selection = s;
			$scope.feedEntries.refresh();
		}
	};
	
	$scope.archivedEntries = {};
	$scope.archivedEntries.items = [];
	$scope.archivedEntries.loading = false;
	$scope.archivedEntries.moreItemsAvailable = true;
	$scope.archivedEntries.visible = false;
	
	$scope.archivedEntries.loadMore = function() {
		if ($scope.archivedEntries.moreItemsAvailable) {
			$scope.viewService.archiveFilter.incrementPageIndex();
			$scope.archivedEntries.load();
		}
	};
	
	$scope.archivedEntries.refresh = function() {
		$scope.viewService.archiveFilter.resetPageIndex();
		$scope.archivedEntries.load();
	};
	
	$scope.archivedEntries.setOrderToAscending = function() {
		$scope.archivedEntries.setAscendingOrder(true);
	};
	
	$scope.archivedEntries.setOrderToDescending = function() {
		$scope.archivedEntries.setAscendingOrder(false);
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
			entry.description = $sce.trustAsHtml(entry.description);
			$scope.archivedEntries.items.push(entry);
		});
	};
	
	$scope.archivedEntries.reset = function() {
		$scope.archivedEntries.items = [];
		$scope.archivedEntries.moreItemsAvailable = true;
	};
    
    $scope.archivedEntries.load = function() {
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
    	
    	$scope.ajaxService.loadArchivedEntries(filter).success(function(response) {
    		$scope.archivedEntries.moreItemsAvailable = response.payload.length === filter.count;
    		$scope.archivedEntries.append(response.payload);
    		$scope.archivedEntries.loading = false;
        });
    };
    
    $scope.archives = [];
    
	$scope.setArchives = function(response) {
		$scope.archives = response.payload;
    	angular.forEach($scope.archives, function(archive) {
			archive.editingTitle = false;
			archive.newTitle = archive.title;
		});
	};
	
	$scope.refreshArchives = function() {
		$scope.ajaxService.refreshArchives().success($scope.setArchives);
    };
	
    $scope.refreshArchives();
    
    $scope.menu = {};
    $scope.menu.uncollapsedItems = [];
	$scope.menu.homeSelected = true;
	$scope.menu.settingsSelected = false;
	$scope.menu.allItemsSelected = false;
	$scope.menu.archivedItemsSelected = false;
	$scope.menu.archivedItemsCollapsed = true;
	
	$scope.menu.refreshSelection = function() {
		$scope.menu.homeSelected = $scope.viewService.isHomeSelected();
		$scope.menu.settingsSelected = $scope.viewService.isSettingsSelected();
		$scope.menu.allItemsSelected = $scope.viewService.isAllItemsSelected();
		$scope.menu.archivedItemsSelected = $scope.viewService.isArchivedItemsSelected();
		
		angular.forEach($scope.groups.items, function(group) {
			group.selected = $scope.viewService.isGroupSelected(group.id);
			angular.forEach(group.subscriptions, function(subscription) {
				subscription.selected = $scope.viewService.isSubscriptionSelected(group.id, subscription.id);
			});
		});
		
		angular.forEach($scope.archives, function(archive) {
			archive.selected = $scope.viewService.isArchiveSelected(archive.id);
		});
	};
	
	$scope.menu.refreshCollapsion = function() {
		angular.forEach($scope.groups.items, function(group) {
			group.collapsed = $scope.menu.uncollapsedItems.indexOf(group.id) < 0;
		});
	};
	
	$scope.menu.collapse = function(groupId, $event) {
		var index = $scope.menu.uncollapsedItems.indexOf(groupId);
		if (index >= 0) {
			$scope.menu.uncollapsedItems.splice(index, 1);
		}
		$scope.menu.refreshCollapsion();
		$event.stopPropagation();
	};
	
	$scope.menu.uncollapse = function(groupId, $event) {
		var index = $scope.menu.uncollapsedItems.indexOf(groupId);
		if (index < 0) {
			$scope.menu.uncollapsedItems.push(groupId);
		}
		$scope.menu.refreshCollapsion();
		$event.stopPropagation();
	};
	
	$scope.menu.collapseArchivedItems = function($event) {
		$scope.menu.archivedItemsCollapsed = true;
		$event.stopPropagation();
	};
	
	$scope.menu.uncollapseArchivedItems = function($event) {
		$scope.menu.archivedItemsCollapsed = false;
		$event.stopPropagation();
	};
	
	$scope.shortcuts = {};
	$scope.shortcuts.handlers = [];
	
	$scope.shortcuts.register = function(char, predicate, callback) {
		$scope.shortcuts.handlers.push({
			"char": char.toLowerCase(),
			"predicate": predicate,
			"callback": callback
		});
	};
	
	$scope.shortcuts.handle = function(event) {
		var char = String.fromCharCode(event.keyCode).toLowerCase();
		angular.forEach($scope.shortcuts.handlers, function(handler) {
			if (angular.equals(handler.char, char) && handler.predicate()) {
				console.log("calling handler for " + char);
				handler.callback();
			}
		});
	};
    
}]);
