<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>jReader - Not authorized</title>
		<link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
	</head>
	<body>
		<h1>jReader users</h1>
		<table>
			<tr>
				<th>Username</th>
				<th>Role</th>
			</tr>
			<c:forEach var="user" items="${users}">
				<tr>
					<td><c:out value="${user.username}"/></td>
					<td><c:out value="${user.role}"/></td>
					<td>
						<c:if test="${user.role ne 'ADMIN'}">
							<form action="/admin/users" method="POST">
								<input type="hidden" name="username" value="${user.username}">
								<input type="hidden" name="role" value="ADMIN">
								<input type="submit" value="Administrator">
							</form>
						</c:if>
						<c:if test="${user.role ne 'USER'}">
							<form action="/admin/users" method="POST">
								<input type="hidden" name="username" value="${user.username}">
								<input type="hidden" name="role" value="USER">
								<input type="submit" value="User">
							</form>
						</c:if>
						<c:if test="${user.role ne 'UNAUTHORIZED'}">
							<form action="/admin/users" method="POST">
								<input type="hidden" name="username" value="${user.username}">
								<input type="hidden" name="role" value="UNAUTHORIZED">
								<input type="submit" value="Unauthorized">
							</form>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
		
	</body>
</html>
