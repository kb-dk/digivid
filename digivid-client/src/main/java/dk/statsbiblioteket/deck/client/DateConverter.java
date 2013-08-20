package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.exception.BasicException;

import java.io.PrintStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: stephan
 * Date: Dec 28, 2005
 * Time: 12:33:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateConverter extends GregorianCalendar {

    private static PrintStream out = System.out;
    private static boolean debug = false;
    private static final long MINUTE_MILLIS = 60 * 1000;
    private static GregorianCalendar cal = new GregorianCalendar();
    private static GeoLocation geoLocation;

    private static String month;
    private static String day;
    private static String year;
    private static String hour;
    private static String hour24;
    private static String minute;
    private static String second;
    private static String millisecond;

    private static String datestring;
    private static String timestring;
    private static String datetimestring;

    private static Date date;

    private static int arg_year;
    private static int arg_month;
    private static int arg_day;
    private static int arg_minute;
    private static int arg_second;
    private static int arg_millisecond;
    private static int arg_hour;
    private static int arg_hour24;
    private static int arg_hour12;
    private static int arg_amIs0OrPmIs1;


    /**
     * together with a regular expression a string is parsed and converted into
     * a object of type long
     *
     * @param datestring
     * @param regExpression
     * @return timestamp
     * @throws BasicException

     */
    public static long parseDateAsStringIntoMilliseconds(String datestring, String regExpression)
            throws BasicException {

        Pattern dateMatcher = Pattern.compile(regExpression);
        Matcher m = dateMatcher.matcher(datestring);

            if (m.matches()) {
                arg_year = Integer.parseInt(m.group(1));
                arg_month = Integer.parseInt(m.group(2));
                arg_day = Integer.parseInt(m.group(3));

                arg_hour = Integer.parseInt(m.group(4));
                arg_minute = Integer.parseInt(m.group(5));
                arg_second = Integer.parseInt(m.group(6));
                arg_millisecond = Integer.parseInt(m.group(7));

                //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                cal.clear();
                cal.set(arg_year, arg_month - 1, arg_day,arg_hour,arg_minute,arg_second);
                long millis = cal.getTimeInMillis();

                if (debug) {
                    cal.setTimeInMillis(millis);
                    arg_year = cal.get(Calendar.YEAR);
                    arg_month = cal.get(Calendar.MONTH) + 1;
                    arg_day = cal.get(Calendar.DAY_OF_MONTH);

                    out.println("Year " + arg_year +
                            " Month  / " + arg_month +
                            " Day / " + arg_day +
                            " Hour : " + arg_hour +
                            " Minute : " + arg_minute +
                            " Second : " + arg_second);
                }
                return millis;
            } else {
                throw new BasicException("Pattern " + regExpression +
                        " does not recognizes: " + datestring + " example: " +
                        "([0-9]{4})/([0-9]{2})/([0-9]{2})_([0-9]{2}):([0-9]{2}):([0-9]{2}).([0-9]{1}) reads [ yyyy/MM/dd_HH:MM:SS.S ]");
            }
    }

    /**
     * together with a regular expression a string is parsed and converted into 
     * a object of type long
     *
     * @param timestring
     * @param regExpression
     * @return timestamp
     * @throws BasicException
     */
    public static long parseTimeAsStringIntoMilliseconds(String timestring, String regExpression)
            throws BasicException {

        Pattern dateMatcher = Pattern.compile(regExpression);
        Matcher m = dateMatcher.matcher(timestring);
        if (m.matches()) {
            arg_hour = Integer.parseInt(m.group(1));
            arg_minute = Integer.parseInt(m.group(2));
            arg_second = Integer.parseInt(m.group(3));
            //arg_millisecond = Integer.parseInt(m.group(4));

            out.println(    " Hour : " + arg_hour +
                            " Minute : " + arg_minute +
                            " Second : " + arg_second);

            Time time = new Time(arg_hour,arg_minute,arg_second);
            long millis = time.getTime();
            System.out.println("time:  " + calculateTimestringFromSeconds(millis));


            return millis;

        } else {
            throw new BasicException("Pattern " + regExpression +
                    " does not recognizes: " + timestring + " example: " +
                    "([0-9]{2}):([0-9]{2}):([0-9]{2}).([0-9]{1}) reads [ HH:MM:SS.S ]"
            );
        }
    }

    /**
     *
     * @param dateStart
     * @param dateStop
     * @param regExpress
     * @return difference
     */
   public static long ElapsedMillisecondsFromDates (String dateStart, String dateStop, String regExpress) {
        GregorianCalendar gc1 =new GregorianCalendar(1970, 1, 1, 0, 0, 0);
        GregorianCalendar gc2=new GregorianCalendar(1970, 1, 1, 0, 0, 0);

       Pattern dateMatcher = Pattern.compile(regExpress);
        Matcher ms = dateMatcher.matcher(dateStart);
        if (ms.matches()) {
            arg_year = Integer.parseInt(ms.group(1));
            arg_month = Integer.parseInt(ms.group(2));
            arg_day = Integer.parseInt(ms.group(3));

            arg_hour = Integer.parseInt(ms.group(4));
            arg_minute = Integer.parseInt(ms.group(5));
            arg_second = Integer.parseInt(ms.group(6));
            arg_millisecond = Integer.parseInt(ms.group(7));
            gc1 = new GregorianCalendar(arg_year, arg_month, arg_day, arg_hour, arg_minute, arg_second);
        }
        Matcher me = dateMatcher.matcher(dateStop);
        if (me.matches()) {
            arg_year = Integer.parseInt(me.group(1));
            arg_month = Integer.parseInt(me.group(2));
            arg_day = Integer.parseInt(me.group(3));

            arg_hour = Integer.parseInt(me.group(4));
            arg_minute = Integer.parseInt(me.group(5));
            arg_second = Integer.parseInt(me.group(6));
            arg_millisecond = Integer.parseInt(me.group(7));
            gc2 = new GregorianCalendar(arg_year, arg_month, arg_day, arg_hour, arg_minute, arg_second);
        }

      // the above two dates are one second apart
       Date d1 = gc1.getTime();
       Date d2 = gc2.getTime();
       long l1 = d1.getTime();
       long l2 = d2.getTime();
       long difference = l2 - l1;
       System.out.println("Elapsed milliseconds: " + difference);
       return difference;
   }

     public static String calculateTimestringFromSeconds(long timeInSeconds) {
          String timeformat;
          long hours, minutes, seconds;
          hours = timeInSeconds / 3600;
          timeInSeconds = timeInSeconds - (hours * 3600);
          minutes = timeInSeconds / 60;
          timeInSeconds = timeInSeconds - (minutes * 60);
          seconds = timeInSeconds;
          System.out.println(hours + " hour(s) " + minutes + " minute(s) " + seconds + " second(s)");
          timeformat = hours + ":" + minutes + ":" + seconds;
          return timeformat;
   }

    /**
     * Together with a regular expression a string is parsed and converted into
     * a object of type date
     *
     * @param datetime
     * @param regExpression
     * @return
     * @throws BasicException
     */
    public static Date parseDatetimeStringIntoDate(String datetime,String regExpression)
            throws BasicException {

        Pattern dateMatcher = Pattern.compile(regExpression);
        Matcher m = dateMatcher.matcher(datetime);
        if (m.matches()) {

            int arg_year = Integer.parseInt(m.group(1));
            int arg_month = Integer.parseInt(m.group(2));
            int arg_day = Integer.parseInt(m.group(3));
            int arg_hour = Integer.parseInt(m.group(4));
            int arg_minute = Integer.parseInt(m.group(5));
            int arg_second = Integer.parseInt(m.group(6));
            //int arg_millisecond = Integer.parseInt(m.group(7));

            //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            cal.clear();
            cal.set(arg_year, arg_month - 1, arg_day, arg_hour, arg_minute, arg_second);
            //System.out.println("blaaaaa" + cal.getTimeInMillis());
            //java.text.SimpleDateFormat simpleDateFormat =null;
            date = cal.getTime();
            out.println("output Date: " + date);
            return date;

        } else {
            throw new BasicException("Pattern " + regExpression +
                    " does not recognizes: " + datetime + " example: " +
                    "([0-9]{4})/([0-9]{2})/([0-9]{2})_([0-9]{2}):([0-9]{2}):([0-9]{2}).([0-9]{1}) reads [ yyyy/MM/dd_HH:MM:SS.S ]"
            );
        }

    }

    /**
     * takes a Timestamp input and converts it into milliseconds,
     * also checks if the timestampo pattern is satisfied
     *
     * @param tStamp
     * @return  milliseconds
     * @throws BasicException
     */

    public static long convertTimestampIntoMilliseconds(Timestamp tStamp)
            throws BasicException {

        Pattern dateMatcher = Pattern.compile("([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2}).([0-9]{1})");
        Matcher m = dateMatcher.matcher(tStamp.toString());
        if (m.matches()) {
            int arg_year = Integer.parseInt(m.group(1));
            int arg_month = Integer.parseInt(m.group(2));
            int arg_day = Integer.parseInt(m.group(3));
            int arg_hour = Integer.parseInt(m.group(4));
            int arg_minute = Integer.parseInt(m.group(5));
            int arg_second = Integer.parseInt(m.group(6));
            int arg_millisecond = Integer.parseInt(m.group(7));

            //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            cal.clear();
            cal.set(arg_year, arg_month - 1, arg_day, arg_hour, arg_minute, arg_second);
            long millis = cal.getTimeInMillis();

            return millis;

        } else {
            throw new BasicException("Date format must be yyyy/MM/dd:HH:MM:SS.S, not " + tStamp);
        }
    }

    /**
     * takes a String input of a timestamp syntax and parses it into milliseconds
     *
     * @param tStamp
     * @return  milliseconds
     * @throws BasicException
     */
    public static long parseTimestampStringIntoMillis(String tStamp)
            throws BasicException {

        Pattern dateMatcher = Pattern.compile("([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2}).([0-9]{1})");
        Matcher m = dateMatcher.matcher(tStamp.toString());
        if (m.matches()) {
            arg_year = Integer.parseInt(m.group(1));
            arg_month = Integer.parseInt(m.group(2));
            arg_day = Integer.parseInt(m.group(3));
            arg_hour = Integer.parseInt(m.group(4));
            arg_minute = Integer.parseInt(m.group(5));
            arg_second = Integer.parseInt(m.group(6));
            arg_millisecond = Integer.parseInt(m.group(7));

            //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            cal.clear();
            cal.set(arg_year, arg_month - 1, arg_day, arg_hour, arg_minute, arg_second);
            long millis = cal.getTimeInMillis();
            return millis;

        } else {
            throw new BasicException("Date format must be yyyy/MM/dd:HH:MM:SS.S, not " + tStamp);
        }
    }

    public static long convertDatetimeIntoMilliseconds(Date date) {
        cal.setGregorianChange(date);
        return cal.getTimeInMillis();
    }

    public static Date convertMillisecondsIntoDate(long millis) {
        cal.setTimeInMillis(millis);
        arg_year = cal.get(Calendar.YEAR);
        arg_month = cal.get(Calendar.MONTH + 1);
        arg_day = cal.get(Calendar.DAY_OF_MONTH);
        arg_minute = cal.get(Calendar.MINUTE);
        arg_second = cal.get(Calendar.SECOND);
        arg_hour24 = cal.get(Calendar.HOUR_OF_DAY);
        arg_hour12 = cal.get(Calendar.HOUR);
        arg_amIs0OrPmIs1 = cal.get(Calendar.AM_PM);
        date = cal.getTime();
        out.println(arg_year + "/" + arg_month + "/" + arg_day + " " + arg_hour24 + ":" + arg_minute + ":" + arg_second);
        return date;
    }

    /**
     * String output as Date expression from milliseconds
     *
     * @param millis
     * @return datestring
     */
    public static String printMillisecondsIntoDate(long millis) {
        cal.setTimeInMillis(millis);
        if (cal.get(Calendar.YEAR) < 10) {
            year = "0" + cal.get(Calendar.YEAR);
        } else {
            year = String.valueOf(cal.get(Calendar.YEAR));
        }

        if (cal.get(Calendar.MONTH + 1) < 10) {
            month = "0" + cal.get(Calendar.MONTH + 1);
        } else {
            month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        }

        if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + cal.get(Calendar.DAY_OF_MONTH);
        } else {
            day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        }
        datestring = year + "/"
                + month + "/"
                + day;
        return datestring;
    }

    /**
     * String output as Date Time expression from milliseconds
     *
     * @param millis
     * @return datetimestring
     */
    public static String printMillisecondsIntoDatetime(long millis) {
        cal.setTimeInMillis(millis);
        arg_year = cal.get(Calendar.YEAR);
        arg_month = cal.get(Calendar.MONTH) + 1;
        arg_day = cal.get(Calendar.DAY_OF_MONTH);
        arg_minute = cal.get(Calendar.MINUTE);
        arg_second = cal.get(Calendar.SECOND);
        arg_hour24 = cal.get(Calendar.HOUR_OF_DAY);
        arg_hour12 = cal.get(Calendar.HOUR);
        arg_amIs0OrPmIs1 = cal.get(Calendar.AM_PM);
        year = String.valueOf(arg_year);
        month = String.valueOf(arg_month);
        day = String.valueOf(arg_day);
        hour24 = String.valueOf(arg_hour24);
        minute = String.valueOf(arg_minute);
        second = String.valueOf(arg_second);

        // if (cal.get( Calendar.YEAR )<10) { year = "0"+ year; }
        if (arg_month < 10) {
            month = "0" + month;
        }
        if (arg_day < 10) {
            day = "0" + day;
        }

        // if (cal.get( Calendar.MINUTE )<10) { String minute = "0"+cal.get( Calendar.MINUTE ); }
        // else { String minute = MiscDB.castIntToString(+cal.get( Calendar.MINUTE ));}

        // if (cal.get( Calendar.HOUR_OF_DAY )<10) { String hour24 = "0"+cal.get( Calendar.HOUR_OF_DAY ); }
        // else { String hour24 = MiscDB.castIntToString(cal.get( Calendar.HOUR_OF_DAY ));}

        // if (cal.get( Calendar.SECOND )<10) { String second =  "0" + cal.get( Calendar.SECOND );
        // } else { String hour24 = MiscDB.castIntToString(cal.get( Calendar.SECOND ));}
        if (second.length() > 2) {
            second = String.valueOf(second);
            System.out.println("Second " + second);
            second = truncate(second, 2);
        } else if (second.length() < 2) {
            second = "0" + second;
        } else {
            second = String.valueOf(second);
        }

        datetimestring = year + "/"
                + month + "/"
                + day + " "
                + hour24 + ":"
                + minute + ":"
                + second + ".0";
        return datetimestring;
    }

    public static String printMillisecondsIntoSerialnumber(long millis) {
        cal.setTimeInMillis(millis);
        arg_year = cal.get(Calendar.YEAR);
        arg_month = cal.get(Calendar.MONTH) + 1;
        arg_day = cal.get(Calendar.DAY_OF_MONTH);
        arg_minute = cal.get(Calendar.MINUTE);
        arg_second = cal.get(Calendar.SECOND);
        arg_millisecond = cal.get(Calendar.MILLISECOND);
        arg_hour24 = cal.get(Calendar.HOUR_OF_DAY);
        arg_hour12 = cal.get(Calendar.HOUR);
        arg_amIs0OrPmIs1 = cal.get(Calendar.AM_PM);
        year = String.valueOf(arg_year);
        month = String.valueOf(arg_month);
        day = String.valueOf(arg_day);
        hour24 = String.valueOf(arg_hour24);
        minute = String.valueOf(arg_minute);
        second = String.valueOf(arg_second);
        millisecond = String.valueOf(arg_millisecond);
        // if (cal.get( Calendar.YEAR )<10) { year = "0"+ year; }

        if (arg_month < 10) {
            month = "0" + month;
        }
        if (arg_day < 10) {
            day = "0" + day;
        }

        // if (cal.get( Calendar.MINUTE )<10) { String minute = "0"+cal.get( Calendar.MINUTE ); }
        // else { String minute = MiscDB.castIntToString(+cal.get( Calendar.MINUTE ));}

        // if (cal.get( Calendar.HOUR_OF_DAY )<10) { String hour24 = "0"+cal.get( Calendar.HOUR_OF_DAY ); }
        // else { String hour24 = MiscDB.castIntToString(cal.get( Calendar.HOUR_OF_DAY ));}

        // if (cal.get( Calendar.SECOND )<10) { String second =  "0" + cal.get( Calendar.SECOND );
        // } else { String hour24 = MiscDB.castIntToString(cal.get( Calendar.SECOND ));}
        if (hour24.length() > 2) {
            hour24 = String.valueOf(hour24);
            System.out.println("Hour 24 " + hour24);
            hour24 = truncate(hour24, 2);
        } else if (hour24.length() < 2) {
            hour24 = "0" + hour24;
        } else {
            hour24 = String.valueOf(arg_hour24);
        }

        if (minute.length() > 2) {
            minute = String.valueOf(second);
            System.out.println("Minute " + minute);
            minute = truncate(minute, 2);
        } else if (minute.length() < 2) {
            minute = "0" + minute;
        } else {
            minute = String.valueOf(arg_minute);
        }

        if (second.length() > 2) {
            second = String.valueOf(second);
            System.out.println("Second " + second);
            second = truncate(second, 2);
        } else if (second.length() < 2) {
            second = "0" + second;
        } else {
            second = String.valueOf(arg_second);
        }

        String serial = year +"_"+ month +"_"+ day +"_"+ hour24 +"_"+ minute +"_"+ second +"_"+ millisecond;
        return serial;
    }


    static final public String truncate(final String target, final int maxSize) {
        return (target.length() > maxSize ? target.substring(0, maxSize) : target);
    }

    /**
     * Milliseconds into Timestamp
     * @param millis
     * @return  timestamp
     */
    public static Timestamp convertMillisecondsIntoTimestamp(long millis) {
        Timestamp timestamp = null;
        timestamp.setTime(millis);
        return timestamp;
    }

    /**
     * Milliseconds into a time syntaxed object of type String
     *
     * @param millis
     * @return timestring
     */
    public static String printMillisecondsIntoTime(long millis) {

        cal.setTimeInMillis(millis);

        if (String.valueOf(cal.get(Calendar.SECOND)).length() > 2) {
            second = String.valueOf(cal.get(Calendar.SECOND));
            out.println("Second " + second);
            second = truncate(second, 2);
        } else {
            second = String.valueOf(cal.get(Calendar.SECOND));
        }

        timestring = cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE) + ":"
                + second + ".0";
        return timestring;
    }

    /**
     * A method that returns a <code>Date</code> from the time passed in
     *
     * @param time The time to be set as the time for the <code>Date</code>.
     *             The time expected is in the format: 18.75 for 6:45:00 PM
     * @return The Date.
     */
    private Date convertDateIntoTime(double time) {

        if (Double.isNaN(time)) {
            return null;
        }
        cal.clear();
        cal.set(Calendar.YEAR, get(Calendar.YEAR));
        cal.set(Calendar.MONTH, get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH));
        if ((!getTimeZone().inDaylightTime(getTime()) && time < 0) || (getTimeZone().inDaylightTime(getTime()) && time < 1)) { // sunset time (usually offset) is later than midnight GMT
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        arg_hour = (int) time; // cut off minutes
        time -= arg_hour;
        arg_minute = (int) (time *= 60);
        time -= arg_minute;
        arg_second = (int) (time *= 60);
        time -= arg_second; // milliseconds

        cal.set(Calendar.HOUR_OF_DAY, arg_hour);
        cal.set(Calendar.MINUTE, arg_minute);
        cal.set(Calendar.SECOND, arg_second);
        cal.set(Calendar.MILLISECOND, (int) (time * 1000));

        return cal.getTime();
    }


    /**
     * @param geoLocation The geoLocation to set.
     */
    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
        this.setTimeZone(geoLocation.getTimeZone());// TODO might not be needed
    }

    /**
     * A method that adds time zone offset and daylight savings time to the raw
     * UTC time.
     *
     * @param time The UTC time to be adjusted.
     * @return The time adjusted for the time zone offset and dailight savings
     *         time.
     */
    private double getOffsetTime(double time) {
        boolean dst = getTimeZone().inDaylightTime(getTime());
        double dstOffset = 0;
        // be nice to newfies and use a double
        double gmtOffset = getTimeZone().getRawOffset() / (60 * MINUTE_MILLIS);
        if (dst) {
            dstOffset = getTimeZone().getDSTSavings() / (60 * MINUTE_MILLIS);
        }
        return time + gmtOffset + dstOffset;
    }

    public static String printCurrentMillisecondsIntoDateTime() {
        new DateConverter();
        millisecond = printMillisecondsIntoDatetime(System.currentTimeMillis());
        out.println(millisecond);
        return millisecond;
    }
}
