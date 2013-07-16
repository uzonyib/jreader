$(document).ready(function() {
	
	$("#home-menu-item").on("click", "a", function(event) {
		$("#feed-entries").empty();
		$("#settings").hide();
		$("#home").show();
		return false;
	});

	$("#settings-menu-item").on("click", "a", function(event) {
		$("#home").hide();
		$("#settings").show();
		return false;
	});
	
	$("#starred-menu-item").on("click", "a", function(event) {
		$("#feed-entries").empty();
		$("#settings").hide();
		$("#home").show();
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
	
	$("#subscription-menu").on("click", ".subscription-menu-item", function(event) {
		$("#settings").hide();
		$("#home").show();
		var feedId = $(event.target).attr("feed-id");
		loadFeedEntriesFrom("/reader/entries/" + feedId);
	});
	
	$("#main-area").on("click", ".set-group-title button", function(event) {
		var div = $(event.target).parent();
		var input = div.children("input").first();
		if (!input.is(":visible")) {
			input.show();
			div.children("span").first().hide();
		} else {
			var group = input.val();
			var id = div.parent().attr("feed-id");
			$.post("/reader/assign", { "id" : id, "group" : group }, function(data) {
				refreshSubscriptions(data);
			});
		}
	});

	$("#main-area").on("click", ".set-subscription-title button", function(event) {
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
			var feedId = feedEntry.attr("feed-id");
			var feedEntryId = feedEntry.attr("feed-entry-id");
			$.post("/reader/read", { "feedId" : feedId, "feedEntryId" : feedEntryId }, function(data) {
				
			});
		}
	});
	
	$("#main-area").on("click", ".feed-entry .breadcrumb .star-buttons .star", function(event) {
		var starButton = $(event.target);
		starButton.attr("disabled, disabled");
		var feedEntry = starButton.closest(".feed-entry");
		var feedId = feedEntry.attr("feed-id");
		var feedEntryId = feedEntry.attr("feed-entry-id");
		$.post("/reader/star", { "feedId" : feedId, "feedEntryId" : feedEntryId }, function(data) {
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
		var feedId = feedEntry.attr("feed-id");
		var feedEntryId = feedEntry.attr("feed-entry-id");
		$.post("/reader/unstar", { "feedId" : feedId, "feedEntryId" : feedEntryId }, function(data) {
			unstarButton.hide();
			var starButton = unstarButton.parent().children(".star").first();
			starButton.removeAttr("disabled");
			starButton.show();
		});
	});
	
	reloadSubscriptions();
	
	dust.loadSource(dust.compile($("#template-subscription-settings").html(),"subscriptionSettingsTemplate"));
	dust.loadSource(dust.compile($("#template-subscription-menu-item").html(),"subscriptionMenuItemTemplate"));
	dust.loadSource(dust.compile($("#template-feed-entry").html(),"feedEntryTemplate"));
	
});

function template(templateName, data) {
	var result = "";
	dust.render(templateName, data, function(err, res) {
		result = res;
	});
	return result;
};

function loadFeedEntriesFrom(url) {
	$.get(url, {}, function(data) {
		$("#feed-entries").empty();
	    $.each(JSON.parse(data), function (id, feedEntry) {
	    	feedEntry.publishedDate = moment(new Date(feedEntry.publishedDate)).format("YYYY-MM-DD HH:mm");
	        $("#feed-entries").append(template("feedEntryTemplate", feedEntry));
	    });
	});
}

function refreshSubscriptions(subscriptionGroups) {
	$("#subscription-menu").empty();
	$("#subscription-menu").append(template("subscriptionMenuItemTemplate", subscriptionGroups));
	$("#subscription-settings").empty();
	$("#subscription-settings").append(template("subscriptionSettingsTemplate", subscriptionGroups));
}

function reloadSubscriptions() {
	$.get("/reader/subscriptions", {}, function(data) {
		refreshSubscriptions(data);
	});
}
