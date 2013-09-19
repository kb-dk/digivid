<%@ page import="dk.statsbiblioteket.deck.client.webinterface.WebConstants" %>
<%@ page import="java.net.InetAddress" %>

<script type="text/javascript">
    function loadEncoderName() {
        document.preview_form.<%=WebConstants.ENCODER_NAME_PARAM%>.value = document.nav_form.<%=WebConstants.ENCODER_NAME_PARAM%>.value;
        }
</script>

<%
    String encoderName = request.getParameter(WebConstants.ENCODER_NAME_PARAM);
%>

<form name="preview_form" action="Control" method="post" >
    <input type="radio" name="<%=WebConstants.CARD_NAME_PARAM%>" value="1" checked="checked" onclick="document.preview_form.streamPortHTTP.value='9001';">Source A</input>
    <input type="radio" name="<%=WebConstants.CARD_NAME_PARAM%>" value="3"                   onclick="document.preview_form.streamPortHTTP.value='9003';">Source B</input>
    <input type="hidden" name="<%=WebConstants.INPUT_CHANNEL_ID_PARAM%>" value="SB-Tape1"/>
    <input type="hidden" name="<%=WebConstants.STREAM_PROTOCOL_PARAM%>" value="HTTP"/>
    <input type="hidden" name="<%=WebConstants.STREAM_PORT_HTTP_PARAM%>" value="9001"/>
    <input type="hidden" name="<%=WebConstants.ENCODER_NAME_PARAM%>" value="<%=encoderName%>"/>
    <input type="submit" name="<%=WebConstants.CONTROL_COMMAND_PARAM%>" value="<%=WebConstants.START_PREVIEW%>" />
</form>


