<div id="home-contents">
	Welcome!
</div>

<div id="items-contents" style="display: none;">
	<div id="nav-bar">
		<button id="mark-all-read">Mark all as read</button>
		<input id="only-unread" type="checkbox" />
		<label for="only-unread">Show only unread</label>
		<span id="reverse-order-container" style="display: none;">
			<input id="reverse-order" type="checkbox" />
			<label for="reverse-order">Reverse order</label>
		</span>
		<div class="status">Loading...</div>
	</div>
	<div id="feed-entries"></div>
</div>

<div id="settings-contents" style="display: none;">
	<jsp:include page="settings.jsp" />
</div>
