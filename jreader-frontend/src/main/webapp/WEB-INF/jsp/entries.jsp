<div id="nav-bar">
	<span class="actions">
		<button id="mark-all-read" title="Mark all as read" ng-click="markAllRead()">
			<img src="/images/tick_black.png">
		</button>
		<button id="refresh" title="Refresh" ng-click="refreshEntries()">
			<img src="/images/refresh_black.png">
		</button>
	</span>
	<span class="items-selection">
		<button id="items-selection-all" ng-class="{selected: filter.selection == 'all'}" title="All" ng-click="setSelection('all')">
			<img src="/images/list_black.png">
		</button><button
				id="items-selection-unread" ng-class="{selected: filter.selection == 'unread'}" title="Unread" ng-click="setSelection('unread')">
			<img src="/images/square_black.png">
		</button><button
				id="items-selection-starred" ng-class="{selected: filter.selection == 'starred'}" title="Starred" ng-click="setSelection('starred')">
			<img src="/images/star_black.png">
		</button>
	</span>
	<span class="items-order">
		<button id="items-order-asc" title="Ascending" ng-click="setAscendingOrder(true)" ng-class="{selected: filter.ascendingOrder}">
			<img src="/images/down_black.png">
		</button><button
				id="items-order-desc" title="Descending" ng-click="setAscendingOrder(false)" ng-class="{selected: !filter.ascendingOrder}">
			<img src="/images/up_black.png">
		</button>
	</span>
	<span id="status-bar-count" ng-if="!loading">Items displayed:&nbsp;<span id="entry-count">{{entries.length}}</span></span>
	<span id="status-bar-loading" ng-if="loading">Loading...</span>
</div>
<div id="feed-entries-container"
	infinite-scroll="loadMoreEntries()"
	infinite-scroll-distance="1"
	infinite-scroll-disabled="ajaxService.loadingEntries || !ajaxService.moreEntriesAvailable"
	infinite-scroll-immediate-check="false">
	<table id="feed-entries">
		<tbody ng-repeat="entry in entries" class="feed-entry" ng-class="{unread: !entry.read}">
			<tr class="breadcrumb">
				<td class="star-buttons">
					<button class="star" ng-show="!entry.starred" ng-click="star(entry)"></button>
					<button class="unstar" ng-show="entry.starred" ng-click="unstar(entry)"></button>
				</td>
				<td class="feed-title" ng-click="toggleCollapsion(entry)">{{entry.subscriptionTitle}}</td>
				<td class="title" ng-click="toggleCollapsion(entry)">{{entry.title}}</td>
				<td class="date" ng-click="toggleCollapsion(entry)">{{entry.publishedDate | moment}}</td>
			</tr>
			<tr class="detail" ng-show="entry.uncollapsed">
				<td colspan="4">
					<div class="link">
						<a target=_blank href="{{entry.link}}">Open</a>
					</div>
					<div class="author" ng-if="entry.author">
						Author: {{entry.author}}
					</div>
					<div class="description" ng-bind-html="entry.description"></div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
