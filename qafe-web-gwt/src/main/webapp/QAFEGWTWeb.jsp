
<%@page import="com.qualogy.qafe.web.util.ServletUtilities"%>

<%
  
	int height = 0;
	int width = 0;
	String placement = null;
	if(request.getParameter("height")!=null){
		height  = Integer.parseInt(request.getParameter("height"));
	}
	if(request.getParameter("width")!=null){
		width = Integer.parseInt(request.getParameter("width"));
	}
	if(request.getParameter("placement")!=null){
		placement = request.getParameter("placement").toString();
	}

%>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="chrome=1">
		<jsp:include page="QAFEGWTWebTitle.jsp"></jsp:include>
<%
		if(height>0 && width>0){
		%>
		<script type="text/javascript">
			function setwindowHeightAndWidth(){
				window.resizeTo('<%=height%>','<%=width%>');
				if ('<%=placement%>'=='center'){
					var left = parseInt(((screen.width -<%=width%>)/2));
					var top = parseInt(((screen.height - <%=height%>)/2));
					window.moveTo(left,top);

				}
			}
		
		</script>
		<%	
		}
%>

		
     	<link rel="shortcut icon" href="images/favicon.ico">
     	<!--  css writing is moved to QAFEGWTWEB.java onModuleLoad -->
		<!-- <link rel="stylesheet" type="text/css" href="css/qafe-gwt.css" /> -->
		<!-- <link rel="stylesheet" type="text/css" href="css-generator?type=gwt" /> -->
		<!-- <link rel="stylesheet" type="text/css" href="css/standard.css" /> -->
	 	<!--  <link rel="stylesheet" type="text/css" href="css/ScrollTableDemo.css" />-->
      <!--   <link id="advancedTheme" type="text/css" rel="stylesheet" href="qafegwt/advanced/themes/default/theme.css"/>-->
		<meta name='gwt:module' content='qafegwt/qafegwt.nocache.js'>
	</head>

	<!--                                           -->
	<!-- The body can have arbitrary html, or      -->
	<!-- you can leave the body empty if you want  -->
	<!-- to create a completely dynamic ui         -->
	
	<!-- The oncontextmenu false is needed for the contextmenu from Rocket oncontextmenu="return false;"-->                                         
	<!--[if IE 6]>
  <style type="text/css">
.imglogo {
    filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='images/wallpaper-logo.png', sizingMethod='scale');
}

  </style>
<![endif]-->
	<body class="qafe_body">
		<div id="appId" value="<%=ServletUtilities.initConversationAppId(response)%>"/>
		<div id="winId" value="<%=ServletUtilities.initConversationWindowSession(response)%>"/>
		
		<!--                                            -->
		<!-- This script is required bootstrap stuff.   -->
		<!-- You can put it in the HEAD, but startup    -->
		<!-- is slightly faster if you include it here. -->
		<!--                                            -->
		<script language="javascript" src="qafegwt/qafegwt.nocache.js"></script>
		<!-- OPTIONAL: this is included to have printing possibilities -->
     	<iframe id="__printingFrame" style="width:0;height:0;border:0"></iframe> 
		<!-- OPTIONAL: include this if you want history support -->
		<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>


		<div id="main">

					

		</div>

		
	</body>
</html>
