<script type="text/x-template" id="template-subscription-settings">
	<div class="subscription-settings-item" feed-id="{feed.id}">
		<div class="set-subscription-title">
			<input type="text" value="{title}" />
			<button>Change</button>
		</div>
		<div class="set-group-title">
			<input class="set-group-title" type="text" style="display: none;" value="{group.title}" />
			<span>{group.title}</span>
			<button>Group</button>
		</div>
		<button class="unsubscribe-button">Unsubscribe</button>
	</div>
</script>

<script type="text/x-template" id="template-subscription-menu-item">
	<div class="subscription-menu-item" feed-id="{feed.id}">
		{?group}
			{group.title}&nbsp;/&nbsp;
		{/group}
		{title}
	</div>
</script>

<script type="text/x-template" id="template-feed-entry">
	<div class="feed-entry{^entry.read} unread{/entry.read}" feed-id="{feedId}" feed-entry-id="{entry.id}">
		<div class="breadcrumb">
			<div class="star-buttons">
				<button class="star"{?entry.starred} style="display: none;"{/entry.starred}>Star</button>
				<button class="unstar"{^entry.starred} style="display: none;"{/entry.starred}>Unstar</button>
			</div>
			<div class="title">
				{entry.title}
			</div>
			<div class="date">
				{entry.publishedDate}
			</div>
		</div>
		<div class="detail" style="display: none;">
			<div class="link">
				<a target=_blank href="{entry.link}">Open</a>
			</div>
			<div class="description">
				{entry.description}
			</div>
		</div>
	</div>
</script>
