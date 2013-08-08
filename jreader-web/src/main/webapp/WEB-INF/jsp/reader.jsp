<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>jReader</title>
	<link rel="stylesheet" type="text/css" href="/css/reset.css">
	<link rel="stylesheet" type="text/css" href="/css/reader.css">
	<script type="text/javascript" src="/js/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="/js/moment.min.js"></script>
	<script type="text/javascript" src="/js/dust-full-1.2.3.min.js"></script>
	<script type="text/javascript" src="/js/dust-helpers-1.1.1.js"></script>
	<script type="text/javascript" src="/js/reader.js"></script>
	<jsp:include page="templates.jsp"></jsp:include>
</head>
<body>
	<div id="header">
		<jsp:include page="header.jsp" />
	</div>
	<div id="menu">
		<jsp:include page="menu.jsp" />
	</div>
	<div id="main-area">
		<jsp:include page="main.jsp" />
	</div>
	<div id="footer"></div>
</body>
</html>
