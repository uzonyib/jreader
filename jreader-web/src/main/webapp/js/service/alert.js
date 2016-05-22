angular.module("jReaderApp").service("alertService", ["$timeout", function ($timeout) {
	var service = this;
	
	this.alerts = [];
	
	this.add = function(message) {
		service.alerts.push({ "message": message });
	};
	
	this.removeFirst = function() {
		service.alerts.shift();
	};
	
}]);
angular.module("jReaderApp").directive("animateAlert", ["$animate", "$timeout", "alertService", function($animate, $timeout, alertService) {
	return function(scope, elem, attr) {
		scope.$watch(attr.animateOnChange, function() {
			var cls = "animate";
			$animate.addClass(elem, cls).then(function() {
				$timeout(function() {
					$animate.removeClass(elem, cls).then(function() {
						alertService.removeFirst();
					});
				}, 5000);
			});
		})
	}
}]);
