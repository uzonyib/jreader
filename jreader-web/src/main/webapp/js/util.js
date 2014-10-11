angular.module("jReaderFilters", []).filter("moment", function() {
	return function (date) {
		var m = moment(date);
		var duration = moment.duration(m.diff(moment()));
		if (duration.asDays() <= -1) {
			return m.format("YYYY-MM-DD HH:mm");
		}
		return duration.humanize(true);
	};
});
