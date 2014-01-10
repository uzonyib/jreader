<script type="text/x-template" id="template-subscription-menu-group">
	<div class="menu-group{@if cond="{collapsed}"} collapsed{/if}" feed-group="{title}">
		<div class="menu-item group-item{@if cond="{selected}"} selected{/if}" subscription-group-id="{id}" view="items-contents" url="/reader/entries/group/{id}/{~lb}selection{~rb}/{~lb}pageIndex{~rb}?ascending={~lb}ascending{~rb}">
			<span class="icon collapsed-icon"></span>
			<span class="icon uncollapsed-icon"></span>
			<span class="title">{title}</span>
			{@if cond="{unreadCount} > 0"}
				<span class="unread-count">({unreadCount})</span>
			{/if}
		</div>
		{#subscriptions groupId=id}
			<div class="menu-item feed-item{@if cond="{selected}"} selected{/if}" subscription-id="{id}" view="items-contents" url="/reader/entries/group/{groupId}/subscription/{id}/{~lb}selection{~rb}/{~lb}pageIndex{~rb}?ascending={~lb}ascending{~rb}">
				<span class="title">{title}</span>
				{@if cond="{unreadCount} > 0"}
					<span class="unread-count">({unreadCount})</span>
				{/if}
			</div>
		{/subscriptions}
	</div>
</script>

<script type="text/x-template" id="template-subscription-group-settings">
	<div class="settings-group" subscription-group-id="{id}">
		<div class="group-title">
			{?title}{title}{:else}Ungrouped{/title}
		</div>
		{#subscriptions groupTitle=title}
			<div class="settings-item" subscription-id="{id}">
				<div class="set-item-title">
					<input type="text" value="{title}" />
					<button>Change</button>
				</div>
				<button class="unsubscribe-button">Unsubscribe</button>
			</div>
		{/subscriptions}
	</div>
</script>

<script type="text/x-template" id="template-feed-entry">
	<tbody class="feed-entry{^read} unread{/read}" feed-entry-id="{id}" subscription-id="{subscriptionId}" subscription-group-id="{subscriptionGroupId}">
		<tr class="breadcrumb">
			<td class="star-buttons">
				<button class="star"{?starred} style="display: none;"{/starred}></button>
				<button class="unstar"{^starred} style="display: none;"{/starred}></button>
			</td>
			<td class="feed-title">{subscriptionTitle}</td>
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
		<div class="group-title">{title}</div>
		{#subscriptions}
			<div class="subscription-stat" subscription-id="{id}">
				<div class="title">{title}</div>
				<div class="subtitle">
					<a href="{feed.url}">{feed.title}</a>
				</div>
				{?feed.description}<div class="description">{feed.description}</div>{/feed.description}
				<div class="date">Refreshed: {refreshDate}</div>
				<div class="date">Updated: {updatedDate}</div>
			</div>
		{/subscriptions}
	</div>
</script>
