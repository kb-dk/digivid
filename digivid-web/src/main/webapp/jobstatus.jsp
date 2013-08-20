<%@ page import="dk.statsbiblioteket.deck.client.CommandLineCtrl" %>
<%@ page import="dk.statsbiblioteket.deck.client.FSCtrl" %>
<%@ page import="dk.statsbiblioteket.deck.client.webinterface.WebConstants" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.util.regex.Pattern" %>
<%--<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="panels.js" language="JavaScript"></script>
    <script type="text/javascript" src="debugger.js" language="JavaScript"></script>
    <script type="text/javascript" src="iframe.js" language="JavaScript"></script>
</HEAD>
<BODY id="theBody" onload="parent.showPage('contentLayer')">--%>
<script type="text/javascript">
        function loadEncoderIP(aform, encoderip) {
            aform.<%=WebConstants.ENCODER_IP_PARAM%>.value = document.nav_form.encoderip.value;
        }
</script>
<%
    //String encoder_name = request.getParameter(ENCODER_NAME_PARAM);
    //if (encoder_name == null) {
    //    encoder_name = Constants.DEFAULT_STREAMSERVER_NAME;
    //}
    String encoder_name = InetAddress.getLocalHost().getHostName();
    String encoderIP = java.net.InetAddress.getByName(encoder_name).getHostAddress();
    /*String encoder_name = encoderIP;
    if (encoderIP == null || "".equals(encoderIP)) {
        encoderIP = Constants.DEFAULT_STREAMSERVER_IP;
    }
    encoder_name = java.net.InetAddress.getByName(encoderIP).getHostName();*/
%>
Status for <%=encoder_name%>:
<%--<form action="get"><input type="hidden" name="<%=ENCODER_IP_PARAM%>" value="<%=encoderIP%>"/></form>--%>
<%
    CommandLineCtrl ctrl = new CommandLineCtrl(encoderIP, "ps -ww -C vlc,start_recording.sh -o args",0,1);
    List<String> jobs = null;
    try {
        jobs = ctrl.execute();
    } catch (Exception e) {
        throw new RuntimeException("Error listing jobs on ('"+encoder_name+"','"+encoderIP+"')", e);
    }
    List<String> previews = new ArrayList<String>();
    List<String> playbacks = new ArrayList<String>();
    List<String> recordings = new ArrayList<String>();
    for (String job : jobs) {
        if (job.startsWith("vlc") && job.contains("/dev/video")) {
            previews.add(job);
        } else if (job.startsWith("vlc") && !job.contains("/dev/video")) {
            playbacks.add(job);
        } else
        if (job.contains("bin/start_recording.sh") && !job.contains("grep")) {
            recordings.add(job);
        }
    }

    // If there are recordings in progress then we need to get file information for them
    FSCtrl fctrl = new FSCtrl(encoderIP);
    List<String[]> files = (List<String[]>) fctrl.getFileInfo();

%>

<!-- Preview jobs -->
<%
    if (!previews.isEmpty()) {
%>
<table class="status_table">
    <tr><th colspan="3">Previews</th></tr>
    <%
        for (String preview : previews) {
            String vlc_matcher = ".*/dev/video([0-9]).*dst=(.*):([0-9]{4}).*";
            Pattern vlc_pattern = Pattern.compile(vlc_matcher);
            Matcher matcher = vlc_pattern.matcher(preview);
            if (!matcher.matches()) {
                throw new RuntimeException("Job '"+preview+"' did not match expected regexp '"+vlc_matcher+"'");
            }
    %>
         <tr>
             <td>
                 <%
                     String source = "";
                     String card_number = matcher.group(1);
                     if (card_number.equals("1")) {
                         source = "Source A";
                     } else {
                         source = "Source B";
                     }
                     String previewIP = matcher.group(2);
                 %>
                 <%=source%>
             </td>
             <td>
                 <form action="Control" method="post">
                     <input type="hidden" name="<%=WebConstants.CARD_NAME_PARAM%>" value="<%=matcher.group(1)%>"/>
                     <input type="hidden" name="<%=WebConstants.ENCODER_NAME_PARAM%>" value="<%=InetAddress.getByName(previewIP).getHostName()%>" />
                     <input type="hidden" name="<%=WebConstants.STREAM_PORT_HTTP_PARAM%>" value="<%=matcher.group(3)%>"/>
                     <input type="hidden" name="<%=WebConstants.CONTROL_COMMAND_PARAM%>" value="<%=WebConstants.START_PREVIEW%>" />
                     <input type="submit" name="view" value="view"/>
                 </form>
             </td>
             <td>
                 <form action="Control" method="post">
                     <input type="hidden" name="<%=WebConstants.CARD_NAME_PARAM%>" value="<%=matcher.group(1)%>"/>
                     <input type="hidden" name="<%=WebConstants.ENCODER_NAME_PARAM%>" value="<%=InetAddress.getByName(previewIP).getHostName()%>" />
                     <input type="hidden" name="<%=WebConstants.STREAM_PORT_HTTP_PARAM%>" value="<%=matcher.group(3)%>"/>
                     <input type="hidden" name="<%=WebConstants.CONTROL_COMMAND_PARAM%>" value="<%=WebConstants.STOP_PREVIEW%>" />
                     <input type="submit" name="kill" value="kill"/>
                 </form>
             </td>
         </tr>
    <%}%>
</table>

<%}%>


 <!-- Playback jobs -->
