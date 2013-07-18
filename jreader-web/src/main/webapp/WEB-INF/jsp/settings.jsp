<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="add-subscription">
	<form id="subscription-form" method="post" action="/reader/subscribe">
		<input type="text" id="subscription-url" />
		<input type="submit" value="Subscribe" />
	</form>
</div>
<div id="subscription-settings"></div>