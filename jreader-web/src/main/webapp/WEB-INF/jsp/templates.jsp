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
	<div class="feed-entry">
		<div class="breadcrumb">
			<div class="title">
				{title}
			</div>
			<div class="date">
				{publishedDate}
			</div>
		</div>
		<div class="detail" style="display: none;">
			<div class="title">
				{title}<a target=_blank href="{link}">Open</a>
			</div>
			<div class="date">
				{publishedDate}
			</div>
			<div class="description">
				{description}
			</div>
		</div>
	</div>
</script>
