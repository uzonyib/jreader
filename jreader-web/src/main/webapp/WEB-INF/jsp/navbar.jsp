<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container-fluid">
		<div class="row">
			<div id="nav-brand" class="col-lg-2 col-md-2 col-sm-3 hidden-xs">
				<div class="navbar-header">
					<a class="navbar-brand" href="/reader">
						<img alt="Brand" src="/images/rss.png">
					</a>
				</div>
			</div>
			<div id="nav-main" class="col-lg-10 col-md-10 col-sm-9 col-xs-12 col-lg-offset-2 col-md-offset-2 col-sm-offset-3">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
			    	<ul class="nav navbar-nav" data-ng-controller="EntriesNavbarCtrl" data-ng-show="active">
			    		<li class="btn-group">
							<button type="button" class="btn btn-default navbar-btn" title="Mark all as read" data-ng-click="feedEntries.markAllRead()">
								<span class="glyphicon glyphicon-ok"></span>
							</button>
						</li>
						<li class="btn-group">
							<button type="button" class="btn btn-default navbar-btn" title="Refresh" data-ng-click="feedEntries.refresh()">
								<span class="glyphicon glyphicon-refresh"></span>
							</button>
						</li>
						<li class="btn-group" data-toggle="buttons">
							<label class="btn btn-default navbar-btn" title="All" data-ng-class="{active: viewService.entryFilter.selection == 'all'}" data-ng-click="feedEntries.setSelection('all')">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-th-list"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Unread" data-ng-class="{active: viewService.entryFilter.selection == 'unread'}" data-ng-click="feedEntries.setSelection('unread')">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-unchecked"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Starred" data-ng-class="{active: viewService.entryFilter.selection == 'starred'}" data-ng-click="feedEntries.setSelection('starred')">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-star"></span>
					  		</label>
						</li>
						<li class="btn-group" data-toggle="buttons">
							<label class="btn btn-default navbar-btn" title="Ascending" data-ng-click="feedEntries.setAscendingOrder(true)" data-ng-class="{active: viewService.entryFilter.ascendingOrder}">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-down"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Descending" data-ng-click="feedEntries.setAscendingOrder(false)" data-ng-class="{active: !viewService.entryFilter.ascendingOrder}">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-up"></span>
					  		</label>
						</li>
						<li data-ng-show="!feedEntries.loading">
							<p class="navbar-text">
								<span class="hidden-lg hidden-md hidden-sm">&nbsp;</span>
								<ng-pluralize count="feedEntries.items.length"
									when="{'1': '1 item', 'other': '{} items'}">
								</ng-pluralize>
							</p>
						</li>
						<li data-ng-show="feedEntries.loading">
							<p class="navbar-text">
								<span class="hidden-lg hidden-md hidden-sm">&nbsp;</span>Loading...
							</p>
						</li>
					</ul>
			    	<ul class="nav navbar-nav" data-ng-controller="ArchivesNavbarCtrl" data-ng-show="active">
						<li class="btn-group">
							<button type="button" class="btn btn-default navbar-btn" title="Refresh" data-ng-click="archivedEntries.refresh()">
								<span class="glyphicon glyphicon-refresh"></span>
							</button>
						</li>
						<li class="btn-group" data-toggle="buttons">
							<label class="btn btn-default navbar-btn" title="Ascending" data-ng-click="archivedEntries.setAscendingOrder(true)" data-ng-class="{active: viewService.archiveFilter.ascendingOrder}">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-down"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Descending" data-ng-click="archivedEntries.setAscendingOrder(false)" data-ng-class="{active: !viewService.archiveFilter.ascendingOrder}">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-up"></span>
					  		</label>
						</li>
						<li data-ng-show="!archivedEntries.loading">
							<p class="navbar-text" data-ng-show="!archivedEntries.loading">
								<span class="hidden-lg hidden-md hidden-sm">&nbsp;</span>
								<ng-pluralize count="archivedEntries.items.length"
									when="{'1': '1 item', 'other': '{} items'}">
								</ng-pluralize>
							</p>
						</li>
						<li data-ng-show="archivedEntries.loading">
							<p class="navbar-text" data-ng-show="archivedEntries.loading">
								<span class="hidden-lg hidden-md hidden-sm">&nbsp;</span>Loading...
							</p>
						</li>
					</ul>
				</div>
				<div id="navbar-collapse" class="reader-menu collapse navbar-collapse">
					<ul class="nav navbar-nav hidden-lg hidden-md hidden-sm">
						<li class="menu-item"
							data-ng-class="{selected: menu.homeSelected}"
							data-ng-click="viewService.selectHome()">
							<span class="glyphicon glyphicon-home"></span><span class="title">Home</span>
						</li>
						<li class="menu-item"
							data-ng-class="{selected: menu.settingsSelected}"
							data-ng-click="viewService.selectSettings()">
							<span class="glyphicon glyphicon-cog"></span><span class="title">Settings</span>
						</li>
						<li class="menu-item"
							data-ng-class="{selected: menu.allItemsSelected}"
							data-ng-click="viewService.selectAllItems()">
							<span class="glyphicon glyphicon-th-list"></span>
							<span class="title">All feeds</span>
							<span class="unread-count badge" data-ng-show="subscriptionGroups.unreadCount">{{subscriptionGroups.unreadCount}}</span>
						</li>
					</ul>
					<ul data-ng-repeat="group in subscriptionGroups.items"
						class="menu-group nav navbar-nav hidden-lg hidden-md hidden-sm">
						<li class="menu-item group-item"
							data-ng-class="{selected: group.selected}"
							data-ng-click="viewService.selectSubscriptionGroup(group.id)">
							<span class="glyphicon glyphicon-chevron-right" data-ng-show="group.collapsed" data-ng-click="menu.uncollapse(group.id, $event)"></span>
							<span class="glyphicon glyphicon-chevron-down" data-ng-show="!group.collapsed" data-ng-click="menu.collapse(group.id, $event)"></span>
							<span class="title">{{group.title}}</span>
							<span data-ng-show="group.unreadCount > 0" class="unread-count badge">{{group.unreadCount}}</span>
						</li>
						<li data-ng-repeat="subscription in group.subscriptions"
							class="menu-item feed-item"
							data-ng-class="{selected: subscription.selected}"
							data-ng-show="!group.collapsed"
							data-ng-click="viewService.selectSubscription(group.id, subscription.id)">
							<span class="title">{{subscription.title}}</span>
							<span data-ng-show="subscription.unreadCount > 0" class="unread-count badge">{{subscription.unreadCount}}</span>
						</li>
					</ul>
					<ul class="nav navbar-nav hidden-lg hidden-md hidden-sm">
						<li class="menu-item"
							data-ng-class="{selected: menu.archivedItemsSelected}"
							data-ng-show="archives.length > 0"
							data-ng-click="viewService.selectArchivedItems()">
							<span class="glyphicon glyphicon-chevron-right" data-ng-show="menu.archivedItemsCollapsed" data-ng-click="menu.uncollapseArchivedItems($event)"></span>
							<span class="glyphicon glyphicon-chevron-down" data-ng-show="!menu.archivedItemsCollapsed" data-ng-click="menu.collapseArchivedItems($event)"></span>
							<span class="title">Archives</span>
						</li>
						<li data-ng-show="!menu.archivedItemsCollapsed"
							data-ng-repeat="archive in archives"
							class="menu-item feed-item"
							data-ng-class="{selected: archive.selected}"
							data-ng-click="viewService.selectArchive(archive.id)">
							<span class="title">{{archive.title}}</span>
						</li>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<li class="btn-group">
							<button type="button" class="btn btn-default navbar-btn" title="About" data-toggle="modal" data-target="#aboutModal">
								<span class="glyphicon glyphicon-info-sign"></span>
							</button>
						</li>
						<li>
							<a id="logout-button" href="${logoutUrl}" title="Logout">
								<span class="glyphicon glyphicon-log-out"></span>
								<span class="hidden-lg hidden-md hidden-sm">Logout</span>
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</nav>

<div class="modal fade" id="aboutModal" tabindex="-1" role="dialog" aria-labelledby="aboutModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="aboutModalLabel">About jReader</h4>
			</div>
			<div class="modal-body">Current version: ${appVersion}</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>