angular.module("jReaderApp").controller("EntriesNavbarCtrl", ["$scope", "$window", "ajaxService", "viewService", function ($scope, $window, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isEntriesSelected();
	});
	
}]);
