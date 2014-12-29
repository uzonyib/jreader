angular.module("jReaderApp").controller("SettingsCtrl", ["$scope", "$http", "ajaxService", "viewService", function ($scope, $http, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	$scope.subscriptionGroups = [];
	
	$scope.newGroupTitle = "";
	
	$scope.newSubscription = {};
	$scope.newSubscription.group = {};
	$scope.newSubscription.url = "";
	
	$scope.newArchiveTitle = "";
	
	$scope.exportImportJson = "";
	$scope.importLog = "";
	$scope.importErrors = 0;
	$scope.showImportLog = false;
	
	$scope.$watch("viewService.activeView", function() {
		$scope.active = $scope.viewService.isSettingsSelected();
	});
	
	$scope.$watch("ajaxService.subscriptionGroups", function(subscriptionGroups) {
		$scope.subscriptionGroups = angular.copy(subscriptionGroups);
		$scope.newSubscription.group = $scope.subscriptionGroups[0];
		angular.forEach($scope.subscriptionGroups, function(group) {
			group.editingTitle = false;
			group.newTitle = group.title;
			angular.forEach(group.subscriptions, function(subscription) {
				subscription.editingTitle = false;
				subscription.newTitle = subscription.title;
			});
		});
	});
	
	$scope.createGroup = function() {
		$scope.ajaxService.createGroup($scope.newGroupTitle);
		$scope.newGroupTitle = "";
	};
	
	$scope.deleteGroup = function(subscriptionGroupId) {
		$scope.ajaxService.deleteGroup(subscriptionGroupId);
	};
	
	$scope.moveGroupUp = function(subscriptionGroupId) {
		$scope.ajaxService.moveGroupUp(subscriptionGroupId);
	};
	
	$scope.moveGroupDown = function(subscriptionGroupId) {
		$scope.ajaxService.moveGroupDown(subscriptionGroupId);
	};
	
	$scope.moveSubscriptionUp = function(subscriptionGroupId, subscriptionId) {
		$scope.ajaxService.moveSubscriptionUp(subscriptionGroupId, subscriptionId);
	};
	
	$scope.moveSubscriptionDown = function(subscriptionGroupId, subscriptionId) {
		$scope.ajaxService.moveSubscriptionDown(subscriptionGroupId, subscriptionId);
	};
	
	$scope.subscribe = function() {
		$scope.ajaxService.subscribe($scope.newSubscription.group.id, $scope.newSubscription.url);
	};
	
	$scope.unsubscribe = function(subscriptionGroupId, subscriptionId) {
		$scope.ajaxService.unsubscribe(subscriptionGroupId, subscriptionId);
	};
	
	$scope.editTitle = function(groupOrSubscription) {
		groupOrSubscription.editingTitle = true;
	};
	
	$scope.entitleGroup = function(group) {
		$scope.ajaxService.entitleGroup(group.id, group.newTitle);
	};
	
	$scope.entitleSubscription = function(group, subscription) {
		$scope.ajaxService.entitleSubscription(group.id, subscription.id, subscription.newTitle);
	};
	
	$scope.createArchive = function() {
		$scope.ajaxService.createArchive($scope.newArchiveTitle).success($scope.setArchives);
		$scope.newArchiveTitle = "";
	};
	
	$scope.deleteArchive = function(archiveId) {
		$scope.ajaxService.deleteArchive(archiveId).success($scope.setArchives);
	};
	
	$scope.moveArchiveUp = function(archiveId) {
		$scope.ajaxService.moveArchiveUp(archiveId).success($scope.setArchives);
	};
	
	$scope.moveArchiveDown = function(archiveId) {
		$scope.ajaxService.moveArchiveDown(archiveId).success($scope.setArchives);
	};
	
	$scope.entitleArchive = function(archive) {
		$scope.ajaxService.entitleArchive(archive.id, archive.newTitle).success($scope.setArchives);
	};
	
	$scope.exportSubscriptions = function() {
		var result = [];
		angular.forEach($scope.subscriptionGroups, function(group) {
			var groupCopy = {};
			groupCopy.title = group.title;
			groupCopy.subscriptions = [];
			angular.forEach(group.subscriptions, function(subscription) {
				var subscriptionCopy = {};
				subscriptionCopy.title = subscription.title;
				subscriptionCopy.url = subscription.feed.url;
				groupCopy.subscriptions.push(subscriptionCopy);
			});
			result.push(groupCopy);
		});
		$scope.exportImportJson = angular.toJson(result, true);
	};
	
	$scope.importSubscriptions = function() {
		var jobQueue = [];
		angular.forEach(angular.fromJson($scope.exportImportJson), function(group) {
			group.type = 1;
			jobQueue.unshift(group);
		});
		
		$scope.showImportLog = true;
		$scope.importLog = "Import started, please wait...\n";
		$scope.importErrors = 0;
		
		var findGroup = function(title) {
			var id = undefined;
			angular.forEach($scope.subscriptionGroups, function(group) {
				if (group.title === title) {
					id = group.id;
				}
			});
			return id;
		};
		
		var findSubscription = function(url) {
			var id = undefined;
			angular.forEach($scope.subscriptionGroups, function(group) {
				angular.forEach(group.subscriptions, function(subscription) {
					if (subscription.feed.url === url) {
						id = subscription.id;
					}
				});
			});
			return id;
		};
		
		var addSubscribeJobs = function(group, id, jobQueue) {
			var q = [];
			angular.forEach(group.subscriptions, function(subscription) {
				subscription.type = 2;
				subscription.groupId = id;
				q.unshift(subscription);
			});
			angular.forEach(q, function(e) {
				jobQueue.push(e);
			});
		};
		
		var addEntitleJobs = function(subscription, jobQueue) {
			subscription.type = 3;
			jobQueue.push(subscription);
		};
		
        var process = function(job) {
            if (angular.isUndefined(job)) {
            	$scope.importLog += "\nImport completed with " + $scope.importErrors + " error(s).\n";
            	$scope.ajaxService.setSubscriptionGroups($scope.subscriptionGroups);
            } else if (job.type === 1) {
    			var id = findGroup(job.title);
    			if (angular.isDefined(id)) {
    				$scope.importLog += "Group \"" + job.title + "\" already exists.\n";
    				addSubscribeJobs(job, id, jobQueue);
    				process(jobQueue.pop());
    			} else {
    				$scope.importLog += "Creating group \"" + job.title + "\"...";
    				$http({
    					method: "POST",
    					url: "/reader/groups",
    					params: { "title": job.title }
    				}).success(function(response) {
    					$scope.importLog += " OK.\n";
    					$scope.subscriptionGroups = response;
    					addSubscribeJobs(job, findGroup(job.title), jobQueue);
        				process(jobQueue.pop());
    				}).error(function(response) {
    					$scope.importLog += " ERROR.\n";
    					++$scope.importErrors;
        				process(jobQueue.pop());
    				});
    			}
            } else if (job.type === 2) {
            	var id = findSubscription(job.url);
            	if (angular.isDefined(id)) {
    				$scope.importLog += "Already subscribed to " + job.url + ".\n";
    				process(jobQueue.pop());
    			} else {
    				$scope.importLog += "Subscribing to " + job.url + "...";
    				$http({
    					method: "POST",
    					url: "/reader/groups/" + job.groupId + "/subscriptions",
    					params: { "url": job.url }
    				}).success(function(response) {
    					$scope.importLog += " OK.\n";
    					$scope.subscriptionGroups = response;
    					addEntitleJobs(job, jobQueue);
    					process(jobQueue.pop());
    				}).error(function(response) {
    					$scope.importLog += " ERROR.\n";
    					++$scope.importErrors;
        				process(jobQueue.pop());
    				});
    			}
            } else {
            	$scope.importLog += "Setting title \"" + job.title + "\"...";
            	var id = findSubscription(job.url);
            	$http({
        			method: "PUT",
        			url: "/reader/groups/" + job.groupId + "/subscriptions/" + id + "/title",
                    params: { "value": job.title }
                }).success(function(response) {
		        	$scope.importLog += " OK.\n";
		        	$scope.subscriptionGroups = response;
					process(jobQueue.pop());
		        }).error(function(response) {
					$scope.importLog += " ERROR.\n";
					++$scope.importErrors;
    				process(jobQueue.pop());
				});
            }
        };
        process(jobQueue.pop());
	};
	
}]);
