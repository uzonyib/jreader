angular.module("jReaderApp").controller("ArchivesCtrl", ["$scope", "$http", "ajaxService", "viewService", function ($scope, $http, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isArchivesSelected();
	});
	
}]);
