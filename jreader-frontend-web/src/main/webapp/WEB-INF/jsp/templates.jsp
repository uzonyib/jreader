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
			<span>{title}</span>
			{^subscriptions}
				<form method="post" action="/reader/delete">
					<input type="hidden" name="subscriptionGroupId"  value="{id}" />
					<input type="image" src="/images/cross_black.png" title="delete" />
				</form>
			{/subscriptions}
		</div>
		{#subscriptions groupId=id}
			<div class="settings-item" subscription-id="{id}">
				<span class="title">{title}</span>
				<form class="set-item-title hidden" method="post" action="/reader/entitle">
					<input type="text" name="title" placeholder="name" value="{title}" />
					<input type="hidden" name="subscriptionGroupId"  value="{groupId}" />
					<input type="hidden" name="subscriptionId"  value="{id}" />
					<input type="image" src="/images/tick_black.png" title="Change" />
				</form>
				<form method="post" action="/reader/unsubscribe">
					<input type="hidden" name="subscriptionGroupId"  value="{groupId}" />
					<input type="hidden" name="subscriptionId"  value="{id}" />
					<input type="image" src="/images/cross_black.png" title="Unsubscribe" class="unsubscribe-button" />
				</form>
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
				{?author}
					<div class="author">
						Author: {author}
					</div>
				{/author}
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
				<span class="title">{title}</span>
				&nbsp;
				<span class="subtitle">
					(<a href="{feed.url}">{feed.title}</a>)
				</span>
				{?feed.description}<div class="description">{feed.description}</div>{/feed.description}
				<div class="stats">
					<div>Refreshed<br /><span>{refreshDate}</span></div>
					<div>Updated<br /><span>{updatedDate}</span></div>
					<div>Unread<br /><span>{unreadCount}</span></div>
				</div>
			</div>
		{/subscriptions}
	</div>
</script>
