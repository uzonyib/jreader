angular.module("jReaderApp").controller("HeadCtrl", ["$scope", "ajaxService", function ($scope, ajaxService) {
	$scope.ajaxService = ajaxService;

	$scope.titlePrefix = "jReader";
	$scope.title = $scope.titlePrefix;
	
	$scope.$watch("subscriptionGroups.unreadCount", function(count) {
		if (count > 0) {
			$scope.title = $scope.titlePrefix + " (" + count + ")";
		} else {
			$scope.title = $scope.titlePrefix;
		}
	});
}]);
