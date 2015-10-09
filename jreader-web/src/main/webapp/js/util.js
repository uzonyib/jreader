angular.module("jReaderFilters", []).filter("moment", function() {
	return function (date) {
		if (angular.isUndefined(date) || date == null) {
			return "";
		}
		var m = moment(date);
		var duration = moment.duration(m.diff(moment()));
		if (duration.asDays() <= -1) {
			return m.format("YYYY-MM-DD HH:mm");
		}
		return duration.humanize(true);
	};
});
