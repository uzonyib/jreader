angular.module("jReaderFilters", []).filter("moment", function() {
	return function (date) {
		var m = moment(date);
		var duration = moment.duration(m.diff(moment()));
		if (duration.asDays() <= -1) {
			return m.format("YYYY-MM-DD HH:mm");
		}
		return duration.humanize(true);
	};
});

angular.module("jReaderApp", ["ngSanitize", "jReaderFilters", "infinite-scroll"]);

angular.module("jReaderApp").service("viewService", function () {
	this.activeView = {
		type: "home",
		subscriptionGroupId: null,
		subscriptionId: null,
		archiveId: null
	};
	
	this.selectHome = function() {
		var view = {
			type: "home",
			subscriptionGroupId: null,
			subscriptionId: null,
			archiveId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectSettings = function() {
		var view = {
			type: "settings",
			subscriptionGroupId: null,
			subscriptionId: null,
			archiveId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectAllItems = function() {
		var view = {
			type: "allItems",
			subscriptionGroupId: null,
			subscriptionId: null,
			archiveId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectSubscriptionGroup = function(groupId) {
		var view = {
			type: "group",
			subscriptionGroupId: groupId,
			subscriptionId: null,
			archiveId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectSubscription = function(groupId, subscriptionId) {
		var view = {
			type: "subscription",
			subscriptionGroupId: groupId,
			subscriptionId: subscriptionId,
			archiveId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectArchivedItems = function() {
		var view = {
			type: "archivedItems",
			subscriptionGroupId: null,
			subscriptionId: null,
			archiveId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectArchive = function(archiveId) {
		var view = {
			type: "archive",
			subscriptionGroupId: null,
			subscriptionId: null,
			archiveId: archiveId
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.isHomeSelected = function() {
		return angular.equals(this.activeView.type, "home");
	};
	
	this.isSettingsSelected = function() {
		return angular.equals(this.activeView.type, "settings");
	};
	
	this.isAllItemsSelected = function() {
		return angular.equals(this.activeView.type, "allItems");
	};
	
	this.isSubscriptionGroupSelected = function(groupId) {
		var view = {
			type: "group",
			subscriptionGroupId: groupId,
			subscriptionId: null,
			archiveId: null
		};
		return angular.equals(this.activeView, view);
	};
	
	this.isSubscriptionSelected = function(groupId, subscriptionId) {
		var view = {
			type: "subscription",
			subscriptionGroupId: groupId,
			subscriptionId: subscriptionId,
			archiveId: null
		};
		return angular.equals(this.activeView, view);
	};
	
	this.isArchivedItemsSelected = function() {
		return angular.equals(this.activeView.type, "archivedItems");
	};
	
	this.isArchiveSelected = function(archiveId) {
		var view = {
			type: "archive",
			subscriptionGroupId: null,
			subscriptionId: null,
			archiveId: archiveId
		};
		return angular.equals(this.activeView, view);
	};
	
	this.isEntriesSelected = function() {
		return angular.equals(this.activeView.type, "allItems") || angular.equals(this.activeView.type, "group") || angular.equals(this.activeView.type, "subscription");
	};
	
	this.isArchivesSelected = function() {
		return angular.equals(this.activeView.type, "archivedItems") || angular.equals(this.activeView.type, "archive");
	};
	
	this.getSubscriptionGroupId = function() {
		return this.activeView.subscriptionGroupId;
	};
	
	this.getSubscriptionId = function() {
		return this.activeView.subscriptionId;
	};
	
	this.getArchiveId = function() {
		return this.activeView.archiveId;
	};
	
});

angular.module("jReaderApp").service("ajaxService", ["$http", "$interval", function ($http, $interval) {
	var service = this;

	this.subscriptionGroups = [];
	this.entries = [];
	this.archives = [];
	this.archivedEntries = [];
	this.unreadCount = 0;
	
	this.loadingEntries = false;
	this.moreEntriesAvailable = true;
	this.loadingArchivedEntries = false;
	this.moreArchivedEntriesAvailable = true;
	
	this.setSubscriptionGroups = function(groups) {
		if (!angular.equals(this.subscriptionGroups, groups)) {
			service.subscriptionGroups = groups;
			var count = 0;
			angular.forEach(service.subscriptionGroups, function(group) {
				count += group.unreadCount;
			});
			service.unreadCount = count;
		}
	};
	
	this.setEntries = function(newEntries) {
		if (!angular.equals(this.entries, newEntries)) {
			this.entries = newEntries;
		}
	};
	
	this.setArchives = function(as) {
		if (!angular.equals(this.archives, as)) {
			service.archives = as;
		}
	};
	
	this.setArchivedEntries = function(newEntries) {
		if (!angular.equals(this.archivedEntries, newEntries)) {
			this.archivedEntries = newEntries;
		}
	};
	
	this.resetEntries = function() {
		this.entries = [];
		this.moreEntriesAvailable = true;
	};
	
	this.resetArchivedEntries = function() {
		this.archivedEntries = [];
		this.moreArchivedEntriesAvailable = true;
	};
	
	this.refreshSubscriptions = function() {
    	$http.get("/reader/groups").success(function(data) {
        	service.setSubscriptionGroups(data);
        });
    };
    
    this.refreshArchives = function() {
    	$http.get("/reader/archives").success(function(data) {
        	service.setArchives(data);
        });
    };
    
    this.loadEntries = function(filter) {
    	if (this.loadingEntries) {
    		return;
    	}
    	this.loadingEntries = true;
    	if (filter.pageIndex === 0) {
    		this.resetEntries();
    	}
    	
    	var items = "";
    	if (filter.subscriptionGroupId != null) {
    		items = "/groups/" + filter.subscriptionGroupId;
    		if (filter.subscriptionId != null) {
    			items += "/subscriptions/" + filter.subscriptionId;
    		}
    	}
    	items += "/entries";
    	
    	var offset = filter.pageIndex > 0 ? (filter.initialPagesToLoad - 1 + filter.pageIndex) * filter.pageSize : 0;
    	var count = filter.pageIndex > 0 ? filter.pageSize : filter.initialPagesToLoad * filter.pageSize;
    	
    	$http({
    		method: "GET",
    		url: "/reader" + items + "/" + filter.selection,
    		params: {
    			"offset": offset,
    			"count": count,
    			"ascending": filter.ascendingOrder
    		}
    	}).success(function(data) {
    		service.moreEntriesAvailable = data.length === count;
    		service.loadingEntries = false;
        	service.setEntries(service.entries.concat(data));
        });
    };
    
    this.loadArchivedEntries = function(filter) {
    	if (this.loadingArchivedEntries) {
    		return;
    	}
    	this.loadingArchivedEntries = true;
    	if (filter.pageIndex === 0) {
    		this.resetArchivedEntries();
    	}
    	
    	var items = "";
    	if (filter.archiveId != null) {
    		items = "/" + filter.archiveId;
    	}
    	
    	var offset = filter.pageIndex > 0 ? (filter.initialPagesToLoad - 1 + filter.pageIndex) * filter.pageSize : 0;
    	var count = filter.pageIndex > 0 ? filter.pageSize : filter.initialPagesToLoad * filter.pageSize;
    	
    	$http({
    		method: "GET",
    		url: "/reader/archives" + items + "/entries",
    		params: {
    			"offset": offset,
    			"count": count,
    			"ascending": filter.ascendingOrder
    		}
    	}).success(function(data) {
    		service.moreArchivedEntriesAvailable = data.length === count;
    		service.loadingArchivedEntries = false;
        	service.setArchivedEntries(service.archivedEntries.concat(data));
        });
    };
    
    this.createGroup = function(title) {
    	$http({
			method: "POST",
			url: "/reader/groups",
            params: { "title": title }
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        });
	};
	
	this.deleteGroup = function(id) {
		$http({
			method: "DELETE",
			url: "/reader/groups/" + id
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        });
	};
	
	this.entitleGroup = function(groupId, title) {
		$http({
			method: "PUT",
			url: "/reader/groups/" + groupId + "/title",
            params: { "value": title }
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        });
	};
	
	this.moveGroup = function(id, up) {
		$http({
			method: "PUT",
			url: "/reader/groups/" + id + "/order",
            params: { "up": up }
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        });
	};
	
	this.moveGroupUp = function(id) {
		service.moveGroup(id, true);
	};
	
	this.moveGroupDown = function(id) {
		service.moveGroup(id, false);
	};
	
	this.subscribe = function(groupId, url) {
		$http({
			method: "POST",
			url: "/reader/groups/" + groupId + "/subscriptions",
            params: { "url": url }
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        });
	};
	
	this.unsubscribe = function(groupId, subscriptionId) {
		$http({
			method: "DELETE",
			url: "/reader/groups/" + groupId + "/subscriptions/" + subscriptionId
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        });
	};
	
	this.entitleSubscription = function(groupId, subscriptionId, title) {
		$http({
			method: "PUT",
			url: "/reader/groups/" + groupId + "/subscriptions/" + subscriptionId + "/title",
            params: { "value": title }
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        });
	};
	
	this.moveSubscription = function(groupId, subscriptionId, up) {
		$http({
			method: "PUT",
			url: "/reader/groups/" + groupId + "/subscriptions/" + subscriptionId + "/order",
            params: { "up": up }
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        });
	};
	
	this.moveSubscriptionUp = function(groupId, subscriptionId) {
		service.moveSubscription(groupId, subscriptionId, true);
	};
	
	this.moveSubscriptionDown = function(groupId, subscriptionId) {
		service.moveSubscription(groupId, subscriptionId, false);
	};
	
	this.createArchive = function(title) {
    	$http({
			method: "POST",
			url: "/reader/archives",
            params: { "title": title }
        }).success(function(response) {
        	service.setArchives(response);
        });
	};
	
	this.deleteArchive = function(id) {
		$http({
			method: "DELETE",
			url: "/reader/archives/" + id
        }).success(function(response) {
        	service.setArchives(response);
        });
	};
	
	this.entitleArchive = function(archiveId, title) {
		$http({
			method: "PUT",
			url: "/reader/archives/" + archiveId + "/title",
            params: { "value": title }
        }).success(function(response) {
        	service.setArchives(response);
        });
	};
	
	this.moveArchive = function(id, up) {
		$http({
			method: "PUT",
			url: "/reader/archives/" + id + "/order",
            params: { "up": up }
        }).success(function(response) {
        	service.setArchives(response);
        });
	};
	
	this.moveArchiveUp = function(id) {
		service.moveArchive(id, true);
	};
	
	this.moveArchiveDown = function(id) {
		service.moveArchive(id, false);
	};
	
	this.markRead = function(entry) {
		angular.forEach(service.entries, function(e) {
			if (entry.id === e.id) {
				e.read = true;
			}
		});
		
		var map = {};
		map[entry.subscriptionGroupId] = {};
		map[entry.subscriptionGroupId][entry.subscriptionId] = [entry.id];
		
		$http({
			method: "POST",
			url: "/reader/entries",
            data: map,
            headers: { "Content-Type": "application/json" }
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        });
	};
	
	this.markAllRead = function(entries, filter) {
		if (!angular.isDefined(entries) || !angular.isArray(entries) || entries.length == 0) {
			return;
		}
		this.loadingEntries = true;
		var map = {};
		angular.forEach(entries, function(entry) {
			if (!angular.isDefined(map[entry.subscriptionGroupId])) {
				map[entry.subscriptionGroupId] = {};
			}
			if (!angular.isDefined(map[entry.subscriptionGroupId][entry.subscriptionId])) {
				map[entry.subscriptionGroupId][entry.subscriptionId] = [];
			}
			map[entry.subscriptionGroupId][entry.subscriptionId].push(entry.id);
		});

		$http({
			method: "POST",
			url: "/reader/entries",
            data: map,
            headers: { "Content-Type": "application/json" }
        }).success(function(response) {
        	service.setSubscriptionGroups(response);
        	service.loadingEntries = false;
			service.loadEntries(filter);
        });
	};
	
	this.setStarred = function(entry, starred) {
		$http({
			method: "PUT",
			url: "/reader/groups/" + entry.subscriptionGroupId + "/subscriptions/" + entry.subscriptionId + "/entries/" + entry.id + "/starred",
            params: { "value": starred }
        });
	};
	
	this.star = function(entry) {
		service.setStarred(entry, true);
	};
	
	this.unstar = function(entry) {
		service.setStarred(entry, false);
	};
	
	this.archive = function(entry, archive) {
		$http({
			method: "POST",
			url: "/reader/archives/" + archive.id + "/entries",
            params: {
            	"groupId": entry.subscriptionGroupId,
            	"subscriptionId": entry.subscriptionId,
            	"entryId": entry.id,
            },
            headers: { "Content-Type": "application/json" }
        });
	};
	
	this.deleteArchivedEntry = function(archiveId, entryId) {
		return $http({
			method: "DELETE",
			url: "/reader/archives/" + archiveId + "/entries/" + entryId
        });
	};
    
    $interval(this.refreshSubscriptions, 1000 * 60 * 5);
    this.refreshSubscriptions();
    this.refreshArchives();
}]);

angular.module("jReaderApp").controller("HeadCtrl", ["$scope", "ajaxService", function ($scope, ajaxService) {
	$scope.ajaxService = ajaxService;

	$scope.titlePrefix = "jReader";
	$scope.title = $scope.titlePrefix;
	
	$scope.$watch("ajaxService.unreadCount", function(count) {
		if (count > 0) {
			$scope.title = $scope.titlePrefix + " (" + count + ")";
		} else {
			$scope.title = $scope.titlePrefix;
		}
	});
}]);

angular.module("jReaderApp").controller("MenuCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;

	$scope.uncollapsedItems = [];
	
	$scope.home = {};
	$scope.settings = {};
	$scope.allItems = {};
	$scope.archivedItems = {};
	
	$scope.home.selected = true;
	$scope.settings.selected = false;
	$scope.allItems.selected = false;
	$scope.archivedItems.selected = false;
	$scope.archivedItems.collapsed = true;
	
	$scope.subscriptionGroups = [];
	$scope.unreadCount = 0;
	$scope.archives = [];
	
	$scope.$watch("viewService.activeView", function() {
		$scope.refreshSelection();
	});
	
	$scope.$watch("ajaxService.subscriptionGroups", function(subscriptionGroups) {
		$scope.subscriptionGroups = angular.copy(subscriptionGroups);
		$scope.refreshSelection();
		$scope.refreshCollapsion();
	});
	
	$scope.$watch("ajaxService.unreadCount", function(count) {
		$scope.unreadCount = count;
	});
	
	$scope.$watch("ajaxService.archives", function(archives) {
		$scope.archives = angular.copy(archives);
	});
	
	$scope.refreshSelection = function() {
		$scope.home.selected = $scope.viewService.isHomeSelected();
		$scope.settings.selected = $scope.viewService.isSettingsSelected();
		$scope.allItems.selected = $scope.viewService.isAllItemsSelected();
		$scope.archivedItems.selected = $scope.viewService.isArchivedItemsSelected();
		
		angular.forEach($scope.subscriptionGroups, function(group) {
			group.selected = $scope.viewService.isSubscriptionGroupSelected(group.id);
			angular.forEach(group.subscriptions, function(subscription) {
				subscription.selected = $scope.viewService.isSubscriptionSelected(group.id, subscription.id);
			});
		});
		
		angular.forEach($scope.archives, function(archive) {
			archive.selected = $scope.viewService.isArchiveSelected(archive.id);
		});
	};
	
	$scope.refreshCollapsion = function() {
		angular.forEach($scope.subscriptionGroups, function(group) {
			group.collapsed = $scope.uncollapsedItems.indexOf(group.id) < 0;
		});
	};
	
	$scope.collapse = function(groupId, $event) {
		var index = $scope.uncollapsedItems.indexOf(groupId);
		if (index >= 0) {
			$scope.uncollapsedItems.splice(index, 1);
		}
		$scope.refreshCollapsion();
		$event.stopPropagation();
	};
	
	$scope.uncollapse = function(groupId, $event) {
		var index = $scope.uncollapsedItems.indexOf(groupId);
		if (index < 0) {
			$scope.uncollapsedItems.push(groupId);
		}
		$scope.refreshCollapsion();
		$event.stopPropagation();
	};
	
	$scope.selectHome = function() {
		$scope.viewService.selectHome();
	};
	
	$scope.selectSettings = function() {
		$scope.viewService.selectSettings();
	};
	
	$scope.selectAllItems = function() {
		$scope.viewService.selectAllItems();
	};
	
	$scope.selectSubscriptionGroup = function(group) {
		$scope.viewService.selectSubscriptionGroup(group.id);
	};
	
	$scope.selectSubscription = function(group, subscription) {
		$scope.viewService.selectSubscription(group.id, subscription.id);
	};
	
	$scope.selectArchivedItems = function() {
		$scope.viewService.selectArchivedItems();
	};
	
	$scope.selectArchive = function(archive) {
		$scope.viewService.selectArchive(archive.id);
	};
	
	$scope.collapseArchivedItems = function($event) {
		$scope.archivedItems.collapsed = true;
		$event.stopPropagation();
	};
	
	$scope.uncollapseArchivedItems = function($event) {
		$scope.archivedItems.collapsed = false;
		$event.stopPropagation();
	};
	
}]);

angular.module("jReaderApp").controller("HomeCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = true;
	$scope.subscriptionGroups = [];
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isHomeSelected();
	});
	
	$scope.$watch("ajaxService.subscriptionGroups", function(subscriptionGroups) {
		$scope.subscriptionGroups = angular.copy(subscriptionGroups);
	});
}]);

angular.module("jReaderApp").controller("SettingsCtrl", ["$scope", "$http", "ajaxService", "viewService", function ($scope, $http, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.subscriptionGroups = [];
	$scope.archives = [];
	
	$scope.newGroupTitle = "";
	
	$scope.newSubscription = {};
	$scope.newSubscription.group = {};
	$scope.newSubscription.url = "";
	
	$scope.newArchiveTitle = "";
	
	$scope.exportImportJson = "";
	$scope.importLog = "";
	$scope.importErrors = 0;
	$scope.showImportLog = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isSettingsSelected();
	});
	
	$scope.$watch("ajaxService.subscriptionGroups", function(subscriptionGroups) {
		$scope.subscriptionGroups = angular.copy(subscriptionGroups);
		$scope.newSubscription.group = $scope.subscriptionGroups[0];
		angular.forEach($scope.subscriptionGroups, function(group) {
			group.editingTitle = false;
			group.newTitle = group.title;
			angular.forEach(group.subscriptions, function(subscription) {
				subscription.editingTitle = false;
				subscription.newTitle = subscription.title;
			});
		});
	});
	
	$scope.$watch("ajaxService.archives", function(archives) {
		$scope.archives = angular.copy(archives);
		angular.forEach($scope.archives, function(archive) {
			archive.editingTitle = false;
			archive.newTitle = archive.title;
		});
	});
	
	$scope.createGroup = function() {
		$scope.ajaxService.createGroup($scope.newGroupTitle);
		$scope.newGroupTitle = "";
	};
	
	$scope.deleteGroup = function(subscriptionGroupId) {
		$scope.ajaxService.deleteGroup(subscriptionGroupId);
	};
	
	$scope.moveGroupUp = function(subscriptionGroupId) {
		$scope.ajaxService.moveGroupUp(subscriptionGroupId);
	};
	
	$scope.moveGroupDown = function(subscriptionGroupId) {
		$scope.ajaxService.moveGroupDown(subscriptionGroupId);
	};
	
	$scope.moveSubscriptionUp = function(subscriptionGroupId, subscriptionId) {
		$scope.ajaxService.moveSubscriptionUp(subscriptionGroupId, subscriptionId);
	};
	
	$scope.moveSubscriptionDown = function(subscriptionGroupId, subscriptionId) {
		$scope.ajaxService.moveSubscriptionDown(subscriptionGroupId, subscriptionId);
	};
	
	$scope.subscribe = function() {
		$scope.ajaxService.subscribe($scope.newSubscription.group.id, $scope.newSubscription.url);
	};
	
	$scope.unsubscribe = function(subscriptionGroupId, subscriptionId) {
		$scope.ajaxService.unsubscribe(subscriptionGroupId, subscriptionId);
	};
	
	$scope.editTitle = function(groupOrSubscription) {
		groupOrSubscription.editingTitle = true;
	};
	
	$scope.entitleGroup = function(group) {
		$scope.ajaxService.entitleGroup(group.id, group.newTitle);
	};
	
	$scope.entitleSubscription = function(group, subscription) {
		$scope.ajaxService.entitleSubscription(group.id, subscription.id, subscription.newTitle);
	};
	
	$scope.createArchive = function() {
		$scope.ajaxService.createArchive($scope.newArchiveTitle);
		$scope.newArchiveTitle = "";
	};
	
	$scope.deleteArchive = function(archiveId) {
		$scope.ajaxService.deleteArchive(archiveId);
	};
	
	$scope.moveArchiveUp = function(archiveId) {
		$scope.ajaxService.moveArchiveUp(archiveId);
	};
	
	$scope.moveArchiveDown = function(archiveId) {
		$scope.ajaxService.moveArchiveDown(archiveId);
	};
	
	$scope.entitleArchive = function(archive) {
		$scope.ajaxService.entitleArchive(archive.id, archive.newTitle);
	};
	
	$scope.exportSubscriptions = function() {
		var result = [];
		angular.forEach($scope.subscriptionGroups, function(group) {
			var groupCopy = {};
			groupCopy.title = group.title;
			groupCopy.subscriptions = [];
			angular.forEach(group.subscriptions, function(subscription) {
				var subscriptionCopy = {};
				subscriptionCopy.title = subscription.title;
				subscriptionCopy.url = subscription.feed.url;
				groupCopy.subscriptions.push(subscriptionCopy);
			});
			result.push(groupCopy);
		});
		$scope.exportImportJson = angular.toJson(result, true);
	};
	
	$scope.importSubscriptions = function() {
		var jobQueue = [];
		angular.forEach(angular.fromJson($scope.exportImportJson), function(group) {
			group.type = 1;
			jobQueue.unshift(group);
		});
		
		$scope.showImportLog = true;
		$scope.importLog = "Import started, please wait...\n";
		$scope.importErrors = 0;
		
		var findGroup = function(title) {
			var id = undefined;
			angular.forEach($scope.subscriptionGroups, function(group) {
				if (group.title === title) {
					id = group.id;
				}
			});
			return id;
		};
		
		var findSubscription = function(url) {
			var id = undefined;
			angular.forEach($scope.subscriptionGroups, function(group) {
				angular.forEach(group.subscriptions, function(subscription) {
					if (subscription.feed.url === url) {
						id = subscription.id;
					}
				});
			});
			return id;
		};
		
		var addSubscribeJobs = function(group, id, jobQueue) {
			var q = [];
			angular.forEach(group.subscriptions, function(subscription) {
				subscription.type = 2;
				subscription.groupId = id;
				q.unshift(subscription);
			});
			angular.forEach(q, function(e) {
				jobQueue.push(e);
			});
		};
		
		var addEntitleJobs = function(subscription, jobQueue) {
			subscription.type = 3;
			jobQueue.push(subscription);
		};
		
        var process = function(job) {
            if (angular.isUndefined(job)) {
            	$scope.importLog += "\nImport completed with " + $scope.importErrors + " error(s).\n";
            	$scope.ajaxService.setSubscriptionGroups($scope.subscriptionGroups);
            } else if (job.type === 1) {
    			var id = findGroup(job.title);
    			if (angular.isDefined(id)) {
    				$scope.importLog += "Group \"" + job.title + "\" already exists.\n";
    				addSubscribeJobs(job, id, jobQueue);
    				process(jobQueue.pop());
    			} else {
    				$scope.importLog += "Creating group \"" + job.title + "\"...";
    				$http({
    					method: "POST",
    					url: "/reader/groups",
    					params: { "title": job.title }
    				}).success(function(response) {
    					$scope.importLog += " OK.\n";
    					$scope.subscriptionGroups = response;
    					addSubscribeJobs(job, findGroup(job.title), jobQueue);
        				process(jobQueue.pop());
    				}).error(function(response) {
    					$scope.importLog += " ERROR.\n";
    					++$scope.importErrors;
        				process(jobQueue.pop());
    				});
    			}
            } else if (job.type === 2) {
            	var id = findSubscription(job.url);
            	if (angular.isDefined(id)) {
    				$scope.importLog += "Already subscribed to " + job.url + ".\n";
    				process(jobQueue.pop());
    			} else {
    				$scope.importLog += "Subscribing to " + job.url + "...";
    				$http({
    					method: "POST",
    					url: "/reader/groups/" + job.groupId + "/subscriptions",
    					params: { "url": job.url }
    				}).success(function(response) {
    					$scope.importLog += " OK.\n";
    					$scope.subscriptionGroups = response;
    					addEntitleJobs(job, jobQueue);
    					process(jobQueue.pop());
    				}).error(function(response) {
    					$scope.importLog += " ERROR.\n";
    					++$scope.importErrors;
        				process(jobQueue.pop());
    				});
    			}
            } else {
            	$scope.importLog += "Setting title \"" + job.title + "\"...";
            	var id = findSubscription(job.url);
            	$http({
        			method: "PUT",
        			url: "/reader/groups/" + job.groupId + "/subscriptions/" + id + "/title",
                    params: { "value": job.title }
                }).success(function(response) {
		        	$scope.importLog += " OK.\n";
		        	$scope.subscriptionGroups = response;
					process(jobQueue.pop());
		        }).error(function(response) {
					$scope.importLog += " ERROR.\n";
					++$scope.importErrors;
    				process(jobQueue.pop());
				});
            }
        };
        process(jobQueue.pop());
	};
	
}]);

angular.module("jReaderApp").controller("EntriesCtrl", ["$scope", "$window", "ajaxService", "viewService", function ($scope, $window, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.subscriptionGroups = [];
	$scope.archives = [];
	$scope.entries = [];
	
	$scope.filter = {};
	$scope.filter.subscriptionGroupId = null;
	$scope.filter.subscriptionId = null;
	$scope.filter.selection = "unread";
	$scope.filter.pageIndex = 0;
	$scope.filter.ascendingOrder = true;
	$scope.filter.pageSize = Math.ceil($window.innerHeight / 29 / 10) * 10;
	$scope.filter.initialPagesToLoad = 2;
	
	$scope.loading = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.filter.subscriptionGroupId = $scope.viewService.getSubscriptionGroupId();
		$scope.filter.subscriptionId = $scope.viewService.getSubscriptionId();

		$scope.active = $scope.viewService.isEntriesSelected();
		if ($scope.active) {
			$scope.refreshEntries();
		}
	});
	
	$scope.$watch("ajaxService.subscriptionGroups", function(subscriptionGroups) {
		$scope.subscriptionGroups = angular.copy(subscriptionGroups);
	});
	
	$scope.$watch("ajaxService.archives", function(archives) {
		$scope.archives = angular.copy(archives);
	});
	
	$scope.$watch("ajaxService.entries", function(entries) {
		$scope.entries = angular.copy(entries);
		angular.forEach($scope.entries, function(entry) {
			entry.archived = false;
			entry.archive = $scope.archives[0];
		});
	});
	
	$scope.$watch("ajaxService.loadingEntries", function(loadingEntries) {
		$scope.loading = loadingEntries;
	});
	
	$scope.loadEntries = function() {
		$scope.ajaxService.loadEntries($scope.filter);
	};
	
	$scope.markAllRead = function() {
		var unreads = [];
		angular.forEach($scope.entries, function(entry) {
			if (!entry.read) {
				entry.read = true;
				unreads.push(entry);
			}
		});
		$scope.filter.pageIndex = 0;
		$scope.ajaxService.markAllRead(unreads, $scope.filter);
	};
	
	$scope.refreshEntries = function() {
		$scope.filter.pageIndex = 0;
		$scope.loadEntries();
	};
	
	$scope.toggleCollapsion = function(entry) {
		entry.uncollapsed = !entry.uncollapsed;
		if (!entry.read) {
			entry.read = true;
			$scope.ajaxService.markRead(entry);
		}
	};
	
	$scope.setAscendingOrder = function(ascending) {
		if (!angular.equals($scope.filter.ascendingOrder, ascending)) {
			$scope.filter.ascendingOrder = ascending;
			$scope.refreshEntries();
		}
	};
	
	$scope.setSelection = function(s) {
		if (!angular.equals($scope.filter.selection, s)) {
			$scope.filter.selection = s;
			$scope.refreshEntries();
		}
	};
	
	$scope.loadMoreEntries = function() {
		if ($scope.ajaxService.moreEntriesAvailable) {
			++$scope.filter.pageIndex;
			$scope.loadEntries();
		}
	};
	
	$scope.star = function(entry) {
		entry.starred = true;
		$scope.ajaxService.star(entry);
	};
	
	$scope.unstar = function(entry) {
		entry.starred = false;
		$scope.ajaxService.unstar(entry);
	};
	
	$scope.archive = function(entry) {
		console.log(entry.archive.title);
		entry.archived = true;
		$scope.ajaxService.archive(entry, entry.archive);
	};
	
}]);

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

