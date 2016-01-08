<div id="posts-container"
	data-infinite-scroll="archivedPosts.loadMore()"
	data-infinite-scroll-distance="1"
	data-infinite-scroll-disabled="!archivedPosts.visible || archivedPosts.loading || !archivedPosts.moreItemsAvailable"
	data-infinite-scroll-immediate-check="false">
	<table id="posts" class="table">
		<tr class="article-breadcrumb"
			data-ng-repeat-start="post in archivedPosts.items">
			<td class="action-buttons">
				<button class="btn btn-default delete" title="Delete" data-ng-click="deletePost(psot)">
					<span class="glyphicon glyphicon-remove"></span>
				</button>
			</td>
			<td class="title" data-ng-click="toggleExpansion(post)">{{post.title}}</td>
			<td class="date hidden-sm hidden-xs" data-ng-click="toggleExpansion(post)">{{post.publishDate | moment}}</td>
		</tr>
		<tr class="article-detail"
			data-ng-if="post.expanded"
			data-ng-repeat-end="">
			<td colspan="4">
				<div class="header">
					<div>
						<a target=_blank href="{{post.link}}" title="Open">
							<span class="glyphicon glyphicon-new-window"></span>
							<span class="title">{{post.title}}</span>
						</a>
					</div>
					<div>
						<span data-ng-if="post.author">by {{post.author}}<span class="hidden-lg hidden-md">,</span></span>
						<span class="hidden-lg hidden-md">{{post.publishDate | moment}}</span>
					</div>
				</div>
				<div class="description" data-ng-bind-html="post.description"></div>
			</td>
		</tr>
	</table>
</div>
