<div id="posts-container"
	data-infinite-scroll="posts.loadMore()"
	data-infinite-scroll-distance="1"
	data-infinite-scroll-disabled="!posts.visible || posts.loading || !posts.moreItemsAvailable"
	data-infinite-scroll-immediate-check="false">
	<table id="posts" class="table">
		<tr class="article-breadcrumb"
			data-ng-class="{info: !post.read, selected: post.expanded}"
			data-ng-repeat-start="post in posts.items">
			<td class="action-buttons">
				<button class="btn btn-default bookmark" title="Bookmark" data-ng-show="!post.bookmarked && !post.bookmarking" data-ng-click="bookmark(post)">
					<span class="glyphicon glyphicon-bookmark"></span>
				</button>
				<button class="btn btn-default delete-bookmark" title="Delete bookmark" data-ng-show="post.bookmarked && !post.bookmarking" data-ng-click="deleteBookmark(post)">
					<span class="glyphicon glyphicon-bookmark"></span>
				</button>
				<span class="glyphicon glyphicon-hourglass" data-ng-show="post.bookmarking"></span>
			</td>
			<td class="feed-title" data-ng-click="toggleExpansion(post);posts.markRead(post);">{{::post.subscriptionTitle}}</td>
			<td class="title" data-ng-click="toggleExpansion(post);posts.markRead(post);">{{::post.title}}</td>
			<td class="date hidden-sm hidden-xs" data-ng-click="toggleExpansion(post);posts.markRead(post);">{{::post.publishDate | moment}}</td>
		</tr>
		<tr class="article-detail"
			data-ng-if="post.expanded"
			data-ng-repeat-end="">
			<td colspan="4">
				<div class="header">
					<div>
						<a target=_blank href="{{::post.link}}" title="Open">
							<span class="glyphicon glyphicon-new-window"></span>
							<span class="title">{{::post.title}}</span>
						</a>
					</div>
					<div>
						<span data-ng-if="::post.author">by {{::post.author}}<span class="hidden-lg hidden-md">,</span></span>
						<span class="hidden-lg hidden-md">{{::post.publishDate | moment}}</span>
					</div>
				</div>
				<div class="description" data-ng-bind-html="::post.description"></div>
				<div class="footer" data-ng-if="archives.items.length > 0">
					<form class="form-inline" data-ng-if="!post.archived && !post.archiving" data-ng-submit="archive(post)">
						<span>Archive to</span>
						<div class="form-group">
							<div class="dropdown">
								<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
									{{post.archive.title}}<span class="caret"></span>
								</button>
								<ul class="dropdown-menu">
									<li data-ng-repeat="archive in archives.items" data-ng-click="post.archive=archive">
										<a tabindex="-1" href="">{{archive.title}}</a>
									</li>
								</ul>
							</div>
						</div>
						<button type="submit" class="btn btn-default" title="Archive">
							<span class="glyphicon glyphicon-ok"></span>
						</button>
					</form>
					<span data-ng-if="post.archived && !post.archiving">Archived to {{post.archive.title}}</span>
					<span class="glyphicon glyphicon-hourglass" data-ng-show="post.archiving"></span>
				</div>
			</td>
		</tr>
	</table>
</div>
