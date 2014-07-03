<!doctype html>

<%@page import="com.qualogy.qafe.web.util.ServletUtilities"%>

<html manifest="/qafemobile.manifest">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">		
		<link rel="apple-touch-icon" href="images/logo.png" />
		<link rel="apple-touch-startup-image" href="images/full.png">
		
		<style type="text/css">
		</style>

		<title>QAFE Mobile</title>
		
		<script type="text/javascript" language="javascript" src="qafemobile/qafemobile.nocache.js">
		</script>
	</head>
	<body>
		<script type="text/javascript">
			var AppDetails = {
				appUUID: <%=ServletUtilities.initConversationAppId(response)%>,
				windowSession: <%=ServletUtilities.initConversationWindowSession(response)%>
			};		
		</script>
		
		<!-- OPTIONAL: include this if you want history support -->
		<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position: absolute; width: 0; height: 0; border: 0"/>
		<noscript>
			<div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
				Your web browser must have JavaScript enabled in order for this
				application to display correctly.
			</div>
		</noscript>
	</body>
</html>