<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>jReader - Users</title>
		<link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
	</head>
	<body>
		<h1>jReader users</h1>
		<table id="user-role">
			<thead>
				<tr>
					<th>Username</th>
					<th>Role</th>
					<th>Registration date</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${users}">
					<tr>
						<td class="username"><c:out value="${user.username}"/></td>
						<td class="role"><c:out value="${user.role}"/></td>
						<td class="registrationDate"><fmt:formatDate value="${user.registrationDateAsDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td class="action">
							<c:if test="${user.role ne 'ADMIN'}">
								<form action="/admin/users" method="POST" class="admin">
									<input type="hidden" name="username" value="${user.username}">
									<input type="hidden" name="role" value="ADMIN">
									<input type="submit" value="Administrator">
								</form>
							</c:if>
							<c:if test="${user.role ne 'USER'}">
								<form action="/admin/users" method="POST" class="user">
									<input type="hidden" name="username" value="${user.username}">
									<input type="hidden" name="role" value="USER">
									<input type="submit" value="User">
								</form>
							</c:if>
							<c:if test="${user.role ne 'UNAUTHORIZED'}">
								<form action="/admin/users" method="POST" class="unauthorized">
									<input type="hidden" name="username" value="${user.username}">
									<input type="hidden" name="role" value="UNAUTHORIZED">
									<input type="submit" value="Unauthorized">
								</form>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
	</body>
</html>
