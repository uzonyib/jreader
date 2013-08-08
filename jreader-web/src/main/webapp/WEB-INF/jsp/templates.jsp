<script type="text/x-template" id="template-subscription-menu-group">
	<div class="menu-group collapsed">
		<div>
			<span class="group-collapse">
				<img src="/images/arrow_right.png" height="12" width="12" class="collapsed">
				<img src="/images/arrow_down.png" height="12" width="12" class="uncollapsed">
			</span>
			<span class="group-title">
				{?title}{title}{:else}Ungrouped{/title}
				{@if cond="{unreadCount} > 0"}
					<span class="unread-count">({unreadCount})</span>
				{/if}
			</span>
		</div>
		{#subscriptions}
			<div class="menu-item" feed-id="{feed.id}">
				{title}
				{@if cond="{unreadCount} > 0"}
					<span class="unread-count">({unreadCount})</span>
				{/if}
			</div>
		{/subscriptions}
	</div>
</script>

<script type="text/x-template" id="template-subscription-group-settings">
	<div class="settings-group">
		<div class="group-title">
			{?title}{title}{:else}Ungrouped{/title}
		</div>
		{#subscriptions groupTitle=title}
			<div class="settings-item" feed-id="{feed.id}">
				<div class="set-item-title">
					<input type="text" value="{title}" />
					<button>Change</button>
				</div>
				<div class="set-group-title">
					<input class="set-group-title" type="text" value="{groupTitle}" />
					<button>Group</button>
				</div>
				<button class="unsubscribe-button">Unsubscribe</button>
			</div>
		{/subscriptions}
	</div>
</script>

<script type="text/x-template" id="template-feed-entry">
	<div class="feed-entry{^read} unread{/read}" feed-entry-id="{id}">
		<div class="breadcrumb">
			<div class="star-buttons">
				<button class="star"{?starred} style="display: none;"{/starred}>Star</button>
				<button class="unstar"{^starred} style="display: none;"{/starred}>Unstar</button>
			</div>
			<div class="feed-title" feed-id="{feedId}">{subscriptionTitle}</div>
			<div class="title">
				{title}
			</div>
			<div class="date">
				{publishedDate}
			</div>
		</div>
		<div class="detail" style="display: none;">
			<div class="link">
				<a target=_blank href="{link}">Open</a>
			</div>
			<div class="description">
				{description|s}
			</div>
		</div>
	</div>
</script>
