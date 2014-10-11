<div id="subscription-group-stats">
	<div ng-repeat="group in subscriptionGroups" class="subscription-group-stat">
		<div class="group-title">{{group.title}}</div>
		<div ng-repeat="subscription in group.subscriptions" class="subscription-stat">
			<span class="title">{{subscription.title}}</span>
			&nbsp;
			<span class="subtitle">
				(<a href="{{subscription.feed.url}}">{{subscription.feed.title}}</a>)
			</span>
			<div ng-if="subscription.feed.description" class="description">{{subscription.feed.description}}</div>
			<div class="stats">
				<div>Refreshed<br /><span>{{subscription.refreshDate | moment}}</span></div>
				<div>Updated<br /><span>{{subscription.updatedDate | moment}}</span></div>
				<div>Unread<br /><span>{{subscription.unreadCount}}</span></div>
			</div>
		</div>
	</div>
</div>