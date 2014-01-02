$(document).ready(function() {
	
	$("#menu").on("click", ".menu-item", function(event) {
		refreshSelectedMenuItem(event);
		
		$(".menu-item[view]").each(function(id, item) {
			$("#" + $(item).attr("view")).hide();
		});
		
		var div = $(event.target).closest(".menu-item");
		$("#" + div.attr("view")).show();
		
		if (div.attr("url") !== undefined) {
			reloadFeedEntries();
		}
		
		return false;
	});
		
	$("#main-area").on("click", ".unsubscribe-button", function(event) {
		var div = $(event.target);
		var id = div.parent().attr("subscription-id");
		var groupId = div.closest(".settings-group").attr("subscription-group-id");
		$.post("/reader/unsubscribe", { "subscriptionId" : id, "subscriptionGroupId" : groupId }, function(data) {
			refreshSubscriptions(data);
		});
	});
	
	$("#main-area").on("submit", "#subscription-group-form", function() {
		var title = $("#subscription-group-form #group-name").val();
		$.post($(this).attr("action"), { "title" : title }, function(data) {
			refreshSubscriptions(data);
		});
		return false;
	});
	

	$("#main-area").on("submit", "#subscription-form", function() {
		var subscriptionGroupId = $("#subscription-form #subscription-group").val();
		var url = $("#subscription-form #subscription-url").val();
		$.post($(this).attr("action"), { "subscriptionGroupId" : subscriptionGroupId, "url" : url }, function(data) {
			refreshSubscriptions(data);
		});
		return false;
	});

	$("#subscription-menu").on("click", ".menu-group .group-collapse", function(event) {
		var menuGroup = $(event.target).closest(".menu-group");
		if (menuGroup.hasClass("collapsed")) {
			menuGroup.removeClass("collapsed");
		} else {
			menuGroup.addClass("collapsed");
		}
		return false;
	});

	$("#main-area").on("click", ".set-item-title button", function(event) {
		var div = $(event.target).parent();
		var input = div.children("input").first();
		var title = input.val();
		if (title == "") {
			return;
		}
		var id = div.parent().attr("subscription-id");
		var groupId = div.closest(".settings-group").attr("subscription-group-id");
		$.post("/reader/entitle", { "subscriptionId" : id, "subscriptionGroupId" : groupId, "title" : title }, function(data) {
			refreshSubscriptions(data);
		});
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
				if ($("#only-unread").is(":checked")) {
					reloadFeedEntries();
				}
			});
		}
	});
	
	$('#nav-bar input').change(function() {
        reloadFeedEntries();
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
};

function reloadFeedEntries() {
	var selection = $("#nav-bar input[name=items-selection]:checked").val();
	var ascending = $("#nav-bar input[name=items-order]:checked").val() === "asc";
	var url = $(".menu-item.selected").attr("url").replace("{selection}", selection).replace("{ascending}", ascending);
	var feedEntriesTable = $("#feed-entries");
	feedEntriesTable.empty();
	var statusDiv = $("#nav-bar .status");
	statusDiv.addClass("loading-feed-entries");
	$.get(url, {}, function(data) {
		$.each(JSON.parse(data), function (id, feedEntry) {
	    	feedEntry.publishedDate = moment(new Date(feedEntry.publishedDate)).format("YYYY-MM-DD HH:mm");
	    	feedEntriesTable.append(template("feedEntryTemplate", feedEntry));
	    });
		statusDiv.removeClass("loading-feed-entries");
	});
}

function refreshSubscriptions(subscriptionGroups) {
	var totalUnreadCount = 0;
	var parsedGroups = JSON.parse(subscriptionGroups);
	$.each(parsedGroups, function(groupIndex, group) {
		totalUnreadCount += group.unreadCount;
		var groupTitle = group.title == undefined ? "Ungrouped" : group.title;
		var loadedGroup = $(".menu-group.menu-item[feed-group='" + groupTitle + "']").get(0);
		if (loadedGroup != undefined) {
			group.cssClass = ($(loadedGroup).hasClass("selected") ? " selected" : "") + ($(loadedGroup).hasClass("collapsed") ? " collapsed" : "");
		} else {
			group.cssClass = " collapsed";
		}
		$.each(group.subscriptions, function(index, subscription) {
			var loadedSubscription = $(".menu-item.feed-item[feed-id='" + subscription.feed.id + "']").get(0);
			subscription.cssClass = (loadedSubscription != undefined && $(loadedSubscription).hasClass("selected")) ? " selected" : "";
			subscription.feed.publishedDate = moment(new Date(subscription.feed.publishedDate)).format("YYYY-MM-DD HH:mm");
			subscription.updatedDate = moment(new Date(subscription.updatedDate)).format("YYYY-MM-DD HH:mm");
			subscription.refreshDate = moment(new Date(subscription.refreshDate)).format("YYYY-MM-DD HH:mm");
		});
	});

	$("#subscription-menu").empty();
	$("#subscription-settings").empty();
	$("#subscription-group-stats").empty();
	$("#subscription-form #subscription-group option").remove();

	$.each(parsedGroups, function(id, group) {
		$("#subscription-menu").append(template("subscriptionMenuGroupTemplate", group));
		$("#subscription-settings").append(template("subscriptionGroupSettingsTemplate", group));
		$("#subscription-group-stats").append(template("subscriptionGroupStatTemplate", group));
		$("#subscription-form #subscription-group").append("<option value='" + group.id + "'>" + group.title + "</option>");
	});
	
	document.title = (totalUnreadCount > 0 ? "(" + totalUnreadCount + ") " : "") + "jReader";
	$("#all-items-menu-item .title .unread-count").html(totalUnreadCount > 0 ? " (" + totalUnreadCount + ")" : "");
}

function reloadSubscriptions() {
	var statusDiv = $("#nav-bar .status");
	statusDiv.addClass("loading-subscriptions");
	$.get("/reader/subscriptions", {}, function(data) {
		refreshSubscriptions(data);
		statusDiv.removeClass("loading-subscriptions");
	});
}

function refreshSelectedMenuItem(event) {
	$("#menu .menu-item.selected").removeClass("selected");
	$(event.target).closest(".menu-item").addClass("selected");
}
