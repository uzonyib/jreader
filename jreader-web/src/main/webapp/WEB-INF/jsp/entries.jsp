<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="collapse navbar-collapse">
    	<ul class="nav navbar-nav">
    		<li class="btn-group">
				<button type="button" class="btn btn-default navbar-btn" title="Mark all as read" data-ng-click="markAllRead()">
					<span class="glyphicon glyphicon-ok"></span>
				</button>
			</li>
			<li class="btn-group">
				<button type="button" class="btn btn-default navbar-btn" title="Refresh" data-ng-click="refreshEntries()">
					<span class="glyphicon glyphicon-refresh"></span>
				</button>
			</li>
			<li class="btn-group" data-toggle="buttons">
				<label class="btn btn-default navbar-btn" title="All" data-ng-class="{active: filter.selection == 'all'}" data-ng-click="setSelection('all')">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-th-list"></span>
		  		</label>
		  		<label class="btn btn-default navbar-btn" title="Unread" data-ng-class="{active: filter.selection == 'unread'}" data-ng-click="setSelection('unread')">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-unchecked"></span>
		  		</label>
		  		<label class="btn btn-default navbar-btn" title="Starred" data-ng-class="{active: filter.selection == 'starred'}" data-ng-click="setSelection('starred')">
		    		<input type="radio" name="entries-order"><span class="glyphicon glyphicon-star"></span>
		  		</label>
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
	infinite-scroll="loadMoreEntries()"
	infinite-scroll-distance="1"
	infinite-scroll-disabled="!active || ajaxService.loadingEntries || !ajaxService.moreEntriesAvailable"
	infinite-scroll-immediate-check="false">
	<table id="feed-entries" class="table table-hover">
		<tr class="article-breadcrumb"
			data-ng-class="{info: !entry.read}"
			data-ng-repeat-start="entry in entries">
			<td class="action-buttons">
				<button class="btn btn-default star" title="Star" data-ng-show="!entry.starred" data-ng-click="star(entry)">
					<span class="glyphicon glyphicon-star-empty"></span>
				</button>
				<button class="btn btn-default unstar" title="Unstar" data-ng-show="entry.starred" data-ng-click="unstar(entry)">
					<span class="glyphicon glyphicon-star"></span>
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
				<div class="footer" data-ng-if="archives.length > 0">
					<form class="form-inline" data-ng-if="!entry.archived" data-ng-submit="archive(entry)">
						<span>Archive to</span>
						<div class="form-group">
							<div class="dropdown">
								<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
									{{entry.archive.title}}<span class="caret"></span>
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
	</table>
</div>
