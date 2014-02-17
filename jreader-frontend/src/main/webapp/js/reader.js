$(document).ready(function() {

	$("#feed-entries-container").height($(document).height() - 34);
	
	$("#menu").on("click", ".menu-item", function(event) {
		var div = $(event.target).closest(".menu-item");
		if (div.attr("view") === undefined) {
			return;
		}
		
		refreshSelectedMenuItem(event);
		
		$(".menu-item[view]").each(function(id, item) {
			$("#" + $(item).attr("view")).hide();
		});
		
		$("#" + div.attr("view")).show();
		
		if (div.attr("url") !== undefined) {
			reloadFeedEntries();
		}
		
		return false;
	});
	
	$("#main-area").on("submit", "form", function() {
		var url = $(this).attr("action");
		if (url === undefined) {
			return false;
		}
		
		var data = {};
		$(this).find("input, select").each(function() {
			var name = $(this).attr("name");
			if (name !== undefined) {
				var value = $(this).val();
				data[name] = value;
			}
		});
		
		$.post(url, data, function(response) {
			refreshSubscriptions(response);
		});
		return false;
	});
	
	$("#subscription-settings").on("click", ".settings-item .title, .group-title .title", function() {
		$(this).hide();
		$(this).parent().find("form").removeClass("hidden");
	});

	$("#subscription-menu").on("click", ".menu-group .icon", function(event) {
		var menuGroup = $(event.target).closest(".menu-group");
		if (menuGroup.hasClass("collapsed")) {
			menuGroup.removeClass("collapsed");
		} else {
			menuGroup.addClass("collapsed");
		}
		return false;
	});
	
	$("#main-area").on("click", ".feed-entry .breadcrumb .feed-title, .feed-entry .breadcrumb .title, .feed-entry .breadcrumb .date", function(event) {
		var feedEntry = $(event.target).closest(".feed-entry");
		feedEntry.children(".detail").first().toggle();
		
		if (feedEntry.hasClass("unread")) {
			feedEntry.removeClass("unread");
			var feedEntryId = feedEntry.attr("feed-entry-id");
			var subscriptionId = feedEntry.attr("subscription-id");
			var subscriptionGroupId = feedEntry.attr("subscription-group-id");
			$.post("/reader/read", { "ids" : feedEntryId, "subscriptionIds" : subscriptionId, "subscriptionGroupIds" : subscriptionGroupId }, function(data) {
				refreshSubscriptions(data);
			});
		}
	});
	
	$("#main-area").on("click", ".feed-entry .breadcrumb .star-buttons .star", function(event) {
		var starButton = $(event.target);
		var feedEntry = starButton.closest(".feed-entry");
		var feedEntryId = feedEntry.attr("feed-entry-id");
		var subscriptionId = feedEntry.attr("subscription-id");
		var subscriptionGroupId = feedEntry.attr("subscription-group-id");
		$.post("/reader/star", { "id" : feedEntryId, "subscriptionId" : subscriptionId, "subscriptionGroupId" : subscriptionGroupId }, function(data) {
			starButton.hide();
			var unstarButton = starButton.parent().children(".unstar").first();
			unstarButton.show();
		});
	});

	$("#main-area").on("click", ".feed-entry .breadcrumb .star-buttons .unstar", function(event) {
		var unstarButton = $(event.target);
		var feedEntry = unstarButton.closest(".feed-entry");
		var feedEntryId = feedEntry.attr("feed-entry-id");
		var subscriptionId = feedEntry.attr("subscription-id");
		var subscriptionGroupId = feedEntry.attr("subscription-group-id");
		$.post("/reader/unstar", { "id" : feedEntryId, "subscriptionId" : subscriptionId, "subscriptionGroupId" : subscriptionGroupId }, function(data) {
			unstarButton.hide();
			var starButton = unstarButton.parent().children(".star").first();
			starButton.show();
		});
	});
	
	$("#main-area").on("click", "#mark-all-read", function() {
		var ids = "";
		var subscriptionIds = "";
		var subscriptionGroupIds = "";
		$("#feed-entries .feed-entry.unread").each(function(id, item) {
			$(item).removeClass("unread");
			ids += "," + $(item).attr("feed-entry-id");
			subscriptionIds += "," + $(item).attr("subscription-id");
			subscriptionGroupIds += "," + $(item).attr("subscription-group-id");
		});
		if (ids.length > 0) {
			ids = ids.substring(1);
			subscriptionIds = subscriptionIds.substring(1);
			subscriptionGroupIds = subscriptionGroupIds.substring(1);
			$.post("/reader/read", { "ids" : ids, "subscriptionIds" : subscriptionIds, "subscriptionGroupIds" : subscriptionGroupIds }, function(data) {
				refreshSubscriptions(data);
				reloadFeedEntries();
			});
		}
	});
	
	$("#main-area").on("click", "#refresh", function() {
		reloadFeedEntries();
	});
	
	$("#nav-bar .items-selection button").click(function(event) {
		var button = $(event.target).closest("button");
		if (button.attr("data-selected") !== "true") {
			$("#nav-bar .items-selection button[data-selected='true']").attr("data-selected", "false");
			button.attr("data-selected", "true");
	        reloadFeedEntries();
		}
    });
	
	$("#nav-bar .items-order button").click(function(event) {
		var button = $(event.target).closest("button");
		if (button.attr("data-selected") !== "true") {
			$("#nav-bar .items-order button[data-selected='true']").attr("data-selected", "false");
			button.attr("data-selected", "true");
	        reloadFeedEntries();
		}
    });
	
	$("#feed-entries-container").scroll(function() {
		if ($(".menu-item.selected").attr("view") === "items-contents") {
			if ($("#feed-entries-container").scrollTop() + $("#feed-entries-container").height() == $("#feed-entries").height()) {
				loadNextPageOfFeedEntries();
			}
		}
	});
	
	reloadSubscriptions();
	setInterval(reloadSubscriptions, 1000 * 60 * 5);
	
	dust.loadSource(dust.compile($("#template-subscription-group-settings").html(),"subscriptionGroupSettingsTemplate"));
	dust.loadSource(dust.compile($("#template-subscription-menu-group").html(),"subscriptionMenuGroupTemplate"));
	dust.loadSource(dust.compile($("#template-feed-entry").html(),"feedEntryTemplate"));
	dust.loadSource(dust.compile($("#template-subscription-group-stat").html(),"subscriptionGroupStatTemplate"));
	
});

