<div id="nav-bar">
	<span class="actions">
		<button id="refresh" title="Refresh" ng-click="refreshEntries()">
			<img src="/images/refresh_black.png">
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
	infinite-scroll-disabled="!active || ajaxService.loadingEntries || !ajaxService.moreEntriesAvailable"
	infinite-scroll-immediate-check="false">
	<table id="feed-entries">
		<tbody ng-repeat="entry in entries" class="feed-entry" ng-class="{unread: !entry.read}">
			<tr class="breadcrumb">
				<td class="feed-title" ng-click="toggleCollapsion(entry)">{{entry.subscriptionTitle}}</td>
				<td class="title" ng-click="toggleCollapsion(entry)">{{entry.title}}</td>
				<td class="date" ng-click="toggleCollapsion(entry)">{{entry.publishedDate | moment}}</td>
			</tr>
			<tr class="detail" ng-show="entry.uncollapsed">
				<td colspan="4">
					<div class="link">
						<a target=_blank href="{{entry.link}}">Open</a>
						<button class="delete-button" title="Delete" ng-click="deleteEntry(entry)">
							<img src="/images/cross_black.png">
						</button>
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
