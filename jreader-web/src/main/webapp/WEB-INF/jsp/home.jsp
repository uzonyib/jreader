<div id="subscription-group-stats">
	<div data-ng-repeat="group in subscriptionGroups" class="subscription-group-stat">
		<div class="group-title">{{group.title}}</div>
		<div data-ng-repeat="subscription in group.subscriptions" class="subscription-stat">
			<span class="title">{{subscription.title}}</span>
			&nbsp;
			<span class="subtitle">
				(<a href="{{subscription.feed.url}}" target=_blank>{{subscription.feed.title}}</a>)
			</span>
			<div data-ng-if="subscription.feed.description" class="description">{{subscription.feed.description}}</div>
			<div class="stats container-fluid">
				<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">Refreshed<br /><span>{{subscription.refreshDate | moment}}</span></div>
				<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">Updated<br /><span>{{subscription.updatedDate | moment}}</span></div>
				<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">Unread<br /><span>{{subscription.unreadCount}}</span></div>
			</div>
		</div>
	</div>
</div>