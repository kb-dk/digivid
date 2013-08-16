<%@ page import="static dk.statsbiblioteket.deck.client.webinterface.WebConstants.*" %>
<%@ page import="dk.statsbiblioteket.deck.client.webinterface.ControlServlet" %>
<%@ page import="dk.statsbiblioteket.deck.client.webinterface.WebConstants" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.regex.Matcher" %>
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page pageEncoding="UTF-8"
        %>
<%
    SimpleDateFormat BART_DATE_FORMAT = ControlServlet.getTime_format();
    String encoder_name = request.getParameter(ENCODER_NAME_PARAM);
    String encoderIP = null;
    if (encoder_name != null) {
        encoderIP = InetAddress.getByName(encoder_name).getHostAddress();
    }

    //This is where we process the filename and length
    String filename = request.getParameter(FILE_NAME_PARAM);
    String channel_code = null;
    long file_length_seconds = 0L;
    String start_timestamp_string = null;
    int capture_format = 0;
    long start_timestamp = 0L;
    long end_timestamp = 0L;
    String start_date_S = null;
    String end_date_S = null;

    if (request.getParameter(IS_PROCESSED_PARAM).equals("false")) {
        Matcher m = WebConstants.UNPROCESSED_FILE_PATTERN.matcher(filename);
        if (!m.matches()) {
            throw new RuntimeException("Cannot parse filename: " + filename + "\nWith regexp: " + WebConstants.UNPROCESSED_FILE_REGEXP);
        }
        start_timestamp_string = m.group(1);    //milliseconds
        channel_code = m.group(4);
        String format = m.group(5);

        if ("mpeg1".equals(format)) {
            capture_format = 1;
        } else if ("mpeg2".equals(format)) {
            capture_format = 2;
        } else {
            throw new RuntimeException("Unknown capture format: '" + format + "'");
        }
        start_timestamp = Long.parseLong(start_timestamp_string);
        end_timestamp = start_timestamp + Integer.parseInt(request.getAttribute(FILE_LENGTH_ATTR).toString())*1000;
        Date start_date = new Date(start_timestamp);
        start_date_S = BART_DATE_FORMAT.format(start_date);
        Date end_date = new Date(end_timestamp);
        end_date_S = BART_DATE_FORMAT.format(end_date);

    } else {
        Matcher m = WebConstants.BART_FILE_PATTERN.matcher(filename);
        if (!m.matches()) {
            throw new RuntimeException("Cannot parse filename: " + filename + "\nWith regexp: " + WebConstants.BART_FILE_REGEXP);
        }
        String bart_start_time = m.group(5);
        Date start_date = BART_DATE_FORMAT.parse(bart_start_time);
        String bart_end_time = m.group(6);
        Date end_date = BART_DATE_FORMAT.parse(bart_end_time);
        channel_code = m.group(3);
        String format = m.group(4);
        start_timestamp = start_date.getTime();
        capture_format = 0;
        if ("mpeg1".equals(format)) {
            capture_format = 1;
        } else if ("mpeg2".equals(format)) {
            capture_format = 2;
        } else {
            throw new RuntimeException("Unknown capture format: '" + format + "'");
        }
        start_date_S = BART_DATE_FORMAT.format(start_date);
        end_date_S = BART_DATE_FORMAT.format(end_date);
    }

%>

<script type="text/javascript">
    function play_video(url) {
        document.getElementById("video").innerHTML = getVideoObject(url);
        document.vlc.play();
        document.getElementById("video_controls").setAttribute("style", "visibility:visible");
    }

    function getVideoObject(url) {
        // return '<embed name="vlc" target="' + url + '" WIDTH="480" HEIGHT="376" AUTOPLAY="true" CONTROLLER="true" LOOP="false" type="application/x-mplayer2" scale="tofit" kioskmode="false">'+
        //        '</embed>'
        return '<embed name="vlc" target="' + url + '" WIDTH="480" HEIGHT="376" AUTOPLAY="true" CONTROLLER="true" LOOP="false" type="application/x-google-vlc-plugin" scale="tofit" kioskmode="false">' +
                '</embed>'
    }

</script>

<div id="video"></div>
<div id="video_controls" style="visibility:collapse;">
    <!--<input type="button" value="Play" onclick="document.vlc.play();" />
    <input type="button" value="Pause" onclick="document.vlc.pause();" /> -->
    <input type="button" value="Stop" onclick="document.vlc.stop();"/>
    <!--<input type="button" value="+1 min" onclick="document.vlc.seek(60, true);" />-->

