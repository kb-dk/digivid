<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="static dk.statsbiblioteket.deck.client.webinterface.WebConstants.*"%>
<%@ page pageEncoding="UTF-8"
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>

<script type="text/javascript">
    function play_video(url) {
        document.getElementById("video").innerHTML = getVideoObject(url);
        document.vlc.play();
        document.getElementById("video_controls").setAttribute("style", "visibility:visible");
    }

    function getVideoObject(url) {
       // return '<embed name="vlc" target="' + url + '" WIDTH="480" HEIGHT="376" AUTOPLAY="true" CONTROLLER="true" LOOP="false" type="application/x-mplayer2" scale="tofit" kioskmode="false">'+
       //        '</embed>'
       return '<embed name="vlc" target="' + url + '" WIDTH="480" HEIGHT="376" AUTOPLAY="true" CONTROLLER="true" LOOP="false" type="application/x-google-vlc-plugin" scale="tofit" kioskmode="false">'+
               '</embed>'
    }

</script>
</head>

<body>
<%
    String card_string = "?";
    try {
        int card_number = ((Integer) request.getAttribute(CARDS_ATTR)).intValue();
        if (card_number == 1) {
            card_string = "Source A";
        } else {
            card_string = "Source B"  ;
        }
    } catch (Exception e) {
        //Do nothing
    }
%>
Viewing live output of <%=card_string%>.
<div id="video"></div>
<div id="video_controls" style="visibility:collapse;">
<!--<input type="button" value="Play" onclick="document.vlc.play();" />
<input type="button" value="Pause" onclick="document.vlc.pause();" /> -->
<input type="button" value="Stop" onclick="document.vlc.stop();" />
<!--<input type="button" value="+1 min" onclick="document.vlc.seek(60, true);" />-->

</div>
<%
    String stream_url = (String) request.getAttribute("stream_url");
    Thread.sleep(1000);
%>
 <input type="button" onclick="play_video('<%=stream_url%>')" value="afspil"/>
</body>
</html>