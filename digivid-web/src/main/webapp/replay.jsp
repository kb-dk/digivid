 <%@ page import="java.util.*" %>
<%@ page import="dk.statsbiblioteket.deck.client.RecorderCtrl" %>
 <%@ page import="dk.statsbiblioteket.deck.client.FSCtrl"%>


 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
    <TITLE>A Sample FORM using POST</TITLE>
    <META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <META name="author" content="std">
    <META name="copyright" content="&copy; 2007 Statsbiblioteket">
    <META name="keywords" content="TV, Radio, Library">
    <META name="date" content="2007-01-12T08:49:37+00:00">
    <LINK rel="stylesheet" type="text/css" href="panels.css">
    <script type="text/javascript" src="panels.js" language="JavaScript"></script>
    <script type="text/javascript" src="debugger.js" language="JavaScript"></script>
    <script type="text/javascript" src="iframe.js" language="JavaScript"></script>
</HEAD>

<BODY BGCOLOR="#CPCPCP" id="theBody" onload="parent.showPage('contentLayer')">
<script language="JavaScript"><!--

//--></script>
<!-- replay -->
<!--<div id="theReplayFormDiv">-->
<!--    <FORM name="stateForm" id="stateForm" action="CtrlServlet" onsubmit="return callToServer(this.name)" target="RSIFrame">-->

<!--<FORM NAME="ctrl" ACTION="javascript:loadPage('contentLayer',null,'CtrlServlet');" METHOD="GET">-->
<FORM NAME="ctrl" ACTION="/bartdeck/CtrlServlet" METHOD="GET" onsubmit="javascript:loadPage('contentLayer',null,'CtrlServlet');" >
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%" bgcolor="gray">
<!--<TR><TD COLSPAN="3" bgcolor="white">
       <H4>replay recordings</H4>
</TD></TR>-->
<TR>
    <TD>
        <span class="panellist">Choose Input File:</span>
    </TD>
    <TD COLSPAN="3" ALIGN="RIGHT">
        <%
            Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();

                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length == 1) {
                    String paramValue = paramValues[0];
                    if (paramValue.length() == 0) {
                        System.out.print("<I>No Value</I>");
                    } else {
                        if (paramName.equalsIgnoreCase("encoderIP")) {
                            String encoderIP = paramValue;
        %>
        <INPUT TYPE="HIDDEN" name="encoderIP" value="<%= encoderIP %>"><BR/>
        <SELECT size="5" name="fileName">
        <%
                          System.out.println("EncoderIP: " + encoderIP);
                          FSCtrl ctrl = new FSCtrl(encoderIP);

                          //List records = ctrl.getFileListUnix();
                          List records = ctrl.getFileInfo();

                          if (!records.isEmpty()) {
                              for ( int i = 0; i < records.size(); i++ ) {
                                Object recordsitem = records.get(i);
                                if (recordsitem instanceof String[]) {
                                    String[] info =  (String[]) recordsitem;
                                    for (int j=0; j < info.length; j++) {
                                        System.out.println("index 1: " +i+ "index 2: " + j );
                                         if (ctrl.isEven(j)){
                                            %>
                                            <OPTION value="<%= info[j] %>">
                                            <%
                                         }
                                         else {
                                            %>
                                            <%= info[j-1]%> ( <%= info[j] %> KB) </OPTION>
                                            <%
                                         }
                                    }
                                } else {
                                    System.out.println("Empty Records List");
                                    %>
                                    <OPTION value="empty">empty</OPTION>
                                    <%
                                }
                             }
                          }
                      }
                  }
               }
            }
            %>
        </SELECT><BR/>    
    </TD>
</TR>
<TR>
    <TD COLSPAN="1">
        <!--<INPUT TYPE="RADIO" NAME="streamProtocol" VALUE="UDP">UDP-->
    </TD>
    <TD COLSPAN="3">
        <!-- Stream Port:<INPUT TYPE="TEXT" NAME="streamPortUDP"> default:1234-->
    </TD>
