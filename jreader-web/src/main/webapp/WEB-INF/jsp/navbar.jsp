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
							<button type="button" class="btn btn-default navbar-btn" title="Mark all as read (M)"
								data-ng-init="shortcuts.register('m', viewService.isEntriesSelected, feedEntries.markAllRead)"
								data-ng-click="feedEntries.markAllRead()">
								<span class="glyphicon glyphicon-ok"></span>
							</button>
						</li>
						<li class="btn-group">
							<button type="button" class="btn btn-default navbar-btn" title="Refresh (R)"
								data-ng-init="shortcuts.register('r', viewService.isEntriesSelected, feedEntries.refreshWithSubscriptions)"
								data-ng-click="feedEntries.refreshWithSubscriptions()">
								<span class="glyphicon glyphicon-refresh"></span>
							</button>
						</li>
						<li class="btn-group" data-toggle="buttons">
							<label class="btn btn-default navbar-btn" title="All (1)"
								data-ng-class="{active: viewService.entryFilter.selection == 'all'}"
								data-ng-init="shortcuts.register('1', viewService.isEntriesSelected, feedEntries.setSelectionToAll)"
								data-ng-click="feedEntries.setSelectionToAll()">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-th-list"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Unread (2)"
					  			data-ng-class="{active: viewService.entryFilter.selection == 'unread'}"
					  			data-ng-init="shortcuts.register('2', viewService.isEntriesSelected, feedEntries.setSelectionToUnread)"
					  			data-ng-click="feedEntries.setSelectionToUnread()">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-unchecked"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Starred (3)"
					  			data-ng-class="{active: viewService.entryFilter.selection == 'starred'}"
					  			data-ng-init="shortcuts.register('3', viewService.isEntriesSelected, feedEntries.setSelectionToStarred)"
					  			data-ng-click="feedEntries.setSelectionToStarred()">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-star"></span>
					  		</label>
						</li>
						<li class="btn-group" data-toggle="buttons">
							<label class="btn btn-default navbar-btn" title="Ascending (A)"
								data-ng-class="{active: viewService.entryFilter.ascendingOrder}"
								data-ng-init="shortcuts.register('a', viewService.isEntriesSelected, feedEntries.setOrderToAscending)"
								data-ng-click="feedEntries.setOrderToAscending()">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-down"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Descending (D)"
					  			data-ng-class="{active: !viewService.entryFilter.ascendingOrder}"
					  			data-ng-init="shortcuts.register('d', viewService.isEntriesSelected, feedEntries.setOrderToDescending)"
					  			data-ng-click="feedEntries.setOrderToDescending()">
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
							<button type="button" class="btn btn-default navbar-btn" title="Refresh (R)"
								data-ng-init="shortcuts.register('r', viewService.isArchivesSelected, archivedEntries.refresh)"
								data-ng-click="archivedEntries.refresh()">
								<span class="glyphicon glyphicon-refresh"></span>
							</button>
						</li>
						<li class="btn-group" data-toggle="buttons">
							<label class="btn btn-default navbar-btn" title="Ascending (A)"
								data-ng-class="{active: viewService.archiveFilter.ascendingOrder}"
								data-ng-init="shortcuts.register('a', viewService.isArchivesSelected, archivedEntries.setOrderToAscending)"
								data-ng-click="archivedEntries.setAscendingOrder(true)">
					    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-down"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Descending (D)"
					  			data-ng-class="{active: !viewService.archiveFilter.ascendingOrder}"
					  			data-ng-init="shortcuts.register('d', viewService.isArchivesSelected, archivedEntries.setOrderToDescending)"
					  			data-ng-click="archivedEntries.setAscendingOrder(false)">
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
							<span class="unread-count badge" data-ng-show="groups.unreadCount">{{groups.unreadCount}}</span>
						</li>
					</ul>
					<ul data-ng-repeat="group in groups.items"
						class="menu-group nav navbar-nav hidden-lg hidden-md hidden-sm">
						<li class="menu-item group-item"
							data-ng-class="{selected: group.selected}"
							data-ng-click="viewService.selectGroup(group.id)">
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
				<h3 class="modal-title" id="aboutModalLabel">Welcome to jReader!</h3>
			</div>
			<div class="modal-body">
				<h4>Keyboard shortcuts</h4>
				<p>M - Mark all entries as read</p>
				<p>R - Refresh entries</p>
				<p>1 - Show all entries</p>
				<p>2 - Show unread entries</p>
				<p>3 - Show starred entries</p>
				<p>A - Ascending order</p>
				<p>D - Descending order</p>
				<hr>
				<h4>Current version: ${appVersion}</h4>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>