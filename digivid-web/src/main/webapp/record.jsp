<%@ page import="static dk.statsbiblioteket.deck.client.webinterface.WebConstants.*"%>
<%@ page import="java.util.List" %>
<%@ page import="dk.statsbiblioteket.deck.client.GenericTask" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="dk.statsbiblioteket.deck.client.GenericCtrl" %>
<%@ page import="java.net.InetAddress" %>
<%
    String encoderIP = request.getParameter(ENCODER_IP_PARAM);
    String encoder_name = encoderIP;
    if (request.getParameter(ENCODER_NAME_PARAM) != null) {
        encoder_name = request.getParameter(ENCODER_NAME_PARAM);
        encoderIP = InetAddress.getByName(encoder_name).getHostAddress();
    }
    //The cards actually available as determined
    //by querying the encoder
    Pattern pattern = Pattern.compile(".*/dev/video([0-9]).*");
    GenericCtrl task = new GenericCtrl(encoderIP, "ps -C cat -o args");
    List<String> jobs = (List<String>) task.execute();
    Set<Integer> used_cards = new HashSet<Integer>();
    for (String job : jobs) {
        Matcher m = pattern.matcher(job);
        boolean matchb = m.matches();
        if (matchb) {
            used_cards.add(Integer.parseInt(m.group(1)));
        }
    }
    Set<Integer> available_cards_s = new HashSet<Integer>();
    int[] all_cards = new int[]{0, 2};
    for (int card : all_cards) {
        if (!used_cards.contains(new Integer(card))) {
            available_cards_s.add(new Integer(card));
        }
    }
    Integer[] available_cards = (Integer[]) available_cards_s.toArray(new Integer[]{});
    Arrays.sort(available_cards);
    String[] source_names = new String[available_cards.length];
    for (int i = 0; i < available_cards.length; i++) {
        int card = available_cards[i].intValue();
        if (card == 0) source_names[i] = "Source A";
        if (card == 2) source_names[i] = "Source B";
    }
%>
Recording on <%=encoder_name%>
<form action="Control" method="post">
    <%
        if (available_cards.length == 0) {
    %>
    All cards currently recording
    <%
        return;
    } else if (available_cards.length == 1) {
    %>
    <input type="hidden" name="<%=CARD_NAME_PARAM%>" value="<%=available_cards[0].intValue()%>"/>
    Record on (<%=source_names[0]%><br/>
    <%
    } else {
        for (int i = 0; i<available_cards.length; i++) {
            Integer card_number_I = available_cards[i];
            int card_number = card_number_I.intValue();
    %>
   <%=source_names[i]%><input type="radio" name="<%=CARD_NAME_PARAM%>" value="<%=card_number%>"/><br/>
    <%
            }
        }
    %>
    <input type="hidden" name="<%=INPUT_CHANNEL_ID_PARAM%>" value="SB-Tape1"/>
    <input type="hidden" name="<%=ENCODER_IP_PARAM%>" value="<%=encoderIP%>"/>
    <input type="hidden" name="<%=ENCODER_NAME_PARAM%>" value="<%=encoder_name%>"/>
    Additional filename component: <input type="text" name="<%=FILE_NAME_PARAM%>"><br/>
    Original channel: <select name="<%=CHANNEL_LABEL_PARAM%>">
    <option value="K11-DR1">DR1</option>
    <option value="K9-DR2">DR2</option>
    <option value="K10-TV2-Danmark">TV2-Danmark</option>
    <option value="K67-TV2-Zulu">TV2-Zulu</option>
    <option value="K40-TV2-Charlie">TV2-Charlie</option>
    <option value="S14-TV-Danmark">TV-Danmark</option>
    <option value="K54-Kanal5">Kanal5</option>
    <option value="K63-DK4">DK4</option>
    <option value="K67-TV2-Zulu">TV2-Zulu</option>
    <option value="TV-Sport">TV-Sport</option>
</select><br/>
    Original Broadcast Start Date-Time:<span class="dummy_input" id="start_time_display_field">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><br/>

    <input type="hidden" id="start_time_field" name="<%=START_TIME_PARAM%>"/>


    MPEG-1:<input type="radio" name="<%=CAPTURE_FORMAT_PARAM%>" value="1" checked="checked"/>
    MPEG-2:<input type="radio" name="<%=CAPTURE_FORMAT_PARAM%>" value="2" /><br/>
    Recording Time (minutes): <input size="5" name="<%=RECORDING_TIME_PARAM%>" value="60" /><br/>
    <span id="file_size"></span>
    <input type="hidden" name="<%=CONTROL_COMMAND_PARAM%>" value="<%=START_RECORDING%>"/>
    <input type="hidden" name="<%=USER_NAME_PARAM%>" value="digivid" />
    <input type="submit" name="start" value="start" />
</form>

<script type="text/javascript">
    Calendar.setup({
        inputField     :    "start_time_field",      // id of the input field
                ifFormat       :    "%s000",       // format of the input field
                daFormat       :    "%Y %b %e %H:%M",
                displayArea    : "start_time_display_field",
                showsTime      :    true,            // will display a time selector
                singleClick    :    true,           // double-click mode
                step           :    1,                // show all years in drop-down boxes (instead of every other year as default)
                firstDay       :    1
            });
</script>