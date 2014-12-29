angular.module("jReaderApp").service("ajaxService", ["$http", "$interval", function ($http, $interval) {
	var service = this;

	this.subscriptionGroups = [];
	this.unreadCount = 0;
	
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
	
	this.refreshSubscriptions = function() {
    	$http.get("/reader/groups").success(function(data) {
        	service.setSubscriptionGroups(data);
        });
    };
    
    this.refreshArchives = function() {
    	return $http.get("/reader/archives");
    };
    
    this.loadEntries = function(filter) {
    	var items = "";
    	if (filter.subscriptionGroupId != null) {
    		items = "/groups/" + filter.subscriptionGroupId;
    		if (filter.subscriptionId != null) {
    			items += "/subscriptions/" + filter.subscriptionId;
    		}
    	}
    	items += "/entries";
    	
    	return $http({
    		method: "GET",
    		url: "/reader" + items + "/" + filter.selection,
    		params: {
    			"offset": filter.offset,
    			"count": filter.count,
    			"ascending": filter.ascendingOrder
    		}
        });
    };
    
    this.loadArchivedEntries = function(filter) {
    	var items = "";
    	if (filter.archiveId != null) {
    		items = "/" + filter.archiveId;
    	}
    	
    	return $http({
    		method: "GET",
    		url: "/reader/archives" + items + "/entries",
    		params: {
    			"offset": filter.offset,
    			"count": filter.count,
    			"ascending": filter.ascendingOrder
    		}
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
    	return $http({
			method: "POST",
			url: "/reader/archives",
            params: { "title": title }
        });
	};
	
	this.deleteArchive = function(id) {
		return $http({
			method: "DELETE",
			url: "/reader/archives/" + id
        });
	};
	
	this.entitleArchive = function(archiveId, title) {
		return $http({
			method: "PUT",
			url: "/reader/archives/" + archiveId + "/title",
            params: { "value": title }
        });
	};
	
	this.moveArchive = function(id, up) {
		return $http({
			method: "PUT",
			url: "/reader/archives/" + id + "/order",
            params: { "up": up }
        });
	};
	
	this.moveArchiveUp = function(id) {
		return service.moveArchive(id, true);
	};
	
	this.moveArchiveDown = function(id) {
		return service.moveArchive(id, false);
	};
	
	this.markRead = function(entry) {
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

		var promise = $http({
			method: "POST",
			url: "/reader/entries",
            data: map,
            headers: { "Content-Type": "application/json" }
        });
		promise.success(function(response) {
        	service.setSubscriptionGroups(response);
        });
		return promise;
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
    
}]);