function template(templateName, data) {
	var result = "";
	dust.render(templateName, data, function(err, res) {
		result = res;
	});
	return result;
}

var nextPageIndex = 0;
var loading = false;
var endOfList = false;

function loadNextPageOfFeedEntries() {
	if (loading || endOfList) {
		return;
	}
	loading = true;
	var selection = $("#nav-bar .items-selection button[data-selected='true']").attr("data-value");
	var ascending = $("#nav-bar .items-order button[data-selected='true']").attr("data-value") === "asc";
	var url = $(".menu-item.selected").attr("url").replace("{selection}", selection).replace("{ascending}", ascending).replace("{pageIndex}", nextPageIndex);
	
	var feedEntriesTable = $("#feed-entries");
	if (nextPageIndex == 0) {
		feedEntriesTable.empty();
	}
	
	var entryCount = 0;
	if (nextPageIndex > 0) {
		entryCount = parseInt($("#entry-count").html());
	}
	
	var loadingDiv = $("#status-bar-loading");
	loadingDiv.removeClass("hidden");
	var countDiv = $("#status-bar-count");
	countDiv.addClass("hidden");
	
	++nextPageIndex;
	
	$.get(url, {}, function(json) {
		if (json.length < 30) {
			endOfList = true;
		}
		entryCount += json.length;
		$("#entry-count").html(entryCount);
		$.each(json, function (id, feedEntry) {
	    	feedEntry.publishedDate = displayDate(feedEntry.publishedDate);
	    	feedEntriesTable.append(template("feedEntryTemplate", feedEntry));
	    });
		loading = false;
		if (!endOfList && $("#feed-entries-container").height() >= $("#feed-entries").height()) {
			loadNextPageOfFeedEntries();
		} else {
			loadingDiv.addClass("hidden");
			countDiv.removeClass("hidden");
		}
	});
}

function reloadFeedEntries() {
	endOfList = false;
	nextPageIndex = 0;
	loadNextPageOfFeedEntries();
}

function refreshSubscriptions(subscriptionGroups) {
	var totalUnreadCount = 0;
	$.each(subscriptionGroups, function(groupIndex, group) {
		totalUnreadCount += group.unreadCount;
		group.first = (groupIndex == 0);
		group.last = (groupIndex == subscriptionGroups.length - 1);
		var domGroup = $(".menu-group[feed-group='" + group.title + "']").get(0);
		if (domGroup != undefined) {
			group.collapsed = $(domGroup).hasClass("collapsed");
			group.selected = $(domGroup).find(".group-item").hasClass("selected");
		} else {
			group.collapsed = true;
			group.selected = false;
		}
		$.each(group.subscriptions, function(index, subscription) {
			var loadedSubscription = $(".menu-item.feed-item[subscription-id='" + subscription.id + "']").get(0);
			subscription.selected = loadedSubscription != undefined && $(loadedSubscription).hasClass("selected");
			subscription.feed.publishedDate = displayDate(subscription.feed.publishedDate);
			subscription.updatedDate = displayDate(subscription.updatedDate);
			subscription.refreshDate = displayDate(subscription.refreshDate);
			subscription.first = (index == 0);
			subscription.last = (index == group.subscriptions.length - 1);
		});
	});

	$("#subscription-menu").empty();
	$("#subscription-settings").empty();
	$("#subscription-group-stats").empty();
	$("#subscription-form #subscription-group option").remove();

	$.each(subscriptionGroups, function(id, group) {
		$("#subscription-menu").append(template("subscriptionMenuGroupTemplate", group));
		$("#subscription-settings").append(template("subscriptionGroupSettingsTemplate", group));
		$("#subscription-group-stats").append(template("subscriptionGroupStatTemplate", group));
		$("#subscription-form #subscription-group").append("<option value='" + group.id + "'>" + group.title + "</option>");
	});
	
	document.title = (totalUnreadCount > 0 ? "(" + totalUnreadCount + ") " : "") + "jReader";
	$("#all-items-menu-item .unread-count").html(totalUnreadCount > 0 ? "(" + totalUnreadCount + ")" : "");
}

function reloadSubscriptions() {
	var statusDiv = $("#status-bar-loading");
	statusDiv.removeClass("hidden");
	var countDiv = $("#status-bar-count");
	countDiv.addClass("hidden");
	$.get("/reader/subscriptions", {}, function(data) {
		refreshSubscriptions(data);
		statusDiv.addClass("hidden");
		countDiv.removeClass("hidden");
	});
}

function refreshSelectedMenuItem(event) {
	$("#menu .menu-item.selected").removeClass("selected");
	$(event.target).closest(".menu-item").addClass("selected");
}

function displayDate(date) {
	var m = moment(date);
	var duration = moment.duration(m.diff(moment()));
	if (duration.asDays() <= -1) {
		return m.format("YYYY-MM-DD HH:mm");
	}
	return duration.humanize(true);
}
