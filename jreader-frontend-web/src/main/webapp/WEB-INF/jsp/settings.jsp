<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="add-subscription">
	<form id="subscription-group-form" method="post" action="/reader/create-group">
		<span>Add group&nbsp</span><input
			type="text" name="title" placeholder="group name" /><input
			type="image" src="/images/tick_black.png" title="Create group" />
	</form>
	<form id="subscription-form" method="post" action="/reader/subscribe">
		<select name="subscriptionGroupId" id="subscription-group">
		</select><input
			type="text" name="url" placeholder="URL" size="80" /><input
			type="image" src="/images/tick_black.png" title="Subscribe" />
	</form>
</div>
<div id="subscription-settings"></div>