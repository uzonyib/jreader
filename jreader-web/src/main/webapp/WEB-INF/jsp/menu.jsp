<ul class="nav nav-pills nav-stacked">
	<li id="home-menu-item" class="menu-item"
		data-ng-class="{selected: menu.homeSelected}"
		data-ng-click="viewService.selectHome()">
		<span class="glyphicon glyphicon-home"></span><span class="title">Home</span>
	</li>
	<li id="settings-menu-item" class="menu-item"
		data-ng-class="{selected: menu.settingsSelected}"
		data-ng-click="viewService.selectSettings()">
		<span class="glyphicon glyphicon-cog"></span><span class="title">Settings</span>
	</li>
	<li id="all-items-menu-item" class="menu-item"
		data-ng-class="{selected: menu.allItemsSelected}"
		data-ng-click="viewService.selectAllItems()">
		<span class="glyphicon glyphicon-th-list"></span>
		<span class="title">All feeds</span>
		<span class="unread-count badge" data-ng-show="groups.unreadCount">{{groups.unreadCount}}</span>
	</li>
</ul>
<ul data-ng-repeat="group in groups.items"
	class="menu-group nav nav-pills nav-stacked">
	<li class="menu-item group-item"
		data-ng-class="{selected: group.selected}"
		data-ng-click="viewService.selectGroup(group.id)">
		<span class="glyphicon glyphicon-plus" data-ng-show="!group.expanded" data-ng-click="menu.expand(group.id, $event)"></span>
		<span class="glyphicon glyphicon-minus" data-ng-show="group.expanded" data-ng-click="menu.collapse(group.id, $event)"></span>
		<span class="title">{{group.title}}</span>
		<span data-ng-show="group.unreadCount > 0" class="unread-count badge">{{group.unreadCount}}</span>
	</li>
	<li data-ng-repeat="subscription in group.subscriptions"
		class="menu-item feed-item"
		data-ng-class="{selected: subscription.selected}"
		data-ng-show="group.expanded"
		data-ng-click="viewService.selectSubscription(group.id, subscription.id)">
		<span class="title">{{subscription.title}}</span>
		<span data-ng-show="subscription.unreadCount > 0" class="unread-count badge">{{subscription.unreadCount}}</span>
	</li>
</ul>
<ul class="nav nav-pills nav-stacked">
	<li class="menu-item"
		data-ng-class="{selected: menu.archivedItemsSelected}"
		data-ng-show="archives.length > 0"
		data-ng-click="viewService.selectArchivedItems()">
		<span class="glyphicon glyphicon-plus" data-ng-show="!menu.archivedItemsExpanded" data-ng-click="menu.expandArchivedItems($event)"></span>
		<span class="glyphicon glyphicon-minus" data-ng-show="menu.archivedItemsExpanded" data-ng-click="menu.collapseArchivedItems($event)"></span>
		<span class="title">Archives</span>
	</li>
	<li data-ng-show="menu.archivedItemsExpanded"
		data-ng-repeat="archive in archives"
		class="menu-item feed-item"
		data-ng-class="{selected: archive.selected}"
		data-ng-click="viewService.selectArchive(archive.id)">
		<span class="title">{{archive.title}}</span>
	</li>
</ul>
