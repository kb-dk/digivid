<%@ page import="dk.statsbiblioteket.deck.Constants" %>
<%@ page import="dk.statsbiblioteket.deck.client.CommandLineCtrl" %>
<%@ page import="dk.statsbiblioteket.deck.client.FSCtrl" %>
<%@ page import="dk.statsbiblioteket.deck.client.webinterface.WebConstants" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<script type="text/javascript">
    function submit_file_form(name, length, is_processed) {
        document.playback_form.<%=WebConstants.FILE_NAME_PARAM%>.value=name;
        document.playback_form.<%=WebConstants.FILE_LENGTH_PARAM%>.value=length;
        document.playback_form.<%=WebConstants.IS_PROCESSED_PARAM%>.value=is_processed;
        document.playback_form.submit();
    }
</script>
<%
    String encoder_name = request.getParameter(WebConstants.ENCODER_NAME_PARAM);
    String encoderIP = null;
    if (encoder_name != null) {
        encoderIP = InetAddress.getByName(encoder_name).getHostAddress();
    }

   /* String encoderIP = request.getParameter(ENCODER_IP_PARAM);
    if (encoderIP == null) {
        encoderIP = Constants.DEFAULT_STREAMSERVER_IP;
    }*/
    FSCtrl ctrl = new FSCtrl(encoderIP);
    List<String[]> all_records = (List<String[]>) ctrl.getFileInfo();
    List<String[]> records = new ArrayList<String[]>();
    //
    //Now get the current files
    CommandLineCtrl ctrl2 = new CommandLineCtrl(encoderIP, Constants.RECORDER_BINDIR + "/get_current_recordings.sh",0,1);
    List<String> current = ctrl2.execute();
    for (String[] record : all_records) {
        if (!current.contains(record[0].trim())) records.add(record);
    }

    if (records.isEmpty()) {
%>
No files found
<%
        return; }
%>
<h3>Unprocessed Files</h3>
<%
    for (String[] record: records) {
        String file_name = record[0];
        String file_length =  record[1];
        if (WebConstants.UNPROCESSED_FILE_PATTERN.matcher(file_name).matches()) {
%>
<a href="#" onclick="submit_file_form('<%=file_name%>', '<%=file_length%>', false)"><%=file_name%></a><br/>
<%}} %>
<h3>Processed Files</h3>
<%
    for (String[] record: records) {
         String file_name = record[0];
        String file_length =  record[1];
        if (WebConstants.BART_FILE_PATTERN.matcher(record[0]).matches()) {
%>
<a href="#" onclick="submit_file_form('<%=file_name%>', '<%=file_length%>', true)"><%=file_name%></a><br/>
<%}} %>

<form action="Control" method="post" name="playback_form">
    <input type="hidden" name="<%=WebConstants.FILE_NAME_PARAM%>"/>
    <input type="hidden" name="<%=WebConstants.FILE_LENGTH_PARAM%>"/>
    <input type="hidden" name="<%=WebConstants.ENCODER_IP_PARAM%>" value="<%=encoderIP%>"/>
    <input type="hidden" name="<%=WebConstants.ENCODER_NAME_PARAM%>" value="<%=encoder_name%>"/>
    <input type="hidden" name="<%=WebConstants.STREAM_PROTOCOL_PARAM%>" value="HTTP" />
    <input type="hidden" name="<%=WebConstants.STREAM_PORT_HTTP_PARAM%>" value="9004"/>
    <input type="hidden" name="<%=WebConstants.IS_PROCESSED_PARAM%>" />
    <input type="hidden" name="<%=WebConstants.CONTROL_COMMAND_PARAM%>" value="<%=WebConstants.START_POSTPROCESS%>"/>
</form>