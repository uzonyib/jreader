<div id="logout-menu-item">
	<a href="${ logoutUrl }">Logout</a>
</div>

<div class="menu-item selected" id="home-menu-item" view="home-contents">
	<span class="icon"></span>
	<span class="title">Home</span>
</div>

<div class="menu-item" id="settings-menu-item" view="settings-contents">
	<span class="icon"></span>
	<span class="title">Settings</span>
</div>

<div class="menu-item" id="all-items-menu-item" view="items-contents" url="/reader/entries/all/{selection}?ascending={ascending}">
	<span class="icon"></span>
	<span class="title">All items<span class="unread-count"></span></span>
</div>

<div id="subscription-menu"></div>
