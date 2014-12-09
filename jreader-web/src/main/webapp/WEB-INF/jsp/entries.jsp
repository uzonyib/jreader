<div class="btn-toolbar" role="toolbar">
	<div class="actions btn-group" role="group">
		<button type="button" class="btn btn-default" id="mark-all-read" title="Mark all as read" data-ng-click="markAllRead()">
			<span class="glyphicon glyphicon-ok"></span>
		</button>
	</div>
	<div class="actions btn-group" role="group">
		<button type="button" class="btn btn-default" id="refresh" title="Refresh" data-ng-click="refreshEntries()">
			<span class="glyphicon glyphicon-refresh"></span>
		</button>
	</div>
	<div class="items-selection btn-group" role="group">
		<button type="button" class="btn btn-default" id="items-selection-all" data-ng-class="{selected: filter.selection == 'all'}" title="All" data-ng-click="setSelection('all')">
			<span class="glyphicon glyphicon-th-list"></span>
		</button>
		<button id="items-selection-unread" type="button" class="btn btn-default" data-ng-class="{selected: filter.selection == 'unread'}" title="Unread" data-ng-click="setSelection('unread')">
			<span class="glyphicon glyphicon-unchecked"></span>
		</button>
		<button id="items-selection-starred" type="button" class="btn btn-default" data-ng-class="{selected: filter.selection == 'starred'}" title="Starred" data-ng-click="setSelection('starred')">
			<span class="glyphicon glyphicon-star"></span>
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
		<span data-ng-show="!loading">Items displayed:&nbsp;<span>{{entries.length}}</span></span>
		<span data-ng-show="loading">Loading...</span>
	</div>
</div>

<div id="feed-entries-container"
	infinite-scroll="loadMoreEntries()"
	infinite-scroll-distance="1"
	infinite-scroll-disabled="!active || ajaxService.loadingEntries || !ajaxService.moreEntriesAvailable"
	infinite-scroll-immediate-check="false">
	<table id="feed-entries" class="table">
		<tbody data-ng-repeat="entry in entries" class="feed-entry" data-ng-class="{unread: !entry.read}">
			<tr class="article-breadcrumb">
				<td class="action-buttons">
					<button class="btn btn-default star" title="Star" data-ng-show="!entry.starred" data-ng-click="star(entry)">
						<span class="glyphicon glyphicon-star"></span>
					</button>
					<button class="btn btn-default unstar" title="Unstar" data-ng-show="entry.starred" data-ng-click="unstar(entry)">
						<span class="glyphicon glyphicon-star"></span>
					</button>
				</td>
				<td class="feed-title" data-ng-click="toggleCollapsion(entry)">{{entry.subscriptionTitle}}</td>
				<td class="title" data-ng-click="toggleCollapsion(entry)">{{entry.title}}</td>
				<td class="date" data-ng-click="toggleCollapsion(entry)">{{entry.publishedDate | moment}}</td>
			</tr>
			<tr class="article-detail" data-ng-show="entry.uncollapsed">
				<td colspan="4">
					<div class="header">
						<div class="link">
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
					<div class="footer" data-ng-if="archives.length > 0">
						<form class="form-inline" data-ng-if="!entry.archived" data-ng-submit="archive(entry)">
							<span>Archive to</span>
							<div class="form-group">
								<div class="dropdown">
									<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
										{{entry.archive.title}} <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li data-ng-repeat="archive in archives" data-ng-click="entry.archive=archive">
											<a tabindex="-1" href="">{{archive.title}}</a>
										</li>
									</ul>
								</div>
							</div>
							<button type="submit" class="btn btn-default" title="Archive">
								<span class="glyphicon glyphicon-ok"></span>
							</button>
						</form>
						<span data-ng-if="entry.archived">Archived to {{entry.archive.title}}</span>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
