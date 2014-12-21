<div id="feed-entries-container"
	data-infinite-scroll="loadMoreArchivedEntries()"
	data-infinite-scroll-distance="1"
	data-infinite-scroll-disabled="!active || ajaxService.loadingEntries || !ajaxService.moreEntriesAvailable"
	data-infinite-scroll-immediate-check="false">
	<table id="feed-entries" class="table">
		<tr class="article-breadcrumb"
			data-ng-repeat-start="entry in archivedEntries">
			<td class="action-buttons">
				<button class="btn btn-default delete" title="Delete" data-ng-click="deleteEntry(entry)">
					<span class="glyphicon glyphicon-remove"></span>
				</button>
			</td>
			<td class="feed-title" data-ng-click="toggleCollapsion(entry)">{{entry.subscriptionTitle}}</td>
			<td class="title" data-ng-click="toggleCollapsion(entry)">{{entry.title}}</td>
			<td class="date" data-ng-click="toggleCollapsion(entry)">{{entry.publishedDate | moment}}</td>
		</tr>
		<tr class="article-detail"
			data-ng-show="entry.uncollapsed"
			data-ng-repeat-end="">
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
	</table>
</div>
