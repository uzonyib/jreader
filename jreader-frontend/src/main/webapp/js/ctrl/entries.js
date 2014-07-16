angular.module("jReaderApp").controller("EntriesCtrl", function ($scope, $element, $window, ajaxService, viewService) {
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
	
});
