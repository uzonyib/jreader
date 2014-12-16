angular.module("jReaderApp").controller("EntriesCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.archives = [];
	$scope.entries = [];
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isEntriesSelected();
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
	
	$scope.toggleCollapsion = function(entry) {
		entry.uncollapsed = !entry.uncollapsed;
		if (!entry.read) {
			entry.read = true;
			$scope.ajaxService.markRead(entry);
		}
	};
	
	$scope.loadMoreEntries = function() {
		if ($scope.ajaxService.moreEntriesAvailable) {
			$scope.viewService.entryFilter.incrementPageIndex();
			$scope.ajaxService.loadEntries($scope.viewService.entryFilter.get());
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
		entry.archived = true;
		$scope.ajaxService.archive(entry, entry.archive);
	};
	
}]);
