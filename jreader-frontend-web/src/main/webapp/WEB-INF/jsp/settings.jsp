<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="add-subscription">
	<form id="subscription-group-form" method="post" action="/reader/create-group">
		<input type="text" id="group-name" />
		<input type="submit" value="Create group" />
	</form>
	<form id="subscription-form" method="post" action="/reader/subscribe">
		<select name="subscription-group" id="subscription-group"></select>
		<input type="text" id="subscription-url" />
		<input type="submit" value="Subscribe" />
	</form>
</div>
<div id="subscription-settings"></div>