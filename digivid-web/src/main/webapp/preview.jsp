<%@ page import="java.util.*" %>
<%@ page import="dk.statsbiblioteket.deck.client.RecorderCtrl" %>
<%@ page import="static dk.statsbiblioteket.deck.client.webinterface.WebConstants.*" %>
<%@ page import="java.net.InetAddress" %>


<script type="text/javascript">
        function loadEncoderIP() {
            document.preview_form.<%=ENCODER_IP_PARAM%>.value = document.nav_form.<%=ENCODER_IP_PARAM%>.value;
        }
</script>

<%
    String encoder_name = request.getParameter(ENCODER_NAME_PARAM);
    String encoderIP = null;
    if (encoder_name != null) {
        encoderIP = InetAddress.getByName(encoder_name).getHostAddress();
    }
%>

<form name="preview_form" action="Control" method="post" >
    <input type="radio" name="<%=CARD_NAME_PARAM%>" value="1" checked="checked" onclick="document.preview_form.streamPortHTTP.value='9001';">Source A</input>
    <input type="radio" name="<%=CARD_NAME_PARAM%>" value="3"                   onclick="document.preview_form.streamPortHTTP.value='9003';">Source B</input>
    <input type="hidden" name="<%=INPUT_CHANNEL_ID_PARAM%>" value="SB-Tape1"/>
    <input type="hidden" name="<%=STREAM_PROTOCOL_PARAM%>" value="HTTP"/>
    <input type="hidden" name="<%=STREAM_PORT_HTTP_PARAM%>" value="9001"/>
    <input type="hidden" name="<%=ENCODER_IP_PARAM%>" value="<%=encoderIP%>"/>
    <input type="hidden" name="<%=ENCODER_NAME_PARAM%>" value="<%=encoder_name%>"/>
    <input type="submit" name="<%=CONTROL_COMMAND_PARAM%>" value="<%=START_PREVIEW%>" />
</form>


