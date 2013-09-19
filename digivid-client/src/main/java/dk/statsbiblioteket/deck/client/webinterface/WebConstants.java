/* File:        $RCSfile: WebConstants.java,v $
 * Revision:    $Revision: 1.8 $
 * Author:      $Author: csr $
 * Date:        $Date: 2012/03/05 12:23:58 $
 *
 * Copyright Det Kongelige Bibliotek og Statsbiblioteket, Danmark
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package dk.statsbiblioteket.deck.client.webinterface;

import com.google.code.regexp.Pattern;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;


/**
 * csr forgot to comment this!
 *
 * @author csr
 * @since Feb 26, 2007
 */

public class WebConstants {
    /**
     * matching groups are
     * 1) start timestamp
     * 2) user
     * 3) user-specified component
     * 4) channel name
     * 5) mpeg1 | mpeg2
     */
    //1331882386000_bart_pmdkarate_K11-DR1_mpeg2
    public static final String UNPROCESSED_FILE_REGEXP = "(?<startTimestamp>[0-9]+)_(?<user>[^_]+)_(?<userComment>[^_]+)_(?<channelLabel>[^_]+)_(?<capturingFormat>[^_]+)\\.mpeg";

    //tvsport_digivid_TV-Sport_mpeg1_20120305104229_20120305104529_172.18.243.243
    public static final String BART_FILE_REGEXP ="(?<channelID>[^_]+)_(?<user>[^_]+)_(?<channelLabel>[^_]+)_(?<capturingFormat>[^_]+)_(?<startTime>[0-9]{14})_(?<endTime>[0-9]{14}).*\\.mpeg";
    public static final Pattern UNPROCESSED_FILE_PATTERN = Pattern.compile(UNPROCESSED_FILE_REGEXP);
    public static final Pattern BART_FILE_PATTERN = Pattern.compile(BART_FILE_REGEXP);
    //These two must match
    public static final String time_format_string = "yyyy/MM/dd HH:mm";
    public static final String jscalendar_format_string = "%Y/%m/%e %H:%M";
    public static final String BART_FILE_DATEFORMAT_STRING = "yyyyMMddHHmmss";


    public static final String COMMENTS_SUFFIX = ".comments";
    public static final String SORT_FIELD_PARAM = "sortField";
    public static final String SORT_ORDER_NAME = "sortOrderName";
    public static final String SORT_ORDER_MODIFIED = "sortOrderModified";

    private WebConstants(){}


    /**
     * Names of request attributes
     */
    public static final String PAGE_ATTR = "page";
    public static final String STREAM_URL_ATTR = "stream_url";
    public static final String CARDS_ATTR = "cards";
    public static final String FILE_LENGTH_ATTR = "file_length";

    /**
     * Names of request parameters
     */
    public static final String PAGE_PARAM = "page";
    public static final String ENCODER_NAME_PARAM = "encoderName";
    public static final String CARD_NAME_PARAM = "cardName";
    public static final String INPUT_CHANNEL_ID_PARAM = "inputChannelID";
    public static final String STREAM_PROTOCOL_PARAM = "streamProtocol";
    public static final String STREAM_PORT_HTTP_PARAM = "streamPortHTTP";
    public static final String CONTROL_COMMAND_PARAM ="ctrlCommand";
    public static final String FILE_NAME_PARAM = "fileName";
    public static final String CHANNEL_LABEL_PARAM = "labelChannelID";
    public static final String START_TIME_PARAM = "startTime";
    public static final String END_TIME_PARAM = "endTime";
    public static final String VHS_LABEL_PARAM = "vhsLabel";
    public static final String RECORDING_QUALITY = "recordingQuality";
    public static final String CAPTURE_FORMAT_PARAM = "captureFormat";
    public static final String RECORDING_TIME_PARAM = "recordingTime";
    public static final String USER_NAME_PARAM = "digivid";
    public static final String FILE_LENGTH_PARAM = "filelength";
    public static final String IS_PROCESSED_PARAM = "is_processed";

    /**
     * Names of Commands
     */
    public static final String START_PREVIEW = "startPreview";
    public static final String STOP_PREVIEW = "stopPreview";
    public static final String START_RECORDING = "startRecording";
    public static final String STOP_RECORDING = "stopRecording";
    public static final String START_POSTPROCESS = "startPostprocess";
    public static final String STOP_PLAYBACK = "stopPlayback";
    public static final String POSTPROCESS = "postprocess";

    /**
     * Names of jsp pages
     */
    public static final String INDEX_JSP = "/index.jsp";
    public static final String STATUS_JSP = "/jobstatus.jsp";
    public static final String PREVIEW_JSP = "/preview.jsp";
    public static final String RECORD_JSP = "/record.jsp";
    public static final String PLAY_JSP = "/play.jsp";
    public static final String PLAYBACK_JSP = "/showfiles.jsp";
    public static final String POSTPROCESS_JSP = "/postprocess.jsp";

    /**
     * Map from channel designations to log-names. We use a LinkedHashMap as this preserves
     * the insertion order when the results are displayed
     * DR1
     *DR2
     *TV2
     *TV2 Zulu
     *TV2 Charlie
     *TV2 Lorry
     *TV2 Syd
     *TV2 Nord
     *TV2 Fyn
     *TV2 Midt/Vest
     *TV2 Bornholm
     *TV2 Østjylland
     *TV3
     *TV-Danmark
     *Kanal 5
     *DK4
     */
   public static Map<String, String> lognames = new LinkedHashMap<String, String>();

    static {
        lognames.put("DR1", "dr1");
        lognames.put("DR2", "dr2");
        lognames.put("TV2-Danmark", "tv2d");
        lognames.put("TV2-Zulu", "tv2z");
        lognames.put("TV2-Charlie", "tv2c");
        lognames.put("TV2-Lorry", "tv2l");
        lognames.put("TV2-Syd", "tv2s");
        lognames.put("TV2-Nord", "tv2n");
        lognames.put("TV2-Fyn", "tv2f");
        lognames.put("TV2-Midt-Vest", "tv2mv");
        lognames.put("TV2-Bornholm", "tv2b");
        lognames.put("TV2-Østjylland", "tv2d");
        lognames.put("TV3", "tv3");
        lognames.put("TV3P", "tv3p");
        lognames.put("TV-Danmark", "tvdk");
        lognames.put("Kanal5", "kanal5");
        lognames.put("DK4", "dk4");
        lognames.put("TV-Sport", "tvsport");
    }

    public static SimpleDateFormat getPresentationDateFormat() {
        SimpleDateFormat temp = new SimpleDateFormat(time_format_string);
        temp.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
        return temp;
    }

    public static SimpleDateFormat getFilenameDateFormat() {
        SimpleDateFormat temp = new SimpleDateFormat(BART_FILE_DATEFORMAT_STRING);
        temp.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
        return temp;
    }
}
