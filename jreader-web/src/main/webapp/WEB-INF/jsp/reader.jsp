<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Reader</title>
	<link rel="stylesheet" type="text/css" href="/css/reader.css">
	<script type="text/javascript" src="/js/jquery-1.10.2.min.js"></script>
<!-- 	<script type="text/javascript" src="/js/jquery.ui.datepicker.js"></script> -->
	<script type="text/javascript" src="/js/moment.min.js"></script>
	<script type="text/javascript" src="/js/reader.js"></script>
</head>
<body>
	<div id="header">
		<jsp:include page="header.jsp" />
	</div>
	<div id="menu">
		<jsp:include page="menu.jsp" />
	</div>
	<div id="main-area">
		<div id="settings" style="display: none;">
			<jsp:include page="settings.jsp" />
		</div>
		<div id="home">
			<jsp:include page="main.jsp" />
		</div>
	</div>
	<div id="footer"></div>
</body>
</html>