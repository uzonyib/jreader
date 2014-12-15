<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="collapse navbar-collapse">
    	<ul class="nav navbar-nav">
			<li class="btn-group">
				<button type="button" class="btn btn-default navbar-btn" title="Refresh" data-ng-click="refreshEntries()">
					<span class="glyphicon glyphicon-refresh"></span>
				</button>
			</li>
			<li class="btn-group" data-toggle="buttons">
				<label class="btn btn-default navbar-btn" title="Ascending" data-ng-click="setAscendingOrder(true)" data-ng-class="{active: filter.ascendingOrder}">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-down"></span>
		  		</label>
		  		<label class="btn btn-default navbar-btn" title="Descending" data-ng-click="setAscendingOrder(false)" data-ng-class="{active: !filter.ascendingOrder}">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-chevron-up"></span>
		  		</label>
			</li>
			<li data-ng-show="!loading">
				<p class="navbar-text">Items displayed:&nbsp;<span>{{entries.length}}</span></p>
			</li>
			<li data-ng-show="loading">
				<p class="navbar-text">Loading...</p>
			</li>
		</ul>
	</div>
</nav>

<div id="feed-entries-container"
	data-infinite-scroll="loadMoreEntries()"
	data-infinite-scroll-distance="1"
	data-infinite-scroll-disabled="!active || ajaxService.loadingEntries || !ajaxService.moreEntriesAvailable"
	data-infinite-scroll-immediate-check="false">
	<table id="feed-entries" class="table table-hover">
		<tr class="article-breadcrumb"
			data-ng-repeat-start="entry in entries">
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
