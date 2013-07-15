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
		var id = $(event.target).attr("feed-id");
		$.get("/reader/entries/" + id, {}, function(data) {
			$("#feed-entries").empty();
		    $.each(JSON.parse(data), function (id, option) {
		    	var entry = $("<div class=\"feed-entry\"></div>");
		    	var link = $("<div><a target=_blank href='" + option.link + "'>" + option.title + "</a></div>");
		    	entry.append(link);
		    	var description = $("<div>" + option.description + "</div>");
		    	entry.append(description);
		    	var date = $("<div>" + moment(new Date(option.publishedDate)).format("YYYY-MM-DD HH:mm") + "</div>");
		    	entry.append(date);
		        $("#feed-entries").append(entry);
		    });
		});
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
	
	reloadSubscriptions();
	
	dust.loadSource(dust.compile($("#template-subscription-settings").html(),"subscriptionSettingsTemplate"));
	
});

function templateSubscriptionSettings(subscriptionSettings) {
	var result;
	dust.render("subscriptionSettingsTemplate", subscriptionSettings, function(err, res) {
		result = res;
	});
	return result;
};

function refreshSubscriptions(data) {
	$("#subscription-menu").empty();
	$("#subscription-settings").empty();
    $.each(JSON.parse(data), function (id, subscription) {
    	$("#subscription-menu").append("<div class=\"subscription-menu-item\" feed-id=\"" + subscription.feed.id + "\">" + (typeof subscription.group === "undefined" ? "" : (subscription.group.title + " / ")) + subscription.title + "</div>");
    	
    	$("#subscription-settings").append(templateSubscriptionSettings(subscription));
    });
}

function reloadSubscriptions() {
	$.get("/reader/subscriptions", {}, function(data) {
		refreshSubscriptions(data);
	});
}
