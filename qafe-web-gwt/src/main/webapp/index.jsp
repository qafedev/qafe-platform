
<%@page import="com.qualogy.qafe.web.util.ParameterHelper"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="chrome=1">
<meta http-equiv="refresh" content="2;url=QAFEGWTWeb.jsp?<%=ParameterHelper.createParameterInputString(request)%>"/>
<style type="text/css"> 
html, body { height: 100%; overflow: hidden; margin: 0; padding: 0; }
#splash { cursor: wait; position: absolute; top: 0; left: 0; }
#splash_loading { background-image: url('images/blank_splash.gif');
	height: 34px; width: 82px; padding: 66px 0 0 68px; text-align: left;
	font-family: Tahoma, Arial, Verdana,Tahoma; font-size: 10px; font-weight: bold;
	color: #777; cursor: wait; }
</style> 
</head>
<body style="background:white;font:12px Verdana, Arial, Helvetica, sans-serif;">
<a href="QAFEGWTWeb.jsp?<%=ParameterHelper.createParameterInputString(request)%>">QAFE: Qualogy Application Framework for Enterprises Demo</a>

<div id="splash" style="height:100%;width:100%;"> 
	<table width="100%" height="90%"> 
		<tr> 
			<td width="100%" height="100%" align="center" valign="middle"> 
				<div id="splash_loading" alttext="Error">Loading</div> 
			</td> 
		</tr> 
	</table> 
</div> 


<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-6924118-2");
pageTracker._trackPageview();
} catch(err) {}</script>
	
</body>
</html>
