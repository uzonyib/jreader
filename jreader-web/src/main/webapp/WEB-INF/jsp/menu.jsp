<div class="menu-item" id="logout-menu-item">
	<a href="${ logoutUrl }">
		<span class="icon"></span><span class="title">Logout</span>
	</a>
</div>

<div id="home-menu-item" class="menu-item"
	ng-class="{selected: home.selected}"
	ng-click="selectHome()">
	<span class="icon"></span><span class="title">Home</span>
</div>

<div id="settings-menu-item" class="menu-item"
	ng-class="{selected: settings.selected}"
	ng-click="selectSettings()">
	<span class="icon"></span><span class="title">Settings</span>
</div>

<div id="all-items-menu-item" class="menu-item"
	ng-class="{selected: allItems.selected}"
	ng-click="selectAllItems()">
	<span class="icon"></span><span class="title">All feeds</span><span class="unread-count" ng-if="unreadCount">({{unreadCount}})</span>
</div>

<div id="subscription-menu">
	<div ng-repeat="group in subscriptionGroups"
		class="menu-group" ng-class="{collapsed: group.collapsed}">
		<div class="menu-item group-item"
			ng-class="{selected: group.selected}"
			ng-click="selectSubscriptionGroup(group)">
			<span class="icon collapsed-icon" ng-click="uncollapse(group.id, $event)"></span>
			<span class="icon uncollapsed-icon" ng-click="collapse(group.id, $event)"></span>
			<span class="title">{{group.title}}</span>
			<span ng-if="group.unreadCount > 0" class="unread-count">({{group.unreadCount}})</span>
		</div>
		<div ng-repeat="subscription in group.subscriptions"
			class="menu-item feed-item"
			ng-class="{selected: subscription.selected}"
			ng-click="selectSubscription(group, subscription)">
			<span class="title">{{subscription.title}}</span>
			<span ng-if="subscription.unreadCount > 0" class="unread-count">({{subscription.unreadCount}})</span>
		</div>
	</div>
</div>

<div id="archives-menu-item" class="menu-item"
	ng-class="{selected: archivedItems.selected, collapsed: archivedItems.collapsed}"
	ng-show="archives.length > 0"
	ng-click="selectArchivedItems()">
	<span class="icon collapsed-icon" ng-click="uncollapseArchivedItems($event)"></span>
	<span class="icon uncollapsed-icon" ng-click="collapseArchivedItems($event)"></span>
	<span class="title">Archives</span>
</div>

<div id="archive-menu" ng-show="!archivedItems.collapsed">
	<div ng-repeat="archive in archives"
		class="menu-group">
		<div class="menu-item feed-item"
			ng-class="{selected: archive.selected}"
			ng-click="selectArchive(archive)">
			<span class="title">{{archive.title}}</span>
		</div>
	</div>
</div>
