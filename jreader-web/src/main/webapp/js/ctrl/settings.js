angular.module("jReaderApp").controller("SettingsCtrl", ["$scope", "ajaxService", "viewService", function ($scope, ajaxService, viewService) {
	$scope.ajaxService = ajaxService;
	$scope.viewService = viewService;
	
	$scope.active = false;
	
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
	
	$scope.$watch("groups.items", function() {
		$scope.newSubscription.group = $scope.groups.items[0];
	});
	
	$scope.createGroup = function() {
		$scope.ajaxService.createGroup($scope.newGroupTitle, $scope.groups.size()).success($scope.groups.add);
		$scope.newGroupTitle = "";
	};
	
	$scope.deleteGroup = function(groupId) {
		$scope.ajaxService.deleteGroup(groupId).success(function() {
			$scope.groups.remove(groupId);
		});
	};
	
	$scope.swapGroups = function(index1, index2) {
		$scope.groups.swap(index1, index2);
	}
	
	$scope.moveGroupUp = function(groupId) {
		$scope.ajaxService.moveGroupUp(groupId);
	};
	
	$scope.moveGroupDown = function(groupId) {
		$scope.ajaxService.moveGroupDown(groupId);
	};
	
	$scope.moveSubscriptionUp = function(groupId, subscriptionId) {
		$scope.ajaxService.moveSubscriptionUp(groupId, subscriptionId).success($scope.groups.setItemsFromPayLoad);
	};
	
	$scope.moveSubscriptionDown = function(groupId, subscriptionId) {
		$scope.ajaxService.moveSubscriptionDown(groupId, subscriptionId).success($scope.groups.setItemsFromPayLoad);
	};
	
	$scope.subscribe = function() {
		$scope.ajaxService.subscribe($scope.newSubscription.group.id, $scope.newSubscription.url).success($scope.groups.setItemsFromPayLoad);
	};
	
	$scope.unsubscribe = function(groupId, subscriptionId) {
		$scope.ajaxService.unsubscribe(groupId, subscriptionId).success($scope.groups.setItemsFromPayLoad);
	};
	
	$scope.editTitle = function(groupOrSubscription) {
		groupOrSubscription.editingTitle = true;
	};
	
	$scope.entitleGroup = function(group) {
		$scope.ajaxService.entitleGroup(group.id, group.newTitle).success($scope.groups.update);
	};
	
	$scope.entitleSubscription = function(group, subscription) {
		$scope.ajaxService.entitleSubscription(group.id, subscription.id, subscription.newTitle).success($scope.groups.setItemsFromPayLoad);
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
		angular.forEach($scope.groups.items, function(group) {
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
			var g = undefined;
			angular.forEach($scope.groups.items, function(group) {
				if (group.title === title) {
					g = group;
				}
			});
			return g;
		};
		
		var findSubscription = function(url) {
			var s = undefined;
			angular.forEach($scope.groups.items, function(group) {
				angular.forEach(group.subscriptions, function(subscription) {
					if (subscription.feed.url === url) {
						s = subscription;
					}
				});
			});
			return s;
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
            } else if (job.type === 1) {
    			var group = findGroup(job.title);
    			if (angular.isDefined(group)) {
    				$scope.importLog += "Group \"" + job.title + "\" already exists.\n";
    				addSubscribeJobs(job, group.id, jobQueue);
    				process(jobQueue.pop());
    			} else {
    				$scope.importLog += "Creating group \"" + job.title + "\"...";
    				$scope.ajaxService.createGroup(job.title).success(function(response) {
    					$scope.importLog += " OK.\n";
    					$scope.groups.setItemsFromPayLoad(response);
    					addSubscribeJobs(job, findGroup(job.title).id, jobQueue);
        				process(jobQueue.pop());
    				}).error(function(response) {
    					$scope.importLog += " ERROR.\n";
    					++$scope.importErrors;
        				process(jobQueue.pop());
    				});
    			}
            } else if (job.type === 2) {
            	var subscription = findSubscription(job.url);
            	if (angular.isDefined(subscription)) {
    				$scope.importLog += "Already subscribed to " + job.url + ".\n";
    				process(jobQueue.pop());
    			} else {
    				$scope.importLog += "Subscribing to " + job.url + "...";
    				$scope.ajaxService.subscribe(job.groupId, job.url).success(function(response) {
    					$scope.importLog += " OK.\n";
    					$scope.groups.setItemsFromPayLoad(response);
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
            	var subscription = findSubscription(job.url);
            	if (!angular.equals(subscription.title, job.title)) {
	            	$scope.ajaxService.entitleSubscription(job.groupId, subscription.id, job.title).success(function(response) {
			        	$scope.importLog += " OK.\n";
			        	$scope.groups.setItemsFromPayLoad(response);
						process(jobQueue.pop());
			        }).error(function(response) {
						$scope.importLog += " ERROR.\n";
						++$scope.importErrors;
	    				process(jobQueue.pop());
					});
            	} else {
            		$scope.importLog += " OK.\n";
					process(jobQueue.pop());
            	}
            }
        };
        process(jobQueue.pop());
	};
	
}]);
