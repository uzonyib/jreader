angular.module("jReaderApp").controller("HomeCtrl", ["$scope", "$window", "ajaxService", "viewService", function ($scope, $window, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;

	$scope.active = true;

	$scope.stats = [];
	$scope.chart = {
		options: {
			scaleBeginAtZero: true,
			responsive: true,
			animation: false,
			maintainAspectRatio: false,
			scales: {
				xAxes: [{
					ticks: {
						fontColor: "#d4d4d4"
					}
				}],
				yAxes: [{
					ticks: {
						fontColor: "#d4d4d4",
						beginAtZero: true
					}
				}]
			}
		}
	};

	$scope.chartsEnabled = $window.innerWidth >= 992;

	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isHomeSelected();
		if ($scope.active) {
			$scope.refreshStats();
		}
	});

	$scope.$watch("groups.items", function() {
		$scope.populateStats();
	});

	$scope.refreshStats = function() {
		if ($scope.chartsEnabled) {
			$scope.ajaxService.loadStats().success(function(response) {
				$scope.stats = response;
				$scope.populateStats();
			});
		}
	};

	$scope.populateStats = function() {
		angular.forEach($scope.groups.items, function(group) {
			angular.forEach(group.subscriptions, function(subscription) {
				angular.forEach($scope.stats, function(feedStats) {
					if (angular.equals(feedStats.feed.url, subscription.feed.url)) {
						subscription.stats = {};
						subscription.stats.labels = [];
						subscription.stats.data = [[]];

						var last = null;
						var dayMillis = 24 * 60 * 60 * 1000;

						var to = moment().unix() * 1000;
						var from = to - 30 * dayMillis;

						if (feedStats.stats.length > 0) {
						    for (var date = feedStats.stats[0].date - dayMillis; date > from; date -= dayMillis) {
						    	subscription.stats.data[0].unshift(0);
								subscription.stats.labels.unshift(moment(date).utc().format("DD MMM"));
						    }

						    angular.forEach(feedStats.stats, function(stat) {
						        if (last != null) {
						            for (var date = last + dayMillis; date < stat.date; date += dayMillis) {
						            	subscription.stats.data[0].push(0);
						                subscription.stats.labels.push(moment(date).utc().format("DD MMM"));
						            }
						        }

						        subscription.stats.data[0].push(stat.count);
						        subscription.stats.labels.push(moment(stat.date).utc().format("DD MMM"));
						        last = stat.date;
						    });

							last = feedStats.stats[feedStats.stats.length - 1].date;
						    for (var date = last + dayMillis; date < to; date += dayMillis) {
						    	subscription.stats.data[0].push(0);
						        subscription.stats.labels.push(moment(date).utc().format("DD MMM"));
						    }
						} else {
							for (var date = from + dayMillis; date <= to; date += dayMillis) {
								subscription.stats.data[0].push(0);
						        subscription.stats.labels.push(moment(date).utc().format("DD MMM"));
						    }
						}
					}
				});
			});
		});
	};

}]);
