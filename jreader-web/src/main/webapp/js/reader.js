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
		var id = $(event.target).attr("subscription-id");
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
	
	reloadSubscriptions();
	
	$("#subscription-menu").on("click", ".subscription-menu-item", function(event) {
		$("#settings").hide();
		$("#home").show();
		var id = $(event.target).attr("subscription-id");
		$.get("/reader/entries/" + id, {}, function(data) {
			$("#feed-entries").empty();
		    $.each(JSON.parse(data), function (id, option) {
		    	var entry = $("<div class=\"feed-entry\"></div>");
		    	var link = $("<div><a target=_blank href='" + option.link + "'>" + option.title + "</a></div>");
		    	entry.append(link);
		    	var date = $("<div>" + moment(new Date(option.publishedDate)).format("YYYY-MM-DD hh:mm") + "</div>");
		    	entry.append(date);
		        $("#feed-entries").append(entry);
		    });
		});
	});
	
});

function refreshSubscriptions(data) {
	$("#subscription-menu").empty();
	$("#subscription-settings").empty();
    $.each(JSON.parse(data), function (id, option) {
    	$("#subscription-menu").append("<div class=\"subscription-menu-item\" subscription-id=\"" + option.id + "\">" + option.title + "</div>");
    	
    	var deleteButton = $("<button class=\"unsubscribe-button\" subscription-id=\"" + option.id + "\">Unsubscribe</button>");
    	var subscription = $("<div class=\"subscription-settings-item\" subscription-id=\"" + option.id + "\">" + option.title + "</div>");
    	subscription.append(deleteButton);
        $("#subscription-settings").append(subscription);
    });
}

function reloadSubscriptions() {
	$.get("/reader/subscriptions", {}, function(data) {
		refreshSubscriptions(data);
	});
}
