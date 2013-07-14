<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="add-subscription">
	<form id="subscription-form" method="post" action="/reader/subscribe">
		<input type="text" id="subscription-url" />
		<input type="submit" value="Subscribe" />
	</form>
	
<!-- 	<table> -->
<%-- 		<c:forEach items="${ feeds }" var="feed"> --%>
<!-- 			<tr> -->
<%-- 				<td>${ feed.title }</td> --%>
<%-- 				<td>${ feed.url }</td> --%>
<%-- 				<td>${ feed.description }</td> --%>
<%-- 				<td>${ feed.feedType }</td> --%>
<%-- 				<td>${ feed.categories }</td> --%>
<%-- 				<td>${ feed.publishDate }</td> --%>
<!-- 				<td> -->
<!-- 					<form class="unsubscription-form" method="post" action="/reader/unsubscribe"> -->
<%-- 						<input type="hidden" class="url" value="${ feed.url }" /> --%>
<!-- 						<input type="submit" value="Unsubscribe" /> -->
<!-- 					</form> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<%-- 		</c:forEach> --%>
<!-- 	</table> -->
</div>
<div id="subscription-settings"></div>