</TR>
<TR>
    <TD COLSPAN="1">
        <!--<INPUT TYPE="RADIO" NAME="streamProtocol" VALUE="HTTP" CHECKED>HTTP-->
        <INPUT TYPE="HIDDEN" NAME="streamProtocol" VALUE="HTTP">
    </TD>
    <TD COLSPAN="3">
        <!--Stream Port:<INPUT TYPE="TEXT" NAME="streamPortHTTP" > default: 9000-->
        <INPUT TYPE="HIDDEN" NAME="streamPortHTTP" VALUE="9001">
    </TD>
</TR>
<TR>
    <TD>
        <!--<INPUT TYPE="RADIO" NAME="streamProtocol" VALUE="RTP">RTSP-->
    </TD>
    <TD>
        <!--Stream Port:<INPUT TYPE="TEXT" NAME="streamPortRTP"> default: 1234 -->
    </TD>
    <TD>
        <!--Stream Name:<INPUT TYPE="TEXT" NAME="streamName" VALUE="vhs">  -->
    </TD>
</TR>
<TR>
    <TD COLSPAN="4" ALIGN="right" bgcolor="red">
        <a onclick="javascript:debug(); return true;">debug</a>
        <INPUT TYPE="HIDDEN" NAME="pageTitle" VALUE="DECK-REPLAY">
        <INPUT TYPE="HIDDEN" NAME="ctrlCommand" VALUE="replay">
        <INPUT TYPE="SUBMIT" VALUE="replay"></TD>

</TR>
</TABLE>
<BR>
</FORM>
<!--</div>-->
<!--
  <div id="theFormDiv">
        <form name="stateForm" id="stateForm" action="server.html" onsubmit="return callToServer(this.name)">
        Select a state from this menu:

        <select name="state">
            <option selected value="">
            <option value="AL"> Alabama

            <option value="AK"> Alaska
            <option value="AZ"> Arizona
            <option value="AR"> Arkansas
            <option value="CA"> California
            <option value="CO"> Colorado
            <option value="CT"> Connecticut
            <option value="DE"> Delaware
            <option value="DC"> District of Columbia
            <option value="FL"> Florida

            <option value="GA"> Georgia
            <option value="HI"> Hawaii
            <option value="ID"> Idaho
            <option value="IL"> Illinois
            <option value="IN"> Indiana
            <option value="IA"> Iowa
            <option value="KS"> Kansas
            <option value="KY"> Kentucky
            <option value="LA"> Louisiana

            <option value="ME"> Maine
            <option value="MD"> Maryland
            <option value="MA"> Massachusetts
            <option value="MI"> Michigan
            <option value="MN"> Minnesota
            <option value="MS"> Mississippi
            <option value="MO"> Missouri
            <option value="MT"> Montana
            <option value="NE"> Nebraska

            <option value="NV"> Nevada
            <option value="NH"> New Hampshire
            <option value="NJ"> New Jersey
            <option value="NM"> New Mexico
            <option value="NY"> New York
            <option value="NC"> North Carolina
            <option value="ND"> North Dakota
            <option value="OH"> Ohio
            <option value="OK"> Oklahoma

            <option value="OR"> Oregon
            <option value="PA"> Pennsylvania
            <option value="RI"> Rhode Island
            <option value="SC"> South Carolina
            <option value="SD"> South Dakota
            <option value="TN"> Tennessee
            <option value="TX"> Texas
            <option value="UT"> Utah
            <option value="VT"> Vermont

            <option value="VA"> Virginia
            <option value="WA"> Washington
            <option value="WV"> West Virginia
            <option value="WI"> Wisconsin
            <option value="WY"> Wyoming
        </select><br>
        <input type="submit" value="submit"> <br>

            <select multiple="multiple" name="zipNames" style="width:50%; height:100px">
            </select><br><br>
            <a onclick="javascript:debug(); return true;">debug</a>

        </form>
    </div>
    -->
<!--<div id="responseMessage" style="display:none"></div> -->
<!--<div id="contentLayer" style="display:none"></div>-->
</BODY>
</HTML>