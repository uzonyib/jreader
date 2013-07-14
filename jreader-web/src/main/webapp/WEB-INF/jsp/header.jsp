<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize ifAnyGranted="ROLE_USER">
	<a href="/reader/logout">logout</a>
</sec:authorize>
