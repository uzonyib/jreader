$(document).ready(function() {
	
	$("#home-menu-item").on("click", "a", function(event) {
		$("#items-contents").hide();
		$("#settings-contents").hide();
		$("#home-contents").show();
		return false;
	});

	$("#all-items-menu-item").on("click", "a", function(event) {
		$("#settings-contents").hide();
		$("#home-contents").hide();
		
		var feedIds = "";
		$("#menu #subscription-menu .menu-item").each(function(id, item) {
			feedIds += "," + $(item).attr("feed-id");
		});
		feedIds = feedIds.substring(1);
		loadFeedEntriesFrom("/reader/entries?ids=" + feedIds);
		
		$("#items-contents").show();
		return false;
	});

	$("#settings-menu-item").on("click", "a", function(event) {
		$("#items-contents").hide();
		$("#home-contents").hide();
		$("#settings-contents").show();
		return false;
	});
	
	$("#starred-menu-item").on("click", "a", function(event) {
		$("#settings-contents").hide();
		$("#home-contents").hide();
		$("#items-contents").show();
		loadFeedEntriesFrom("/reader/starred");
		return false;
	});
	
	$("#main-area").on("click", ".unsubscribe-button", function(event) {
		var id = $(event.target).parent().attr("feed-id");
		$.post("/reader/unsubscribe", { "id" : id }, function(data) {
			refreshSubscriptions(data);
		});
	});
	

	$("#main-area").on("submit", "#subscription-form", function() {
		var url = $("#subscription-url").val();
		$.post($(this).attr("action"), { "url" : url }, function(data) {
			refreshSubscriptions(data);
		});
		return false;
	});
	
	$("#subscription-menu").on("click", ".menu-item", function(event) {
		$("#settings").hide();
		$("#home").show();
		var feedId = $(event.target).attr("feed-id");
		loadFeedEntriesFrom("/reader/entries?ids=" + feedId);
	});

	$("#subscription-menu").on("click", ".menu-group .group-title", function(event) {
		$("#settings").hide();
		$("#home").show();
		var feedIds = "";
		$(event.target).parent().children(".menu-item").each(function(id, item) {
			feedIds += "," + $(item).attr("feed-id");
		});
		feedIds = feedIds.substring(1);
		loadFeedEntriesFrom("/reader/entries?ids=" + feedIds);
	});
	
	$("#main-area").on("click", ".set-group-title button", function(event) {
		var div = $(event.target).parent();
		var group = div.children("input").first().val();
		var id = div.parent().attr("feed-id");
		$.post("/reader/assign", { "id" : id, "group" : group }, function(data) {
			refreshSubscriptions(data);
		});
	});

	$("#main-area").on("click", ".set-item-title button", function(event) {
		var div = $(event.target).parent();
		var input = div.children("input").first();
		var title = input.val();
		if (title == "") {
			return;
		}
		var id = div.parent().attr("feed-id");
		$.post("/reader/entitle", { "id" : id, "title" : title }, function(data) {
			refreshSubscriptions(data);
		});
	});
	
	$("#main-area").on("click", ".feed-entry .breadcrumb .title", function(event) {
		var feedEntry = $(event.target).closest(".feed-entry");
		feedEntry.children(".detail").first().toggle();
		
		if (feedEntry.hasClass("unread")) {
			feedEntry.removeClass("unread");
			var feedEntryId = feedEntry.attr("feed-entry-id");
			$.post("/reader/read", { "ids" : feedEntryId }, function(data) {
				
			});
		}
	});
	
	$("#main-area").on("click", ".feed-entry .breadcrumb .star-buttons .star", function(event) {
		var starButton = $(event.target);
		starButton.attr("disabled, disabled");
		var feedEntry = starButton.closest(".feed-entry");
		var feedEntryId = feedEntry.attr("feed-entry-id");
		$.post("/reader/star", { "id" : feedEntryId }, function(data) {
			starButton.hide();
			var unstarButton = starButton.parent().children(".unstar").first();
			unstarButton.removeAttr("disabled");
			unstarButton.show();
		});
	});

	$("#main-area").on("click", ".feed-entry .breadcrumb .star-buttons .unstar", function(event) {
		var unstarButton = $(event.target);
		unstarButton.attr("disabled, disabled");
		var feedEntry = unstarButton.closest(".feed-entry");
		var feedEntryId = feedEntry.attr("feed-entry-id");
		$.post("/reader/unstar", { "id" : feedEntryId }, function(data) {
			unstarButton.hide();
			var starButton = unstarButton.parent().children(".star").first();
			starButton.removeAttr("disabled");
			starButton.show();
		});
	});
	
	$("#main-area").on("click", "#mark-all-read", function() {
		var ids = "";
		$("#feed-entries .feed-entry.unread").each(function(id, item) {
			$(item).removeClass("unread");
			ids += "," + $(item).attr("feed-entry-id");
		});
		if (ids.length > 0) {
			ids = ids.substring(1);
			$.post("/reader/read", { "ids" : ids }, function(data) {
				if ($("#only-unread").is(":checked")) {
					reloadFeedEntries();
				}
			});
		}
	});
	
	$("#main-area").on("click", "#only-unread", function(event) {
		reloadFeedEntries();
		$("#reverse-order-container").toggle();
	});
	
	reloadSubscriptions();
	
	dust.loadSource(dust.compile($("#template-subscription-group-settings").html(),"subscriptionGroupSettingsTemplate"));
	dust.loadSource(dust.compile($("#template-subscription-menu-group").html(),"subscriptionMenuGroupTemplate"));
	dust.loadSource(dust.compile($("#template-feed-entry").html(),"feedEntryTemplate"));
	
});

function template(templateName, data) {
	var result = "";
	dust.render(templateName, data, function(err, res) {
		result = res;
	});
	return result;
};

function reloadFeedEntries() {
	var url = $("#feed-entries").attr("loaded-from");
	if (typeof url !== "undefined" && url.length > 0) {
		loadFeedEntriesFrom(url);
	}
}

function loadFeedEntriesFrom(urlParam) {
	var url = urlParam + (urlParam.indexOf("?") > -1 ? "&" : "?") + "only-unread=" + $("#only-unread").is(":checked");
	$.get(url, {}, function(data) {
		var feedEntriesDiv = $("#feed-entries");
		feedEntriesDiv.empty();
		feedEntriesDiv.attr("loaded-from", urlParam);
	    $.each(JSON.parse(data), function (id, feedEntry) {
	    	feedEntry.publishedDate = moment(new Date(feedEntry.publishedDate)).format("YYYY-MM-DD HH:mm");
	    	feedEntriesDiv.append(template("feedEntryTemplate", feedEntry));
	    });
	});
}

function refreshSubscriptions(subscriptionGroups) {
	$("#subscription-menu").empty();
	$("#subscription-settings").empty();
	$.each(JSON.parse(subscriptionGroups), function(id, group) {
		$("#subscription-menu").append(template("subscriptionMenuGroupTemplate", group));
		$("#subscription-settings").append(template("subscriptionGroupSettingsTemplate", group));
	});
}

function reloadSubscriptions() {
	$.get("/reader/subscriptions", {}, function(data) {
		refreshSubscriptions(data);
	});
}
