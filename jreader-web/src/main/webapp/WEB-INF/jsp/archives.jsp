<div class="btn-toolbar" role="toolbar">
	<div class="actions btn-group" role="group">
		<button type="button" class="btn btn-default" id="refresh" title="Refresh" data-ng-click="refreshEntries()">
			<span class="glyphicon glyphicon-refresh"></span>
		</button>
	</div>
	<div class="items-order btn-group" role="group">
		<button id="items-order-asc" type="button" class="btn btn-default" title="Ascending" data-ng-click="setAscendingOrder(true)" data-ng-class="{selected: filter.ascendingOrder}">
			<span class="glyphicon glyphicon-chevron-down"></span>
		</button>
		<button id="items-order-desc" type="button" class="btn btn-default" title="Descending" data-ng-click="setAscendingOrder(false)" data-ng-class="{selected: !filter.ascendingOrder}">
			<span class="glyphicon glyphicon-chevron-up"></span>
		</button>
	</div>
	<div class="status">
		<span data-ng-if="!loading">Items displayed:&nbsp;<span>{{entries.length}}</span></span>
		<span data-ng-if="loading">Loading...</span>
	</div>
</div>
<div id="feed-entries-container"
	infinite-scroll="loadMoreEntries()"
	infinite-scroll-distance="1"
	infinite-scroll-disabled="!active || ajaxService.loadingEntries || !ajaxService.moreEntriesAvailable"
	infinite-scroll-immediate-check="false">
	<table id="feed-entries" class="table">
		<tbody data-ng-repeat="entry in entries" class="feed-entry">
			<tr class="article-breadcrumb">
				<td class="action-buttons">
					<button class="btn btn-default delete" title="Delete" data-ng-click="deleteEntry(entry)">
						<span class="glyphicon glyphicon-remove"></span>
					</button>
				</td>
				<td class="feed-title" data-ng-click="toggleCollapsion(entry)">{{entry.subscriptionTitle}}</td>
				<td class="title" data-ng-click="toggleCollapsion(entry)">{{entry.title}}</td>
				<td class="date" data-ng-click="toggleCollapsion(entry)">{{entry.publishedDate | moment}}</td>
			</tr>
			<tr class="article-detail" data-ng-show="entry.uncollapsed">
				<td colspan="4">
					<div class="header">
						<div>
							<a target=_blank href="{{entry.link}}" title="Open">
								<span class="glyphicon glyphicon-new-window"></span>
								<span class="title">{{entry.title}}</span>
							</a>
						</div>
						<div class="author" data-ng-if="entry.author">
							Author: {{entry.author}}
						</div>
					</div>
					<div class="description" data-ng-bind-html="entry.description"></div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
