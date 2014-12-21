angular.module("jReaderApp").controller("ArchivesNavbarCtrl", ["$scope", "$http", "$window", "ajaxService", "viewService", function ($scope, $http, $window, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.loading = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isArchivesSelected();
	});
	
	$scope.$watch("ajaxService.loadingArchivedEntries", function(loadingEntries) {
		$scope.loading = loadingEntries;
	});
	
}]);
