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