<script type="text/x-template" id="template-subscription-menu-group">
	<div class="menu-group menu-item{cssClass}" feed-group="{?title}{title}{:else}Ungrouped{/title}">
		<div>
			<span class="group-collapse">
				<span class="collapsed"></span>
				<span class="uncollapsed"></span>
			</span>
			<span class="group-title">
				{?title}{title}{:else}Ungrouped{/title}
			</span>
			<span class="unread-count">
				{@if cond="{unreadCount} > 0"}
					({unreadCount})
				{/if}
			</span>
		</div>
		{#subscriptions}
			<div class="menu-item feed-item{cssClass}" feed-id="{feed.id}">
				<span class="title">{title}</span>
				<span class="unread-count">
					{@if cond="{unreadCount} > 0"}
						({unreadCount})
					{/if}
				</span>
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
	<tbody class="feed-entry{^read} unread{/read}" feed-entry-id="{id}">
		<tr class="breadcrumb">
			<td class="star-buttons">
				<button class="star"{?starred} style="display: none;"{/starred}></button>
				<button class="unstar"{^starred} style="display: none;"{/starred}></button>
			</td>
			<td class="feed-title" feed-id="{feedId}">{subscriptionTitle}</td>
			<td class="title">{title}</td>
			<td class="date">{publishedDate}</td>
		</tr>
		<tr class="detail" style="display: none;">
			<td colspan="4">
				<div class="link">
					<a target=_blank href="{link}">Open</a>
				</div>
				<div class="description">{description|s}</div>
			</td>
		</tr>
	</tbody>
</script>

<script type="text/x-template" id="template-subscription-group-stat">
	<div class="subscription-group-stat">
		<div class="group-title">
			{?title}{title}{:else}Ungrouped{/title}
		</div>
		{#subscriptions}
			<div class="subscription-stat" feed-id="{feed.id}">
				<div class="title">
					<span class="feed-title">{title}</span>
					({feed.title} - {feed.url})
				</div>
				{?feed.description}<div class="description">{feed.description}</div>{/feed.description}
				<div class="updated-date">Updated: {feed.updatedDate}</div>
			</div>
		{/subscriptions}
	</div>
</script>
