<%@ page import="static dk.statsbiblioteket.deck.client.webinterface.WebConstants.*" %>
<%@ page import="static dk.statsbiblioteket.deck.Constants.*" %>
<%@ page import="java.net.InetAddress" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page pageEncoding="UTF-8"
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%
        response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
        response.setHeader("Pragma","no-cache"); //HTTP 1.0
        response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
    %>
    <title>DigiVid - Video Digitalisation at Statsbiblioteket</title>
    <link rel="stylesheet" type="text/css" media="all"
          href="style.css" />
    <link rel="stylesheet" type="text/css" media="all"
          href="./jscalendar/calendar-win2k-cold-1.css" title="win2k-cold-1"/>

    <!-- main calendar program -->
    <script type="text/javascript" src="./jscalendar/calendar.js"></script>
    <!-- language for the calendar -->
    <script type="text/javascript"
            src="./jscalendar/lang/calendar-da.js"></script>
    <!-- the following script defines the Calendar.setup helper function, which makes
adding a calendar a matter of 1 or 2 lines of code. -->
    <script type="text/javascript"
            src="./jscalendar/calendar-setup.js"></script>
    <script type="text/javascript" xml:space="preserve">
        function gotopage(page){
            document.getElementById("page_name").setAttribute("value", page);
            document.nav_form.submit();
        }
    </script>
</head>

<body>
    <h1>Welcome to DigiVid</h1>
    <%
        //String server_name = DEFAULT_STREAMSERVER_NAME;
        String server_name = InetAddress.getLocalHost().getHostName();
        String IP_ADDRESS = java.net.InetAddress.getByName(server_name).getHostAddress();
    %>
    <div id="navigation">
        <form action="simple_dispatcher.jsp" name="nav_form" method="get">
            <a href="#" class="main_nav" onclick="gotopage('<%=STATUS_JSP%>');">Status</a>
            <a href="#" class="main_nav" onclick="gotopage('<%=PREVIEW_JSP%>');">Preview</a>
            <a href="#" class="main_nav" onclick="gotopage('<%=RECORD_JSP%>');">Record</a>
            <a href="#" class="main_nav" onclick="gotopage('<%=PLAYBACK_JSP%>');">Post Process</a>
            <input id="page_name" type="hidden" name="<%=PAGE_PARAM%>"/>
            <input type="hidden" name="<%=ENCODER_IP_PARAM%>" value="<%=IP_ADDRESS%>" />
            <input type="hidden" name="<%=ENCODER_NAME_PARAM%>" value="<%=server_name%>" />
        </form >
    </div>

    <div id="content">
        <%
            String content_page  = (String) request.getAttribute(PAGE_ATTR);
        %>
        <% if (content_page != null) {%>
        <jsp:include page="<%=content_page%>"></jsp:include>
        <%}%>

    </div>


</body>
</html>
