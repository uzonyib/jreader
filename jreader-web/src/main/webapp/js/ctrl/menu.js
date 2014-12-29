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
		$scope.archivedItems.selected = $scope.viewService.isArchivedItemsSelected();
		
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
