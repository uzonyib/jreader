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
var jReaderApp = angular.module("jReaderApp", ["ngSanitize", "jReaderFilters", "infinite-scroll"]);

jReaderApp.service("viewService", function () {
	this.activeView = {
		type: "home",
		subscriptionGroupId: null,
		subscriptionId: null
	};
	
	this.selectHome = function() {
		var view = {
			type: "home",
			subscriptionGroupId: null,
			subscriptionId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectSettings = function() {
		var view = {
			type: "settings",
			subscriptionGroupId: null,
			subscriptionId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectAllItems = function() {
		var view = {
			type: "allItems",
			subscriptionGroupId: null,
			subscriptionId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectSubscriptionGroup = function(groupId) {
		var view = {
			type: "group",
			subscriptionGroupId: groupId,
			subscriptionId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectSubscription = function(groupId, subscriptionId) {
		var view = {
			type: "subscription",
			subscriptionGroupId: groupId,
			subscriptionId: subscriptionId
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
			subscriptionId: null
		};
		return angular.equals(this.activeView, view);
	};
	
	this.isSubscriptionSelected = function(groupId, subscriptionId) {
		var view = {
			type: "subscription",
			subscriptionGroupId: groupId,
			subscriptionId: subscriptionId
		};
		return angular.equals(this.activeView, view);
	};
	
	this.getSubscriptionGroupId = function() {
		return this.activeView.subscriptionGroupId;
	};
	
	this.getSubscriptionId = function() {
		return this.activeView.subscriptionId;
	};
});

jReaderApp.service("ajaxService", function ($http, $interval) {
	var service = this;

	this.subscriptionGroups = [];
	this.entries = [];
	this.unreadCount = 0;
	
	this.loadingEntries = false;
	this.moreEntriesAvailable = true;
	
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
	
	this.resetEntries = function() {
		this.entries = [];
		this.moreEntriesAvailable = true;
	};
	
	this.refreshSubscriptions = function() {
    	$http.get("/reader/subscriptions").success(function(data) {
        	service.setSubscriptionGroups(data);
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
    	var items = "all";
    	if (filter.subscriptionGroupId != null) {
    		items = "group/" + filter.subscriptionGroupId;
    		if (filter.subscriptionId != null) {
    			items += "/subscription/" + filter.subscriptionId;
    		}
    	}
    	var offset = filter.pageIndex > 0 ? (filter.initialPagesToLoad - 1 + filter.pageIndex) * filter.pageSize : 0;
    	var count = filter.pageIndex > 0 ? filter.pageSize : filter.initialPagesToLoad * filter.pageSize;
    	$http.get("/reader/entries/" + items + "/" + filter.selection + "?offset=" + offset + "&count=" + count + "&ascending=" + filter.ascendingOrder).success(function(data) {
    		service.moreEntriesAvailable = data.length === count;
    		service.loadingEntries = false;
        	service.setEntries(service.entries.concat(data));
        });
    };
    
    this.createPost = function(url, params) {
    	return $http({
			method: "POST",
			url: url,
            data: params,
            headers: {"Content-Type": "application/x-www-form-urlencoded"}
        });
    };
    
    this.post = function(url, params, callback) {
    	this.createPost(url, params).success(function(response) {
        	if (angular.isDefined(callback)) {
        		callback(response);
        	}
        });
    };
    
    this.createGroup = function(title) {
    	this.post("/reader/create-group", "title=" + title, this.setSubscriptionGroups);
	};
	
	this.deleteGroup = function(id) {
		this.post("/reader/delete", "subscriptionGroupId=" + id, this.setSubscriptionGroups);
	};
	
	this.moveGroupUp = function(id) {
		this.post("/reader/move-group-up", "subscriptionGroupId=" + id, this.setSubscriptionGroups);
	};
	
	this.moveGroupDown = function(id) {
		this.post("/reader/move-group-down", "subscriptionGroupId=" + id, this.setSubscriptionGroups);
	};
	
	this.moveSubscriptionUp = function(groupId, subscriptionId) {
		this.post("/reader/move-up", "subscriptionGroupId=" + groupId + "&subscriptionId=" + subscriptionId, this.setSubscriptionGroups);
	};
	
	this.moveSubscriptionDown = function(groupId, subscriptionId) {
		this.post("/reader/move-down", "subscriptionGroupId=" + groupId + "&subscriptionId=" + subscriptionId, this.setSubscriptionGroups);
	};
	
	this.subscribe = function(groupId, url) {
		this.post("/reader/subscribe", "subscriptionGroupId=" + groupId + "&url=" + url, this.setSubscriptionGroups);
	};
	
	this.unsubscribe = function(groupId, subscriptionId) {
		this.post("/reader/unsubscribe", "subscriptionGroupId=" + groupId + "&subscriptionId=" + subscriptionId, this.setSubscriptionGroups);
	};
	
	this.entitleGroup = function(groupId, title) {
		this.post("/reader/entitle-group", "subscriptionGroupId=" + groupId + "&title=" + title, this.setSubscriptionGroups);
	};
	
	this.entitleSubscription = function(groupId, subscriptionId, title) {
		this.post("/reader/entitle", "subscriptionGroupId=" + groupId + "&subscriptionId=" + subscriptionId + "&title=" + title, this.setSubscriptionGroups);
	};
	
	this.markRead = function(entry) {
		angular.forEach(service.entries, function(e) {
			if (entry.id === e.id) {
				e.read = true;
			}
		});
		this.post("/reader/read", "ids=" + entry.id + "&subscriptionIds=" + entry.subscriptionId + "&subscriptionGroupIds=" + entry.subscriptionGroupId, this.setSubscriptionGroups);
	};
	
	this.markAllRead = function(entries, filter) {
		if (!angular.isDefined(entries) || !angular.isArray(entries) || entries.length == 0) {
			return;
		}
		this.loadingEntries = true;
		var ids = "";
		var subscriptionIds = "";
		var subscriptionGroupIds = "";
		angular.forEach(entries, function(entry) {
			ids += "," + entry.id;
			subscriptionIds += "," + entry.subscriptionId;
			subscriptionGroupIds += "," + entry.subscriptionGroupId;
		});
		this.createPost("/reader/read", "ids=" + ids.substring(1) + "&subscriptionIds=" + subscriptionIds.substring(1) + "&subscriptionGroupIds=" + subscriptionGroupIds.substring(1)).success(function(response) {
			service.setSubscriptionGroups(response);
			service.loadingEntries = false;
			service.loadEntries(filter);
        });
	};
	
	this.star = function(entry) {
		this.post("/reader/star", "id=" + entry.id + "&subscriptionId=" + entry.subscriptionId + "&subscriptionGroupId=" + entry.subscriptionGroupId);
	};
	
	this.unstar = function(entry) {
		this.post("/reader/unstar", "id=" + entry.id + "&subscriptionId=" + entry.subscriptionId + "&subscriptionGroupId=" + entry.subscriptionGroupId);
	};
    
    $interval(this.refreshSubscriptions, 1000 * 60 * 5);
    this.refreshSubscriptions();
});

jReaderApp.controller("HeadCtrl", function ($scope, ajaxService) {
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
});
 
jReaderApp.controller("MenuCtrl", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;

	$scope.uncollapsedItems = [];
	
	$scope.home = {};
	$scope.settings = {};
	$scope.allItems = {};
	
	$scope.home.selected = true;
	$scope.settings.selected = false;
	$scope.allItems.selected = false;
	
	$scope.subscriptionGroups = [];
	$scope.unreadCount = 0;
	
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
	
	$scope.refreshSelection = function() {
		$scope.home.selected = $scope.viewService.isHomeSelected();
		$scope.settings.selected = $scope.viewService.isSettingsSelected();
		$scope.allItems.selected = $scope.viewService.isAllItemsSelected();
		
		angular.forEach($scope.subscriptionGroups, function(group) {
			group.selected = $scope.viewService.isSubscriptionGroupSelected(group.id);
			angular.forEach(group.subscriptions, function(subscription) {
				subscription.selected = $scope.viewService.isSubscriptionSelected(group.id, subscription.id);
			});
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
	
});

jReaderApp.controller("HomeCtrl", function ($scope, ajaxService, viewService) {
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
});

jReaderApp.controller("SettingsCtrl", function ($scope, $http, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.subscriptionGroups = [];
	
	$scope.newGroupTitle = "";
	
	$scope.newSubscription = {};
	$scope.newSubscription.group = {};
	$scope.newSubscription.url = "";
	
	$scope.exportImportJson = "";
	
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
	
	$scope.createGroup = function() {
		$scope.ajaxService.createGroup($scope.newGroupTitle);
		$scope.newGroupTitle = "";
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
});

jReaderApp.controller("EntriesCtrl", function ($scope, $element, $window, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.subscriptionGroups = [];
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

		$scope.active = !$scope.viewService.isHomeSelected() && !$scope.viewService.isSettingsSelected();
		if ($scope.active) {
			$scope.refreshEntries();
		}
	});
	
	$scope.$watch("ajaxService.subscriptionGroups", function(subscriptionGroups) {
		$scope.subscriptionGroups = angular.copy(subscriptionGroups);
	});
	
	$scope.$watch("ajaxService.entries", function(entries) {
		$scope.entries = angular.copy(entries);
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
	
});
