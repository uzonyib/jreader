angular.module("jReaderApp").controller("ReaderCtrl", ["$scope", "$sce", "$interval", "ajaxService", "viewService", "alertService", function ($scope, $sce, $interval, ajaxService, viewService, alertService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	$scope.alertService = alertService;
	
	$scope.head = {};
	$scope.head.titlePrefix = "jReader";
	$scope.head.title = $scope.head.titlePrefix;
	
	$scope.groups = {};
	$scope.groups.items = [];
	$scope.groups.unreadCount = 0;
	
	$scope.groups.setItemsFromPayLoad = function(response) {
		$scope.groups.setItems(response.payload);
	};
	
	$scope.groups.add = function(response) {
		$scope.groups.items.push(response);
	};
	
	$scope.groups.update = function(response) {
		var index = $scope.groups.indexOf(response.id);
		$scope.groups.items[index] = response;
		console.log($scope.groups.items);
	};
	
	$scope.groups.remove = function(id) {
		$scope.groups.items.splice($scope.groups.indexOf(id), 1);
	};
	
	$scope.groups.indexOf = function(id) {
		var index = null;
		angular.forEach($scope.groups.items, function(item, idx) {
			if (angular.equals(id, item.id)) {
				index = idx;
			}
		});
		return index;
	};
	
	$scope.groups.size = function() {
		return $scope.groups.items.length;
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
		$scope.menu.refreshExpansion();
	};
	
	$scope.groups.refresh = function() {
		$scope.ajaxService.refreshSubscriptions().success(function(response) {
		    $scope.groups.setItems(response);
        });
    };
    
    $interval($scope.groups.refresh, 1000 * 60 * 5);
    $scope.groups.refresh();
	
	$scope.posts = {};
	$scope.posts.items = [];
	$scope.posts.loading = false;
	$scope.posts.moreItemsAvailable = true;
	$scope.posts.visible = false;
	
	$scope.$watch("viewService.activeView", function() {
		if ($scope.viewService.isPostsSelected()) {
			$scope.posts.visible = true;
			$scope.archivedPosts.visible = false;
			$scope.posts.refresh();
		} else if ($scope.viewService.isArchivesSelected()) {
			$scope.posts.visible = false;
			$scope.archivedPosts.visible = true;
			$scope.archivedPosts.refresh();
		} else {
			$scope.posts.visible = false;
			$scope.archivedPosts.visible = false;
		}
		$scope.menu.refreshSelection();
	});
	
	$scope.posts.append = function(posts) {
		var copiedPosts = angular.copy(posts);
		angular.forEach(copiedPosts, function(post) {
			post.description = $sce.trustAsHtml(post.description);
			post.archived = false;
			post.archive = $scope.archives[0];
			$scope.posts.items.push(post);
		});
	};
	
	$scope.posts.reset = function() {
		$scope.posts.items = [];
		$scope.posts.moreItemsAvailable = true;
	};
	
	$scope.posts.load = function() {
    	if ($scope.posts.loading) {
    		return;
    	}
    	$scope.posts.loading = true;
    	var filter = $scope.viewService.postFilter.get();
    	if (filter.pageIndex === 0) {
    		$scope.posts.reset();
    	}
    	
    	filter.offset = filter.pageIndex > 0 ? (filter.initialPagesToLoad - 1 + filter.pageIndex) * filter.pageSize : 0;
    	filter.count = filter.pageIndex > 0 ? filter.pageSize : filter.initialPagesToLoad * filter.pageSize;
    	
    	$scope.ajaxService.loadPosts(filter).then(function(response) {
    		$scope.posts.moreItemsAvailable = response.data.payload.length === filter.count;
    		$scope.posts.append(response.data.payload);
    		$scope.posts.loading = false;
        }, function() {
        	$scope.posts.loading = false;
        	$scope.alertService.add("Error occured while loading posts.");
        });
    };
	
	$scope.posts.loadMore = function() {
		if ($scope.posts.moreItemsAvailable) {
			$scope.viewService.postFilter.incrementPageIndex();
			$scope.posts.load();
		}
	};
	
	$scope.posts.markRead = function(post) {
		if (!post.read) {
			post.read = true;
			$scope.ajaxService.markRead(post).then(function(response) {
				$scope.groups.setItemsFromPayLoad(response.data);
			}, function() {
	        	$scope.posts.loading = false;
	        	$scope.alertService.add("Error occured while marking post '" + post.title + "' read.");
	        });
		}
	};
	
	$scope.posts.markAllRead = function() {
		var unreads = [];
		angular.forEach($scope.posts.items, function(post) {
			if (!post.read) {
				post.read = true;
				unreads.push(post);
			}
		});
		if (unreads.length > 0) {
			$scope.posts.loading = true;
			$scope.viewService.postFilter.resetPageIndex();
			$scope.ajaxService.markAllRead(unreads, $scope.viewService.postFilter.get()).then(function(response) {
				$scope.groups.setItemsFromPayLoad(response.data);
				$scope.posts.loading = false;
				$scope.posts.load();
	        }, function() {
	        	$scope.posts.loading = false;
	        	$scope.alertService.add("Error occured while marking posts read.");
	        });
		}
	};
	
	$scope.posts.refreshWithSubscriptions = function() {
		$scope.posts.refresh();
		$scope.groups.refresh();
	};
	
	$scope.posts.refresh = function() {
		$scope.viewService.postFilter.resetPageIndex();
		$scope.posts.load();
	};
	
	$scope.posts.setOrderToAscending = function() {
		$scope.posts.setAscendingOrder(true);
	};
	
	$scope.posts.setOrderToDescending = function() {
		$scope.posts.setAscendingOrder(false);
	};
	
	$scope.posts.setAscendingOrder = function(ascending) {
		if (!angular.equals($scope.viewService.postFilter.ascendingOrder, ascending)) {
			$scope.viewService.postFilter.ascendingOrder = ascending;
			$scope.posts.refresh();
		}
	};
	
	$scope.posts.setSelectionToAll = function() {
		$scope.posts.setSelection("all");
	};
	
	$scope.posts.setSelectionToUnread = function() {
		$scope.posts.setSelection("unread");
	};
	
	$scope.posts.setSelectionToBookmarked = function() {
		$scope.posts.setSelection("bookmarked");
	};
	
	$scope.posts.setSelection = function(s) {
		if (!angular.equals($scope.viewService.postFilter.selection, s)) {
			$scope.viewService.postFilter.selection = s;
			$scope.posts.refresh();
		}
	};
	
	$scope.archivedPosts = {};
	$scope.archivedPosts.items = [];
	$scope.archivedPosts.loading = false;
	$scope.archivedPosts.moreItemsAvailable = true;
	$scope.archivedPosts.visible = false;
	
	$scope.archivedPosts.loadMore = function() {
		if ($scope.archivedPosts.moreItemsAvailable) {
			$scope.viewService.archiveFilter.incrementPageIndex();
			$scope.archivedPosts.load();
		}
	};
	
	$scope.archivedPosts.refresh = function() {
		$scope.viewService.archiveFilter.resetPageIndex();
		$scope.archivedPosts.load();
	};
	
	$scope.archivedPosts.setOrderToAscending = function() {
		$scope.archivedPosts.setAscendingOrder(true);
	};
	
	$scope.archivedPosts.setOrderToDescending = function() {
		$scope.archivedPosts.setAscendingOrder(false);
	};
	
	$scope.archivedPosts.setAscendingOrder = function(ascending) {
		if (!angular.equals($scope.viewService.archiveFilter.ascendingOrder, ascending)) {
			$scope.viewService.archiveFilter.ascendingOrder = ascending;
			$scope.archivedPosts.refresh();
		}
	};
	
	$scope.archivedPosts.append = function(posts) {
		var copiedPosts = angular.copy(posts);
		angular.forEach(copiedPosts, function(post) {
			post.description = $sce.trustAsHtml(post.description);
			$scope.archivedPosts.items.push(post);
		});
	};
	
	$scope.archivedPosts.reset = function() {
		$scope.archivedPosts.items = [];
		$scope.archivedPosts.moreItemsAvailable = true;
	};
    
    $scope.archivedPosts.load = function() {
    	if ($scope.archivedPosts.loading) {
    		return;
    	}
    	$scope.archivedPosts.loading = true;
    	var filter = $scope.viewService.archiveFilter.get();
    	if (filter.pageIndex === 0) {
    		$scope.archivedPosts.reset();
    	}
    	
    	var items = "";
    	if (filter.archiveId != null) {
    		items = "/" + filter.archiveId;
    	}
    	
    	filter.offset = filter.pageIndex > 0 ? (filter.initialPagesToLoad - 1 + filter.pageIndex) * filter.pageSize : 0;
    	filter.count = filter.pageIndex > 0 ? filter.pageSize : filter.initialPagesToLoad * filter.pageSize;
    	
    	$scope.ajaxService.loadArchivedPosts(filter).then(function(response) {
    		$scope.archivedPosts.moreItemsAvailable = response.data.payload.length === filter.count;
    		$scope.archivedPosts.append(response.data.payload);
    		$scope.archivedPosts.loading = false;
        }, function() {
        	$scope.archivedPosts.loading = false;
        	$scope.alertService.add("Error occured while loading archived posts.");
        });
    };
    
    $scope.archives = [];
    
	$scope.setArchives = function(response) {
		$scope.archives = response;
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
    $scope.menu.expandedItems = [];
	$scope.menu.homeSelected = true;
	$scope.menu.settingsSelected = false;
	$scope.menu.allItemsSelected = false;
	$scope.menu.archivedItemsSelected = false;
	$scope.menu.archivedItemsExpanded = false;
	
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
	
	$scope.menu.refreshExpansion = function() {
		angular.forEach($scope.groups.items, function(group) {
			group.expanded = $scope.menu.expandedItems.indexOf(group.id) >= 0;
		});
	};
	
	$scope.menu.collapse = function(groupId, $event) {
		var index = $scope.menu.expandedItems.indexOf(groupId);
		if (index >= 0) {
			$scope.menu.expandedItems.splice(index, 1);
		}
		$scope.menu.refreshExpansion();
		$event.stopPropagation();
	};
	
	$scope.menu.expand = function(groupId, $event) {
		var index = $scope.menu.expandedItems.indexOf(groupId);
		if (index < 0) {
			$scope.menu.expandedItems.push(groupId);
		}
		$scope.menu.refreshExpansion();
		$event.stopPropagation();
	};
	
	$scope.menu.collapseArchivedItems = function($event) {
		$scope.menu.archivedItemsExpanded = false;
		$event.stopPropagation();
	};
	
	$scope.menu.expandArchivedItems = function($event) {
		$scope.menu.archivedItemsExpanded = true;
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
