<%@ page import="dk.statsbiblioteket.deck.client.datastructures.Comments" %>
<%@ page import="dk.statsbiblioteket.deck.client.webinterface.WebConstants" %>
<%@ page import="dk.statsbiblioteket.deck.client.webinterface.WebParams" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page pageEncoding="UTF-8"
        %>
<%
    String encoder_name = request.getParameter(WebConstants.ENCODER_NAME_PARAM);
    String encoderIP = null;
    if (encoder_name != null) {
        encoderIP = InetAddress.getByName(encoder_name).getHostAddress();
    }

    //This is where we process the filename and length
    String filename = request.getParameter(WebConstants.FILE_NAME_PARAM);

    Comments comments = WebParams.getComments(filename, encoderIP);



    String channel_code = comments.getChannelLabel();

    String start_date_S = null;
    String end_date_S = null;
    Date start_date = new Date(comments.getStartDate());
    Date end_date;


    if (request.getParameter(WebConstants.IS_PROCESSED_PARAM).equals("false")) {
        //The end unix timestamp as a long
        long end_timestamp = start_date.getTime() + Integer.parseInt(request.getAttribute(WebConstants.FILE_LENGTH_ATTR).toString()) * 1000;
        //The end time as a Date
        end_date = new Date(end_timestamp);
    } else {
        end_date = new Date(comments.getEndDate());
    }

    String format = comments.getCaptureFormat();
    if (!("mpeg1".equals(format) || "mpeg2".equals(format))){
        throw new RuntimeException("Unknown capture format: '" + format + "'");
    }


    start_date_S = WebConstants.getPresentationDateFormat().format(start_date);

    end_date_S = WebConstants.getPresentationDateFormat().format(end_date);

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
    String stream_url = (String) request.getAttribute(WebConstants.STREAM_URL_ATTR);
    Thread.sleep(1000);
%>
<input type="button" onclick="play_video('<%=stream_url%>')" value="afspil"/>

<form action="Control" method="post">

    <fieldset>
        <legend>Postprocessing</legend>
        <div class="field">
            <label for="filename">Filename:</label>
            <input id="filename" name="<%=WebConstants.FILE_NAME_PARAM%>" readonly="readonly" type="text" class="input_readonly" size="100"
                   value="<%=filename%>"/>
        </div>

        <div class="field">
            <label for="duration">Length:</label>
            <input name="<%=WebConstants.RECORDING_TIME_PARAM%>" readonly="readonly" id="duration" class="input_readonly" type="text"
                   value="<%=request.getAttribute(WebConstants.FILE_LENGTH_ATTR)%>">&nbsp;seconds
        </div>


        <div class="field">
            <label for="vhs_label">VHS Label:</label>
            <textarea id="vhs_label" name="<%=WebConstants.VHS_LABEL_PARAM%>" class="input" rows="3"
                      cols="100"><%=comments.getComments()%></textarea>
        </div>

        <div class="field">
            <label for="quality">Quality:</label>
            <select id="quality" name="<%=WebConstants.RECORDING_QUALITY%>" class="input">
                <%
                    Integer quality = comments.getQuality();
                    if (quality == null || quality < 0){
                        quality = 5;
                    }
                    for (int qualityNr = 1; qualityNr <= 10; qualityNr++) {
                        String selected = "";
                        String comment = "";
                        if (quality.equals(qualityNr)) {
                            selected = "selected=\"selected\"";
                        }
                        if (qualityNr == 1) {
                            comment = " (Worst Quality)";
                        }
                        if (qualityNr == 5) {
                            comment = " (Average Quality)";
                        }
                        if (qualityNr == 10) {
                            comment = " (Best Quality)";
                        }

                %> <%=selected%>

                %>
                <option value="<%=qualityNr%>" <%=selected%>><%=qualityNr%><%=comment%>
                </option>
                <%
                    }
                %>
            </select>
        </div>

        <div class="field">
            <label for="start_time_display_field">Start date/time:</label>
            <input id="start_time_display_field" type="text" readonly="readonly" name="<%=WebConstants.START_TIME_PARAM%>"
                   value="<%=start_date_S%>"/>
        </div>

        <div class="field">
            <label for="end_time_display_field">End date/time:</label>
            <input id="end_time_display_field" type="text" readonly="readonly" name="<%=WebConstants.END_TIME_PARAM%>"
                   value="<%=end_date_S%>"/>
        </div>


        <div class="field">
            <label for="channel_label">Channel:</label>
            <select id="channel_label" name="<%=WebConstants.CHANNEL_LABEL_PARAM%>" class="input">
                <%
                    for (String channel : WebConstants.lognames.keySet()) {
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

        <input type="hidden" name="<%=WebConstants.CAPTURE_FORMAT_PARAM%>" value="<%=format%>"/>
        <input type="hidden" name="<%=WebConstants.CONTROL_COMMAND_PARAM%>" value="<%=WebConstants.POSTPROCESS%>"/>
        <input type="hidden" name="<%=WebConstants.ENCODER_IP_PARAM%>" value="<%=encoderIP%>"/>
        <input type="hidden" name="<%=WebConstants.ENCODER_NAME_PARAM%>" value="<%=encoder_name%>"/>
        <input type="hidden" name="<%=WebConstants.IS_PROCESSED_PARAM%>" value="<%=request.getParameter(WebConstants.IS_PROCESSED_PARAM)%>"/>
    </fieldset>
    <input type="button" name="Reject" value="Reject" onclick="gotopage('<%=WebConstants.PLAYBACK_JSP%>')"/>
    <input type="submit" name="Process" value="Process"/>
</form>

<script type="text/javascript">

    var date_format = "<%=WebConstants.jscalendar_format_string%>";

    var start_calendar = Calendar.setup({
        ifFormat: date_format,
        inputField: "start_time_display_field",
        eventName: "click",
        //onUpdate       :    setCurrentStartDate,
        showsTime: true,            // will display a time selector
        singleClick: true,           // double-click mode
        step: 1,                // show all years in drop-down boxes (instead of every other year as default)
        firstDay: 1
    });


    var end_calendar = Calendar.setup({
        ifFormat: date_format,
        inputField: "end_time_display_field",
        eventName: "click",
        //onUpdate       :    setCurrentStartDate,
        showsTime: true,            // will display a time selector
        singleClick: true,           // double-click mode
        step: 1,                // show all years in drop-down boxes (instead of every other year as default)
        firstDay: 1
    });

</script>