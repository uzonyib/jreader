<div id="home-contents">
	Welcome!
	<div id="subscription-group-stats"></div>
</div>

<div id="items-contents" style="display: none;">
	<div id="nav-bar">
		<div>
			<input type="radio" id="items-selection-all" name="items-selection" value="all" />
			<label for="items-selection-all">All</label>
			<input type="radio" id="items-selection-unread" name="items-selection" value="unread" checked="checked" />
			<label for="items-selection-unread">Unread</label>
			<input type="radio" id="items-selection-starred" name="items-selection" value="starred" />
			<label for="items-selection-starred">Starred</label>
			
			<input type="radio" id="items-order-asc" name="items-order" value="asc" checked="checked" />
			<label for="items-order-asc">Ascending</label>
			<input type="radio" id="items-order-desc" name="items-order" value="desc" />
			<label for="items-order-desc">Descending</label>
		</div>
		
		<button id="mark-all-read">Mark all as read</button>
		<button id="refresh">Refresh</button>
		
	</div>
	<table id="feed-entries"></table>
	<div id="status-bar">Loading...</div>
</div>

<div id="settings-contents" style="display: none;">
	<jsp:include page="settings.jsp" />
</div>
