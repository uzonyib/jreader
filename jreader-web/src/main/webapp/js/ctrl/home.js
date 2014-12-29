angular.module("jReaderApp").controller("HomeCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = true;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isHomeSelected();
	});
	
}]);
