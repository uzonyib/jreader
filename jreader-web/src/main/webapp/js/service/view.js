angular.module("jReaderApp").service("viewService", ["$window", function ($window) {
	var service = this;
	
	this.activeView = {
		type: "home",
		groupId: null,
		subscriptionId: null,
		archiveId: null
	};
	
	this.selectHome = function() {
		var view = {
			type: "home",
			groupId: null,
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
			groupId: null,
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
			groupId: null,
			subscriptionId: null,
			archiveId: null
		};
		if (!angular.equals(this.activeView, view)) {
			this.activeView = view;
		}
	};
	
	this.selectGroup = function(groupId) {
		var view = {
			type: "group",
			groupId: groupId,
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
			groupId: groupId,
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
			groupId: null,
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
			groupId: null,
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
	
	this.isGroupSelected = function(groupId) {
		var view = {
			type: "group",
			groupId: groupId,
			subscriptionId: null,
			archiveId: null
		};
		return angular.equals(this.activeView, view);
	};
	
	this.isSubscriptionSelected = function(groupId, subscriptionId) {
		var view = {
			type: "subscription",
			groupId: groupId,
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
			groupId: null,
			subscriptionId: null,
			archiveId: archiveId
		};
		return angular.equals(this.activeView, view);
	};
	
	service.isEntriesSelected = function() {
		return angular.equals(service.activeView.type, "allItems") || angular.equals(service.activeView.type, "group") || angular.equals(service.activeView.type, "subscription");
	};
	
	service.isArchivesSelected = function() {
		return angular.equals(service.activeView.type, "archivedItems") || angular.equals(service.activeView.type, "archive");
	};
	
	this.getGroupId = function() {
		return this.activeView.groupId;
	};
	
	this.getSubscriptionId = function() {
		return this.activeView.subscriptionId;
	};
	
	this.getArchiveId = function() {
		return this.activeView.archiveId;
	};
	
	this.entryFilter = {
		selection: "unread",
		pageIndex: 0,
		ascendingOrder: true,
		pageSize: Math.ceil($window.innerHeight / 34 / 10) * 10,
		initialPagesToLoad: 2
	};
	
	this.entryFilter.get = function() {
		var filter = angular.copy(this);
		filter.groupId = service.activeView.groupId;
		filter.subscriptionId = service.activeView.subscriptionId;
		return filter;
	};
	
	this.entryFilter.resetPageIndex = function() {
		this.pageIndex = 0;
	};
	
	this.entryFilter.incrementPageIndex = function() {
		++this.pageIndex;
	};
	
	this.archiveFilter = {
		pageIndex: 0,
		ascendingOrder: true,
		pageSize: Math.ceil($window.innerHeight / 34 / 10) * 10,
		initialPagesToLoad: 2
	};
	
	this.archiveFilter.get = function() {
		var filter = angular.copy(this);
		filter.archiveId = service.activeView.archiveId;
		return filter;
	};
	
	this.archiveFilter.resetPageIndex = function() {
		this.pageIndex = 0;
	};
	
	this.archiveFilter.incrementPageIndex = function() {
		++this.pageIndex;
	};
	
}]);
