<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="add-subscription">
	<form ng-submit="createGroup()">
		<span>Add group</span><input ng-model="newGroupTitle"
			type="text" name="title" placeholder="group name" /><input
			type="image" src="/images/tick_black.png" title="Create group" />
	</form>
	<form ng-submit="subscribe()">
		<span>Add subscription to group</span><select
			ng-model="newSubscription.group"
			ng-options="group.title for group in subscriptionGroups">
		</select><span>from</span><input
			type="text" placeholder="URL" size="80" ng-model="newSubscription.url" /><input
			type="image" src="/images/tick_black.png" title="Subscribe" />
	</form>
</div>
<div id="subscription-settings">
	<div class="settings-group" ng-repeat="group in subscriptionGroups">
		<div class="group-title">
			<span class="title" ng-show="!group.editingTitle" ng-click="editTitle(group)">{{group.title}}</span>
			<form class="set-group-title" ng-show="group.editingTitle" ng-click="entitleGroup(group)">
				<input type="text" placeholder="name" ng-model="group.newTitle" />
				<input type="image" src="/images/tick_black.png" title="Change" />
			</form>
			<form ng-if="group.subscriptions.length <= 0"
				ng-submit="deleteGroup(group.id)">
				<input type="image" src="/images/cross_black.png" title="Delete" />
			</form>
			<form ng-if="!$first" ng-submit="moveGroupUp(group.id)">
				<input type="image" src="/images/move_up_black.png" title="Move up" />
			</form>
			<form ng-if="!$last" ng-submit="moveGroupDown(group.id)">
				<input type="image" src="/images/move_down_black.png" title="Move down" />
			</form>
		</div>
		<div class="settings-item" ng-repeat="subscription in group.subscriptions">
			<span class="title" ng-show="!subscription.editingTitle" ng-click="editTitle(subscription)">{{subscription.title}}</span>
			<form class="set-item-title" ng-show="subscription.editingTitle" ng-click="entitleSubscription(group, subscription)">
				<input type="text" placeholder="name" ng-model="subscription.newTitle" />
				<input type="image" src="/images/tick_black.png" title="Change" />
			</form>
			<form ng-submit="unsubscribe(group.id, subscription.id)">
				<input type="image" src="/images/cross_black.png" title="Unsubscribe" class="unsubscribe-button" />
			</form>
			<form ng-if="!$first" ng-submit="moveSubscriptionUp(group.id, subscription.id)">
				<input type="image" src="/images/move_up_black.png" title="Move up" />
			</form>
			<form ng-if="!$last" ng-submit="moveSubscriptionDown(group.id, subscription.id)">
				<input type="image" src="/images/move_down_black.png" title="Move down" />
			</form>
		</div>
	</div>
</div>