angular.module("jReaderApp").controller("HomeCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = true;
	
	$scope.stats = [];
	$scope.chartOptions = {
		scaleBeginAtZero: true,
		responsive: true,
		maintainAspectRatio: false
	};
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isHomeSelected();
		if ($scope.active) {
			$scope.refreshStats();
		}
	});
	
	$scope.$watch("subscriptionGroups.items", function(subscriptionGroups) {
		$scope.populateStats();
	});
	
	$scope.refreshStats = function() {
		$scope.ajaxService.loadStats().success(function(response) {
			$scope.stats = response.payload;
			$scope.populateStats();
		});
	};
	
	$scope.populateStats = function() {
		angular.forEach($scope.subscriptionGroups.items, function(group) {
			angular.forEach(group.subscriptions, function(subscription) {
				angular.forEach($scope.stats, function(feedStats) {
					if (angular.equals(feedStats.feed.url, subscription.feed.url)) {
						subscription.stats = {};
						subscription.stats.labels = [];
						subscription.stats.data = [[]];
						angular.forEach(feedStats.stats, function(stat) {
							subscription.stats.labels.push(moment(stat.date).utc().format("MMM DD"));
							subscription.stats.data[0].push(stat.count);
						});
					}
				});
			});
		});
	};
	
}]);
