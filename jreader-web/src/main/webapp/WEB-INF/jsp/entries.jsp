<div id="feed-entries-container"
	data-infinite-scroll="loadMoreEntries()"
	data-infinite-scroll-distance="1"
	data-infinite-scroll-disabled="!active || ajaxService.loadingEntries || !ajaxService.moreEntriesAvailable"
	data-infinite-scroll-immediate-check="false">
	<table id="feed-entries" class="table">
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
