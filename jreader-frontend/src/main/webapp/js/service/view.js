angular.module("jReaderApp").service("viewService", function () {
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
