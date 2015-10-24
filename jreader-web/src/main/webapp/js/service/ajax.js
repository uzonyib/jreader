angular.module("jReaderApp").service("ajaxService", ["$http", function ($http) {
	var service = this;

	this.refreshSubscriptions = function() {
    	return $http.get("/reader/groups");
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
    	
    	var params = {
			"offset": filter.offset,
			"count": filter.count,
			"ascending": filter.ascendingOrder
		};
    	
    	return $http({
    		method: "GET",
    		url: "/reader" + items + "/" + filter.selection,
    		params: params
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
    	return $http({
			method: "POST",
			url: "/reader/groups",
            params: { "title": title }
        });
	};
	
	this.deleteGroup = function(id) {
		return $http({
			method: "DELETE",
			url: "/reader/groups/" + id
        });
	};
	
	this.entitleGroup = function(groupId, title) {
		return $http({
			method: "PUT",
			url: "/reader/groups/" + groupId + "/title",
            params: { "value": title }
        });
	};
	
	this.moveGroup = function(id, up) {
		return $http({
			method: "PUT",
			url: "/reader/groups/" + id + "/order",
            params: { "up": up }
        });
	};
	
	this.moveGroupUp = function(id) {
		return service.moveGroup(id, true);
	};
	
	this.moveGroupDown = function(id) {
		return service.moveGroup(id, false);
	};
	
	this.subscribe = function(groupId, url) {
		return $http({
			method: "POST",
			url: "/reader/groups/" + groupId + "/subscriptions",
            params: { "url": url }
        });
	};
	
	this.unsubscribe = function(groupId, subscriptionId) {
		return $http({
			method: "DELETE",
			url: "/reader/groups/" + groupId + "/subscriptions/" + subscriptionId
        });
	};
	
	this.entitleSubscription = function(groupId, subscriptionId, title) {
		return $http({
			method: "PUT",
			url: "/reader/groups/" + groupId + "/subscriptions/" + subscriptionId + "/title",
            params: { "value": title }
        });
	};
	
	this.moveSubscription = function(groupId, subscriptionId, up) {
		return $http({
			method: "PUT",
			url: "/reader/groups/" + groupId + "/subscriptions/" + subscriptionId + "/order",
            params: { "up": up }
        });
	};
	
	this.moveSubscriptionUp = function(groupId, subscriptionId) {
		return service.moveSubscription(groupId, subscriptionId, true);
	};
	
	this.moveSubscriptionDown = function(groupId, subscriptionId) {
		return service.moveSubscription(groupId, subscriptionId, false);
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
		
		return $http({
			method: "POST",
			url: "/reader/entries",
            data: map,
            headers: { "Content-Type": "application/json" }
        });
	};
	
	this.markAllRead = function(entries, filter) {
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

		return $http({
			method: "POST",
			url: "/reader/entries",
            data: map,
            headers: { "Content-Type": "application/json" }
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
	
	this.loadStats = function() {
		return $http({
			method: "GET",
    		url: "/reader/stats"
		});
	};
    
}]);
