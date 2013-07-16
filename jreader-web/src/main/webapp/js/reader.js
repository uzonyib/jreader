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
		    $.each(JSON.parse(data), function (id, feedEntry) {
		    	feedEntry.publishedDate = moment(new Date(feedEntry.publishedDate)).format("YYYY-MM-DD HH:mm");
		        $("#feed-entries").append(template("feedEntryTemplate", feedEntry));
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
	
	$("#main-area").on("click", ".feed-entry", function(event) {
		$(event.target).closest(".feed-entry").children(".breadcrumb").first().toggle();
		$(event.target).closest(".feed-entry").children(".detail").first().toggle();
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

function refreshSubscriptions(data) {
	$("#subscription-menu").empty();
	$("#subscription-settings").empty();
    $.each(JSON.parse(data), function (id, subscription) {
    	$("#subscription-menu").append(template("subscriptionMenuItemTemplate", subscription));
    	$("#subscription-settings").append(template("subscriptionSettingsTemplate", subscription));
    });
}

function reloadSubscriptions() {
	$.get("/reader/subscriptions", {}, function(data) {
		refreshSubscriptions(data);
	});
}
