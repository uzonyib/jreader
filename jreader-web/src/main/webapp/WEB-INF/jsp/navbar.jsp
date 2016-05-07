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
						<span class="glyphicon glyphicon-menu-hamburger"></span>
					</button>
			    	<ul class="nav navbar-nav" data-ng-controller="PostsNavbarCtrl" data-ng-show="active">
			    		<li class="btn-group">
							<button type="button" class="btn btn-default navbar-btn" title="Mark all as read (M)"
								data-ng-init="shortcuts.register('m', viewService.isPostsSelected, posts.markAllRead)"
								data-ng-click="posts.markAllRead()">
								<span class="glyphicon glyphicon-ok"></span>
							</button>
						</li>
						<li class="btn-group">
							<button type="button" class="btn btn-default navbar-btn" title="Refresh (R)"
								data-ng-init="shortcuts.register('r', viewService.isPostsSelected, posts.refreshWithSubscriptions)"
								data-ng-click="posts.refreshWithSubscriptions()">
								<span class="glyphicon glyphicon-refresh"></span>
							</button>
						</li>
						<li class="btn-group" data-toggle="buttons">
							<label class="btn btn-default navbar-btn" title="All (1)"
								data-ng-class="{active: viewService.postFilter.selection == 'all'}"
								data-ng-init="shortcuts.register('1', viewService.isPostsSelected, posts.setSelectionToAll)"
								data-ng-click="posts.setSelectionToAll()">
					    		<input type="radio" name="posts-vertical"><span class="glyphicon glyphicon-th-list"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Unread (2)"
					  			data-ng-class="{active: viewService.postFilter.selection == 'unread'}"
					  			data-ng-init="shortcuts.register('2', viewService.isPostsSelected, posts.setSelectionToUnread)"
					  			data-ng-click="posts.setSelectionToUnread()">
					    		<input type="radio" name="posts-vertical"><span class="glyphicon glyphicon-unchecked"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Bookmarked (3)"
					  			data-ng-class="{active: viewService.postFilter.selection == 'bookmarked'}"
					  			data-ng-init="shortcuts.register('3', viewService.isPostsSelected, posts.setSelectionToBookmarked)"
					  			data-ng-click="posts.setSelectionToBookmarked()">
					    		<input type="radio" name="posts-vertical"><span class="glyphicon glyphicon-bookmark"></span>
					  		</label>
						</li>
						<li class="btn-group" data-toggle="buttons">
							<label class="btn btn-default navbar-btn" title="Ascending (A)"
								data-ng-class="{active: viewService.postFilter.ascendingOrder}"
								data-ng-init="shortcuts.register('a', viewService.isPostsSelected, posts.setOrderToAscending)"
								data-ng-click="posts.setOrderToAscending()">
					    		<input type="radio" name="posts-order"><span class="glyphicon glyphicon-chevron-down"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Descending (D)"
					  			data-ng-class="{active: !viewService.postFilter.ascendingOrder}"
					  			data-ng-init="shortcuts.register('d', viewService.isPostsSelected, posts.setOrderToDescending)"
					  			data-ng-click="posts.setOrderToDescending()">
					    		<input type="radio" name="posts-order"><span class="glyphicon glyphicon-chevron-up"></span>
					  		</label>
						</li>
						<li data-ng-show="!posts.loading">
							<p class="navbar-text">
								<span class="hidden-lg hidden-md hidden-sm">&nbsp;</span>
								<ng-pluralize count="posts.items.length"
									when="{'1': '1 item', 'other': '{} items'}">
								</ng-pluralize>
							</p>
						</li>
						<li data-ng-show="posts.loading">
							<p class="navbar-text">
								<span class="hidden-lg hidden-md hidden-sm">&nbsp;</span>Loading...
							</p>
						</li>
					</ul>
			    	<ul class="nav navbar-nav" data-ng-controller="ArchivesNavbarCtrl" data-ng-show="active">
						<li class="btn-group">
							<button type="button" class="btn btn-default navbar-btn" title="Refresh (R)"
								data-ng-init="shortcuts.register('r', viewService.isArchivesSelected, archivedPosts.refresh)"
								data-ng-click="archivedPosts.refresh()">
								<span class="glyphicon glyphicon-refresh"></span>
							</button>
						</li>
						<li class="btn-group" data-toggle="buttons">
							<label class="btn btn-default navbar-btn" title="Ascending (A)"
								data-ng-class="{active: viewService.archiveFilter.ascendingOrder}"
								data-ng-init="shortcuts.register('a', viewService.isArchivesSelected, archivedPosts.setOrderToAscending)"
								data-ng-click="archivedPosts.setAscendingOrder(true)">
					    		<input type="radio" name="posts-order"><span class="glyphicon glyphicon-chevron-down"></span>
					  		</label>
					  		<label class="btn btn-default navbar-btn" title="Descending (D)"
					  			data-ng-class="{active: !viewService.archiveFilter.ascendingOrder}"
					  			data-ng-init="shortcuts.register('d', viewService.isArchivesSelected, archivedPosts.setOrderToDescending)"
					  			data-ng-click="archivedPosts.setAscendingOrder(false)">
					    		<input type="radio" name="posts-order"><span class="glyphicon glyphicon-chevron-up"></span>
					  		</label>
						</li>
						<li data-ng-show="!archivedPosts.loading">
							<p class="navbar-text" data-ng-show="!archivedPosts.loading">
								<span class="hidden-lg hidden-md hidden-sm">&nbsp;</span>
								<ng-pluralize count="archivedPosts.items.length"
									when="{'1': '1 item', 'other': '{} items'}">
								</ng-pluralize>
							</p>
						</li>
						<li data-ng-show="archivedPosts.loading">
							<p class="navbar-text" data-ng-show="archivedPosts.loading">
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
					<ul class="nav navbar-nav hidden-lg hidden-md hidden-sm">
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
				<p>M - Mark all posts read</p>
				<p>R - Refresh posts</p>
				<p>1 - Show all posts</p>
				<p>2 - Show unread posts</p>
				<p>3 - Show bookmarked posts</p>
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