angular.module("jReaderApp").service("ajaxService", ["$http", function ($http) {
	var service = this;

	this.refreshSubscriptions = function() {
    	return $http.get("/reader/groups");
    };
    
    this.refreshArchives = function() {
    	return $http.get("/reader/archives");
    };
    
    this.loadPosts = function(filter) {
    	var url = "";
    	if (filter.groupId != null) {
    		url = "/groups/" + filter.groupId;
    		if (filter.subscriptionId != null) {
    			url += "/subscriptions/" + filter.subscriptionId;
    		}
    	}
    	url += "/posts";
    	
    	var params = {
			"offset": filter.offset,
			"count": filter.count,
			"ascending": filter.ascendingOrder
		};
    	
    	return $http({
    		method: "GET",
    		url: "/reader" + url + "/" + filter.selection,
    		params: params
        });
    };
    
    this.loadArchivedPosts = function(filter) {
    	var url = "";
    	if (filter.archiveId != null) {
    		url = "/" + filter.archiveId;
    	}
    	
    	return $http({
    		method: "GET",
    		url: "/reader/archives" + url + "/posts",
    		params: {
    			"offset": filter.offset,
    			"count": filter.count,
    			"ascending": filter.ascendingOrder
    		}
        });
    };
    
    this.createGroup = function(title, order) {
    	return $http({
			method: "POST",
			url: "/reader/groups",
            data: {
            	"title": title,
            	"order": order
            }
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
			url: "/reader/groups/" + groupId,
            data: { "title": title }
        });
	};
	
	this.reorderGroups = function(groups) {
		return $http({
			method: "PUT",
			url: "/reader/groups",
            data: groups
        });
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
	
	this.markRead = function(post) {
		var map = {};
		map[post.groupId] = {};
		map[post.groupId][post.subscriptionId] = [post.id];
		
		return $http({
			method: "POST",
			url: "/reader/posts",
            data: map,
            headers: { "Content-Type": "application/json" }
        });
	};
	
	this.markAllRead = function(posts, filter) {
		var map = {};
		angular.forEach(posts, function(post) {
			if (!angular.isDefined(map[post.groupId])) {
				map[post.groupId] = {};
			}
			if (!angular.isDefined(map[post.groupId][post.subscriptionId])) {
				map[post.groupId][post.subscriptionId] = [];
			}
			map[post.groupId][post.subscriptionId].push(post.id);
		});

		return $http({
			method: "POST",
			url: "/reader/posts",
            data: map,
            headers: { "Content-Type": "application/json" }
        });
	};
	
	this.setBookmarked = function(post, bookmarked) {
		return $http({
			method: "PUT",
			url: "/reader/groups/" + post.groupId + "/subscriptions/" + post.subscriptionId + "/posts/" + post.id + "/bookmarked",
            params: { "value": bookmarked }
        });
	};
	
	this.bookmark = function(post) {
		return service.setBookmarked(post, true);
	};
	
	this.deleteBookmark = function(post) {
		return service.setBookmarked(post, false);
	};
	
	this.archive = function(post, archive) {
		return $http({
			method: "POST",
			url: "/reader/archives/" + archive.id + "/posts",
            params: {
            	"groupId": post.groupId,
            	"subscriptionId": post.subscriptionId,
            	"postId": post.id,
            },
            headers: { "Content-Type": "application/json" }
        });
	};
	
	this.deleteArchivedPost = function(archiveId, postId) {
		return $http({
			method: "DELETE",
			url: "/reader/archives/" + archiveId + "/posts/" + postId
        });
	};
	
	this.loadStats = function() {
		return $http({
			method: "GET",
    		url: "/reader/stats"
		});
	};
    
}]);
