<div id="home-contents">
	<div id="subscription-group-stats"></div>
</div>

<div id="items-contents" style="display: none;">
	<div id="nav-bar">
		<span class="actions">
			<button id="mark-all-read" title="Mark all as read">
				<img src="/images/tick_black.png">
			</button>
			<button id="refresh" title="Refresh">
				<img src="/images/refresh_black.png">
			</button>
		</span>
		<span class="items-selection">
			<button id="items-selection-all" data-value="all" data-selected="false" title="All">
				<img src="/images/list_black.png">
			</button><button
					id="items-selection-unread" data-value="unread" data-selected="true" title="Unread">
				<img src="/images/square_black.png">
			</button><button
					id="items-selection-starred" data-value="starred" data-selected="false" title="Starred">
				<img src="/images/star_black.png">
			</button>
		</span>
		<span class="items-order">
			<button id="items-order-asc" data-value="asc" data-selected="true" title="Ascending">
				<img src="/images/down_black.png">
			</button><button
					id="items-order-desc" data-value="desc" data-selected="false" title="Descending">
				<img src="/images/up_black.png">
			</button>
		</span>
		<span id="status-bar-count">Items displayed:&nbsp;<span id="entry-count">0</span></span>
		<span id="status-bar-loading">Loading...</span>
	</div>
	<div id="feed-entries-container">
		<table id="feed-entries"></table>
	</div>
</div>

<div id="settings-contents" style="display: none;">
	<jsp:include page="settings.jsp" />
</div>
