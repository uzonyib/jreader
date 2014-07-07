angular.module("jReaderApp").service("ajaxService", ["$http", "$interval", function ($http, $interval) {
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
    	$http.get("/reader/groups").success(function(data) {
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
    
    $interval(this.refreshSubscriptions, 1000 * 60 * 5);
    this.refreshSubscriptions();
}]);
