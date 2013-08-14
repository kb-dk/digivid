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

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
    public static final String UNPROCESSED_FILE_REGEXP = "([0-9]+)_([^_]*)_([^_]*)_([^_]*)_([^_]*)\\.mpeg";

    // /bitarkiv/0105/files/dk4_807.250_K63-DK4_mpeg1_20070314045601_20070315045501.mpeg
    public static final String BART_FILE_REGEXP ="([^_]*)_([^_]*)_([^_]*)_([^_]*)_([0-9]{14})_([0-9]{14}).*\\.mpeg";
    public static final Pattern UNPROCESSED_FILE_PATTERN = Pattern.compile(UNPROCESSED_FILE_REGEXP);
    public static final Pattern BART_FILE_PATTERN = Pattern.compile(BART_FILE_REGEXP);

    private WebConstants(){}

    public static final String BART_DATE_FORMAT_S = "yyyyMMddHHmmss";
    public static final SimpleDateFormat BART_DATE_FORMAT = new SimpleDateFormat(BART_DATE_FORMAT_S);

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
    public static final String ENCODER_IP_PARAM = "encoderIP";
    public static final String CARD_NAME_PARAM = "cardName";
    public static final String INPUT_CHANNEL_ID_PARAM = "inputChannelID";
    public static final String STREAM_PROTOCOL_PARAM = "streamProtocol";
    public static final String STREAM_PORT_HTTP_PARAM = "streamPortHTTP";
    public static final String CONTROL_COMMAND_PARAM ="ctrlCommand";
    public static final String FILE_NAME_PARAM = "fileName";
    public static final String CHANNEL_LABEL_PARAM = "labelChannelID";
    public static final String START_TIME_PARAM = "startTime";
    public static final String END_TIME_PARAM = "endTime";
    public static final String CAPTURE_FORMAT_PARAM = "captureFormat";
    public static final String RECORDING_TIME_PARAM = "recordingTime";
    public static final String USER_NAME_PARAM = "digivid";
    public static final String FILE_LENGTH_PARAM = "filelength";
    public static final String IS_PROCESSED_PARAM = "is_processed";
    public static final String ENCODER_NAME_PARAM = "encoder_name";

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
        lognames.put("K11-DR1", "dr1");
        lognames.put("K9-DR2", "dr2");
        lognames.put("K10-TV2-Danmark", "tv2d");
        lognames.put("K67-TV2-Zulu", "tv2z");
        lognames.put("K40-TV2-Charlie", "tv2c");
        lognames.put("K53-TV2-Lorry", "tv2l");
        lognames.put("S30-TV2-Syd", "tv2s");
        lognames.put("TV2-Nord", "tv2n");
        lognames.put("TV2-Fyn", "tv2f");
        lognames.put("TV2-Midt-Vest", "tv2mv");
        lognames.put("TV2-Bornholm", "tv2b");
        lognames.put("K10-TV2-Østjylland", "tv2d");        
        lognames.put("K45-TV3", "tv3");
        lognames.put("K43-TV3P", "tv3p");
        lognames.put("S14-TV-Danmark", "tvdk");
        lognames.put("K54-Kanal5", "kanal5");
        lognames.put("K63-DK4", "dk4");
        lognames.put("TV-Sport", "tvsport");
    }
}