</div>
<%
    String stream_url = (String) request.getAttribute("stream_url");
    Thread.sleep(1000);
%>
<input type="button" onclick="play_video('<%=stream_url%>')" value="afspil"/>

<form action="Control" method="post">

    <fieldset>
        <legend>Postprocessing</legend>
        <div class="field">
            <label for="filename">Filename:</label>
            <input id="filename" readonly="readonly" type="text" class="input_readonly"  size="100" value="<%=filename%>"/>
        </div>

        <div class="field">
            <label for="duration">Length:</label>
            <input name="<%=RECORDING_TIME_PARAM%>" readonly="readonly" id="duration" class="input_readonly" type="text"
                   value="<%=request.getAttribute(FILE_LENGTH_ATTR)%>">&nbsp;seconds
        </div>



        <div class="field">
            <label for="vhs_label">VHS Label:</label>
            <textarea id="vhs_label" name="vhs_label" class="input" rows="3"></textarea>
        </div>

        <div class="field">
            <label for="quality">Quality:</label>
            <select id="quality" name="quality" class="input" >
                <option value="1">1 (Worst Quality)</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5" selected="selected">5 (Acceptable Quality)</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
                <option value="9">9</option>
                <option value="10">10 (Best Quality)</option>
            </select>
        </div>

        <div class="field">
            <label for="start_time_display_field">Start date/time:</label>
            <input id="start_time_display_field" type="text" readonly="readonly" name="<%=START_TIME_PARAM%>" value="<%=start_timestamp%>"/>
        </div>

        <div class="field">
            <label for="end_time_display_field">End date/time:</label>
            <input id="end_time_display_field" type="text" readonly="readonly" name="<%=END_TIME_PARAM%>" value="<%=end_timestamp%>" />
        </div>




        <div class="field">
            <label for="channel_label">Channel:</label>
            <select id="channel_label" name="<%=CHANNEL_LABEL_PARAM%>" class="input">
                <%
                    for (String channel : lognames.keySet()) {
                        String selected = "";
                        if (channel_code.contains(channel)) {
                            selected = "selected=\"selected\"";
                        }
                %>
                <option value="<%=channel%>" <%=selected%>><%=channel%>
                </option>
                <%
                    }
                %>
            </select>
        </div>

        <input type="hidden" name="<%=CAPTURE_FORMAT_PARAM%>" value="<%=capture_format%>"/>
        <input type="hidden" name="<%=CONTROL_COMMAND_PARAM%>" value="<%=POSTPROCESS%>"/>
        <input type="hidden" name="<%=ENCODER_IP_PARAM%>" value="<%=encoderIP%>"/>
        <input type="hidden" name="<%=ENCODER_NAME_PARAM%>" value="<%=encoder_name%>"/>
    </fieldset>
    <input type="button" name="Reject" value="Reject" onclick="gotopage('<%=PLAYBACK_JSP%>')"/>
    <input type="submit" name="Process" value="Process"/>
</form>

<script type="text/javascript">

    var date_format = "<%=ControlServlet.jscalendar_format_string%>";

    var start_date = new Date(<%=start_timestamp%>);
    var start_calendar = Calendar.setup({
        daFormat: date_format,
        displayInput: "start_time_display_field",
        date: start_date,
        eventName: "dblclick",
        //onUpdate       :    setCurrentStartDate,
        showsTime: true,            // will display a time selector
        singleClick: true,           // double-click mode
        step: 1,                // show all years in drop-down boxes (instead of every other year as default)
        firstDay: 1
    });
    document.getElementById("start_time_display_field").value = '<%=start_date_S%>';


    var end_date = new Date(<%=end_timestamp%>);
    var end_calendar = Calendar.setup({
        daFormat: date_format,
        displayInput: "end_time_display_field",
        date: end_date,
        eventName: "dblclick",
        //onUpdate       :    setCurrentStartDate,
        showsTime: true,            // will display a time selector
        singleClick: true,           // double-click mode
        step: 1,                // show all years in drop-down boxes (instead of every other year as default)
        firstDay: 1
    });
    document.getElementById("end_time_display_field").value = '<%=end_date_S%>';

</script>