<%
    if (!playbacks.isEmpty()) {
%>
<table class="status_table">
    <tr><th colspan="2">Playbacks</th></tr>
    <%
        for (String playback : playbacks) {
            String vlc_matcher = ".*(([/]+([^/^\\s]+))).*dst=(.*):([0-9]{4}).*";
            Pattern vlc_pattern = Pattern.compile(vlc_matcher);
            Matcher matcher = vlc_pattern.matcher(playback);
            if (!matcher.matches()) {
                throw new RuntimeException("Job '"+playback+"' did not match expected regexp '"+vlc_matcher+"'");
            }
            String filename = matcher.group(3);
            String ip = matcher.group(4);
            String port = matcher.group(5);
    %>
         <tr>
             <td>
                 <%=filename%>
             </td>
             <!--<td>
                 <form action="Control" method="post">
                     <input type="hidden" name="<%=WebConstants.ENCODER_NAME_PARAM%>" value="<%=InetAddress.getByName(ip).getHostName()%>" />
                     <input type="hidden" name="<%=WebConstants.STREAM_PORT_HTTP_PARAM%>" value="<%=port%>"/>
                     <input type="hidden" name="<%=WebConstants.FILE_NAME_PARAM%>" value=<%=filename%> />
                     <input type="hidden" name="<%=WebConstants.CONTROL_COMMAND_PARAM%>" value="<%=WebConstants.START_POSTPROCESS%>" />
                     <input type="submit" name="view" value="view"/>
                 </form>
             </td>-->
             <td>
                 <form action="Control" method="post">
                     <input type="hidden" name="<%=WebConstants.ENCODER_NAME_PARAM%>" value="<%=InetAddress.getByName(ip).getHostName()%>" />
                     <input type="hidden" name="<%=WebConstants.STREAM_PORT_HTTP_PARAM%>" value="<%=port%>"/>
                     <input type="hidden" name="<%=WebConstants.CONTROL_COMMAND_PARAM%>" value="<%=WebConstants.STOP_PLAYBACK%>" />
                     <input type="submit" name="kill" value="kill"/>
                 </form>
             </td>
         </tr>
    <%}%>
</table>

<%}%>


<%
if (!recordings.isEmpty()) {
%>

<table class="status_table">
    <tr><th colspan="2">Recordings</th><th>File Size (kB)</th><th>Expected Size</th><th>%age complete</th><th>Time remaining (mins)</th></tr>
    <%
        for (String recording : recordings) {
            //The format of the recording string is like 
            // /bin/bash /usr/bin/recordd tape digivid 172.18.255.242 /dev/video0
            // K11-DR1 720 576 mpeg2 3000000 4000000 /home/bytestroop/Deck/records
            // ldjcn 2007_03_05_15_02_31_827 3600000 6000 1173794269000
            // i.e. recordd has 16 parameters.
            // /bin/bash /home/${digividUser}/Deck/v4l/bin/start_recording.sh -d 0 -i K11-DR1 -a 1 -f tttt -l 2 -o 1172749183000
            // 14

            String[] split_recording = recording.split("\\s");
            if (split_recording.length < 14) {
                throw new RuntimeException("Unparseable recording command line: '" + recording + "'");
            }
            String device_parameter = split_recording[3];

            long file_length_bytes = 0;
            long expected_file_length_bytes = 0;
            double completion_percentage = 0;
            long missing_time_minutes = 0;
            try {
                //String orig_start_timestamp = split_recording[13];
                //int video_format = Integer.parseInt(split_recording[7]);
                //long expected_length_milliseconds = Long.parseLong(split_recording[11]) * 60L * 1000L;
                //String recording_name = split_recording[9];
                //
                //Now identify the file corresponding to this recording by looking for a file starting with the
                //timestamp and containing the recording_name
                String orig_start_timestamp = split_recording[13];
                int video_format = Integer.parseInt(split_recording[7]);
                long expected_length_milliseconds = Long.parseLong(split_recording[11]) * 60L * 1000L;
                String recording_name = split_recording[9];
                String[] this_file = new String[]{"", "0"};
                for (String[] file : files) {
                    if (file[0].startsWith(orig_start_timestamp) && file[0].contains(recording_name)) {
                        this_file = file;
                    }
                }
                String filename = this_file[0];
                file_length_bytes = Long.parseLong(this_file[1]) * 1024L;
                long bitrate = 0;  //bits/second
                if (1 == video_format) {
                    bitrate = 1150000L;
                } else {
                    bitrate = 6500000L;
                }
                expected_file_length_bytes = bitrate * expected_length_milliseconds / (8 * 1000);
                completion_percentage = 100 * ((double) file_length_bytes) / ((double) expected_file_length_bytes);
                missing_time_minutes = ((expected_file_length_bytes - file_length_bytes) * 8L / bitrate) / 60L;

            }  catch (Exception e) {
               //Just proceed to display the information we have
            }
    %>
    <tr>
        <td>
            <%
                String source="";
                if (device_parameter.equals("0")) {
                    source = "Source A";
                } else {
                    source = "Source B";
                }
            %>
            <%=source%>
        </td>
        <td>
            <!--<form action="Control" method="post" onsubmit="loadEncoderIP(this, <%=encoderIP%>);"> -->
             <form action="Control" method="post">   
                <input type="hidden" name="<%=WebConstants.CARD_NAME_PARAM%>" value="<%=device_parameter%>"/>
                <input type="hidden" name="<%=WebConstants.ENCODER_NAME_PARAM%>" value="<%=encoder_name%>"/>
                <input type="hidden" name="<%=WebConstants.CONTROL_COMMAND_PARAM%>" value="<%=WebConstants.STOP_RECORDING%>" />
                <input type="submit" name="kill" value="kill"/>
            </form>
        </td>
        <td><%=file_length_bytes/1000L%></td>
        <td><%=expected_file_length_bytes/1000L%></td>
        <td><%= (int) completion_percentage%></td>
        <td><%=missing_time_minutes%></td>
        <!--<%=recording%> -->
    </tr>

    <%}%>
</table>
<%}%>




