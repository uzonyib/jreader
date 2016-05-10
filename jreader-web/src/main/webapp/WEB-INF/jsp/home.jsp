<div id="subscription-group-stats">
	<div data-ng-repeat="group in groups.items" class="subscription-group-stat">
		<div class="group-title">{{group.title}}</div>
		<div data-ng-repeat="subscription in group.subscriptions" class="subscription-stat">
			<span class="status ok" data-ng-show="subscription.feed.status == 0" title="OK">&#x25cf;</span>
			<span class="status warning" data-ng-show="subscription.feed.status == 1 || subscription.feed.status == 2" title="Instability">&#x25cf;</span>
			<span class="status error" data-ng-show="subscription.feed.status > 2" title="Error">&#x25cf;</span>
			<span class="title">{{subscription.title}}</span>
			<span class="subtitle">&nbsp;(<a href="{{subscription.feed.url}}" target=_blank>{{subscription.feed.title}}</a>)</span>
			<div data-ng-if="subscription.feed.description" class="description">{{subscription.feed.description}}</div>
			<div class="stats container-fluid">
				<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">Refreshed<br /><span>{{subscription.feed.lastRefreshDate | moment}}</span></div>
				<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">Updated<br /><span>{{subscription.feed.lastUpdateDate | moment}}</span></div>
				<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">Unread<br /><span>{{subscription.unreadCount}}</span></div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<canvas class="chart chart-line"
						data-ng-if="active"
						data-chart-options="chart.options"
						data-chart-data="subscription.stats.data"
						data-chart-labels="subscription.stats.labels">
					</canvas>
				</div>
			</div>
		</div>
	</div>
</div>