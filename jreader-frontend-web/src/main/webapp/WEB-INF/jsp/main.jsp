<div id="home-contents">
	Welcome!
	<div id="subscription-group-stats"></div>
</div>

<div id="items-contents" style="display: none;">
	<div id="nav-bar">
		<span class="actions">
			<button id="mark-all-read">
				<img alt="Mark all as read" src="/images/tick_black.png">
			</button>
			<button id="refresh">
				<img alt="Refresh" src="/images/refresh_black.png">
			</button>
		</span>
		<span class="items-selection">
			<button id="items-selection-all" data-value="all" data-selected="false">
				<img alt="All" src="/images/list_black.png">
			</button><button
					id="items-selection-unread" data-value="unread" data-selected="true">
				<img alt="Unread" src="/images/square_black.png">
			</button><button
					id="items-selection-starred" data-value="starred" data-selected="false">
				<img alt="Starred" src="/images/star_black.png">
			</button>
		</span>
		<span class="items-order">
			<button id="items-order-asc" data-value="asc" data-selected="true">
				<img alt="Ascending" src="/images/down_black.png">
			</button><button
					id="items-order-desc" data-value="desc" data-selected="false">
				<img alt="Descending" src="/images/up_black.png">
			</button>
		</span>
	</div>
	<table id="feed-entries"></table>
	<div id="status-bar">Loading...</div>
</div>

<div id="settings-contents" style="display: none;">
	<jsp:include page="settings.jsp" />
</div>
