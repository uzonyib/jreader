<ul class="nav nav-pills nav-stacked">
	<li class="menu-item" id="logout-menu-item">
		<a href="${ logoutUrl }">
			<span class="glyphicon glyphicon-log-out"></span><span class="title">Logout</span>
		</a>
	</li>
	<li id="home-menu-item" class="menu-item"
		data-ng-class="{selected: home.selected}"
		data-ng-click="selectHome()">
		<span class="glyphicon glyphicon-home"></span><span class="title">Home</span>
	</li>
	<li id="settings-menu-item" class="menu-item"
		data-ng-class="{selected: settings.selected}"
		data-ng-click="selectSettings()">
		<span class="glyphicon glyphicon-cog"></span><span class="title">Settings</span>
	</li>
	<li id="all-items-menu-item" class="menu-item"
		data-ng-class="{selected: allItems.selected}"
		data-ng-click="selectAllItems()">
		<span class="glyphicon glyphicon-th-list"></span>
		<span class="title">All feeds</span>
		<span class="unread-count badge" data-ng-show="unreadCount">{{unreadCount}}</span>
	</li>
</ul>

<div id="subscription-menu">
	<ul data-ng-repeat="group in subscriptionGroups"
		class="menu-group nav nav-pills nav-stacked">
		<li class="menu-item"
			data-ng-class="{selected: group.selected}"
			data-ng-click="selectSubscriptionGroup(group)">
			<span class="glyphicon glyphicon-chevron-right" data-ng-show="group.collapsed" data-ng-click="uncollapse(group.id, $event)"></span>
			<span class="glyphicon glyphicon-chevron-down" data-ng-show="!group.collapsed" data-ng-click="collapse(group.id, $event)"></span>
			<span class="title">{{group.title}}</span>
			<span data-ng-show="group.unreadCount > 0" class="unread-count badge">{{group.unreadCount}}</span>
		</li>
		<li data-ng-repeat="subscription in group.subscriptions"
			class="menu-item feed-item"
			data-ng-class="{selected: subscription.selected}"
			data-ng-show="!group.collapsed"
			data-ng-click="selectSubscription(group, subscription)">
			<span class="title">{{subscription.title}}</span>
			<span data-ng-show="subscription.unreadCount > 0" class="unread-count badge">{{subscription.unreadCount}}</span>
		</li>
	</ul>
</div>
	
<div id="archives-menu">
	<ul class="nav nav-pills nav-stacked">
		<li class="menu-item"
			data-ng-class="{selected: archivedItems.selected}"
			data-ng-show="archives.length > 0"
			data-ng-click="selectArchivedItems()">
			<span class="glyphicon glyphicon-chevron-right" data-ng-show="archivedItems.collapsed" data-ng-click="uncollapseArchivedItems($event)"></span>
			<span class="glyphicon glyphicon-chevron-down" data-ng-show="!archivedItems.collapsed" data-ng-click="collapseArchivedItems($event)"></span>
			<span class="title">Archives</span>
		</li>
		<li data-ng-show="!archivedItems.collapsed"
			data-ng-repeat="archive in archives"
			class="menu-item feed-item"
			data-ng-class="{selected: archive.selected}"
			data-ng-click="selectArchive(archive)">
			<span class="title">{{archive.title}}</span>
		</li>
	</ul>
</div>
