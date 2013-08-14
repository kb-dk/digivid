package dk.statsbiblioteket.deck.client;

import java.util.TimeZone;

    /**
     * A class that contains location information such as latitude and longitude
     * required for astromomical calculations. The elevation field is not used by
     * most calculation engines and would be ignored if set. Check the documentation
     * for specific implemenations
     * elevation is calculated as part o the algorithm.
     *
     * @author &copy; Eliyahu Hershfeld 2004 - 2005
     * @version 0.9
    */

public class GeoLocation {


            private double latitude;

            private double longitude;

            private String locationName;

            private TimeZone timeZone;

            private double elevation;

            /**
             * Method to get the elevation in Meters.
             *
             * @return Returns the elevation in Meters.
             */
            public double getElevation() {
                    return elevation;
            }

            /**
             * Method to set the elevation in Meters <b>above </b> sea level.
             *
             * @param elevation
             *            The elevation to set in Meters. An IllegalArgumentException
             *            will be thrown if the value is a negative.
             */
            public void setElevation(double elevation) {
                    if (elevation < 0) {
                            throw new IllegalArgumentException("Elevation can not be negative");

                    }
                    this.elevation = elevation;
            }

            /**
             * GeoLocation constructor with parameters for all required fields.
             *
             * @param name
             *            The location name for display use such as &quot;Lakewood,
             *            NJ&quot;
             * @param latitude
             *            the latitude in a double format such as 40.0828 for Lakewood,
             *            NJ
             * @param longitude
             *            double the longitude in a double format such as -74.2094 for
             *            Lakewood, NJ. <br/> <b>Note: </b> For longitudes east of the
             *            <a href="http://en.wikipedia.org/wiki/Prime_Meridian">Prime
             *            Meridian </a> (Greenwich), a negative value should be used.
             * @param timeZone
             *            the <code>TimeZone</code> for the location.
             */
            public GeoLocation(String name, double latitude, double longitude,
                            TimeZone timeZone) {
                    this(name, latitude, longitude, 0, timeZone);
                    this.setLocationName(name);
                    this.setLatitude(latitude);
                    this.setLongitude(longitude);
                    this.setTimeZone(timeZone);
            }

            /**
             * GeoLocation constructor with parameters for all required fields.
             *
             * @param name
             *            The location name for display use such as &quot;Lakewood,
             *            NJ&quot;
             * @param latitude
             *            the latitude in a double format such as 40.0828 for Lakewood,
             *            NJ
             * @param longitude
             *            double the longitude in a double format such as -74.2094 for
             *            Lakewood, NJ. <br/> <b>Note: </b> For longitudes east of the
             *            <a href="http://en.wikipedia.org/wiki/Prime_Meridian">Prime
             *            Meridian </a> (Greenwich), a negative value should be used.
             * @param elevation
             *            the elevation above sea level in Meters. Elevation is not used
             *            in most algorithms used for calculating sunrise and set.
             * @param timeZone
             *            the <code>TimeZone</code> for the location.
             */
            public GeoLocation(String name, double latitude, double longitude,
                            double elevation, TimeZone timeZone) {
                    setLocationName(name);
                    setLatitude(latitude);
                    setLongitude(longitude);
                    setElevation(elevation);
                    setTimeZone(timeZone);
            }

            /**
             * Default GeoLocation constructor will set location to the Prime Meridian
             * at Greenwich, England and a TimeZone of GMT. Longitude will be 0 and
             * latitide will be 51.4772 to match the location of the <a
             * href="http://www.rog.nmm.ac.uk">Royal Observatory, Greenwich </a>. No
             * daylight savings time will be used.
             */
            public GeoLocation() {
                    setLocationName("Greenwich, England");
                    setLongitude(0); // should not be needed
                    setLatitude(51.4772);
                    setTimeZone(TimeZone.getTimeZone("GMT"));
            }

            /**
             * Method to set the latitude in a double format.
             *
             * @param latitude
             *            The degrees of latitude to set in a double format between -90
             *            and 90. An IllegalArgumentException will be thrown if the
             *            value exceeds the limit. For example 40.0828 would be used for
             *            Lakewood, NJ.
             */
            public void setLatitude(double latitude) {
                    if (latitude > 90 || latitude < -90) {
                            throw new IllegalArgumentException(
                                            "Latitude must be between -90 and  90");
                    }
                    this.latitude = latitude;
            }

            /**
             * Method to set the latitude in degrees, minutes and seconds.
             *
             * @param degrees
             *            The degrees of latitude to set between -90 and 90. An
             *            IllegalArgumentException will be thrown if the value exceeds
             *            the limit. For example 40 would be used for Lakewood, NJ.
             * @param minutes
             * @param seconds
             * @param direction
            *            N for north and S for south. An IllegalArgumentException will
             *            be thrown if the value is not S or N.
             */
            public void setLatitude(int degrees, int minutes, double seconds,
                            String direction) {
                    double tempLat = degrees + ((minutes + (seconds / 60.0)) / 60.0);
                    if (tempLat > 90 || tempLat < 0) {
                            throw new IllegalArgumentException(
                                            "Latitude must be between 0 and  90. Use direction of S instead of negative.");
                    }
                    if (direction.equals("S")) {
                            tempLat *= -1;
                    } else if (!direction.equals("N")) {
                            throw new IllegalArgumentException(
                                            "Latitude direction must be N or S");
                    }
                    this.latitude = tempLat;
            }

            /**
             * @return Returns the latitude.
             */
            public double getLatitude() {
                    return latitude;
            }

           /**
             * Method to set the longitude in a double format.
             *
             * @param longitude
             *            The degrees of longitude to set in a double format between
             *            -180 and 180. An IllegalArgumentException will be thrown if
             *            the value exceeds the limit. For example -74.2094 would be
             *            used for Lakewood, NJ. Note: for longitudes east of the <a
             *            href="http://en.wikipedia.org/wiki/Prime_Meridian">Prime
             *            Meridian </a> (Greenwich) a negative value should be used.
             */
            public void setLongitude(double longitude) {
                    if (longitude > 180 || longitude < -180) {
                            throw new IllegalArgumentException(
                                            "Longitude must be between -180 and  180");
                    }
                    this.longitude = longitude;
            }

            /**
             * Method to set the longitude in degrees, minutes and seconds.
             *
             * @param degrees
             *            The degrees of longitude to set between -180 and 180. An
             *            IllegalArgumentException will be thrown if the value exceeds
             *            the limit. For example -74 would be used for Lakewood, NJ.
             *            Note: for longitudes east of the <a
             *            href="http://en.wikipedia.org/wiki/Prime_Meridian">Prime
             *            Meridian </a> (Greenwich) a negative value should be used.
             * @param minutes
             * @param seconds
             * @param direction
             *            E for east of the Prime Meridian or W for west of it. An
             *            IllegalArgumentException will be thrown if the value is not E
             *            or W.
             */
            public void setLongitude(int degrees, int minutes, double seconds,
                            String direction) {
                    double longTemp = degrees + ((minutes + (seconds / 60.0)) / 60.0);
                    if (longTemp > 180 || longitude < 0) {
                            throw new IllegalArgumentException(
                                            "Longitude must be between 0 and  180. Use the ");
                    }
                    if (direction.equals("W")) {
                            longTemp *= -1;
                    } else if (!direction.equals("E")) {
                            throw new IllegalArgumentException(
                                            "Longitude direction must be E or W");
                    }
                    this.longitude = longTemp;
            }

            /**
             * @return Returns the longitude.
             */
            public double getLongitude() {
                    return longitude;
            }

            /**
             * @return Returns the location name.
             */
            public String getLocationName() {
                    return locationName;
            }

            /**
             * @param name
             *            The setter method for the display name.
             */
            public void setLocationName(String name) {
                    this.locationName = name;
           }

            /**
             * @return Returns the timeZone.
             */
            public TimeZone getTimeZone() {
                    return timeZone;
           }

            /**
             * @param timeZone
             *            The timeZone to set.
             */
            public void setTimeZone(TimeZone timeZone) {
                   this.timeZone = timeZone;
            }

            /**
             * A method that returns an XML formatted <code>String</code> representing
             * the serialized <code>Object</code>. Very similar to the toString
             * method but the return value is in an xml format. The format currently
             * used (subject to change) is:
             *
             * <pre>
             *  &lt;GeoLocation&gt;
             *       &lt;LocationName&gt;Lakewood, NJ&lt;/LocationName&gt;
             *       &lt;Latitude&gt;40.0828�&lt;/Latitude&gt;
             *       &lt;Longitude&gt;-74.2094�&lt;/Longitude&gt;
             *       &lt;Elevation&gt;0 Meters&lt;/Elevation&gt;
             *       &lt;TimezoneName&gt;America/New_York&lt;/TimezoneName&gt;
             *       &lt;TimeZoneDisplayName&gt;Eastern Standard Time&lt;/TimeZoneDisplayName&gt;
             *       &lt;TimezoneGMTOffset&gt;-5&lt;/TimezoneGMTOffset&gt;
             *       &lt;TimezoneDSTOffset&gt;1&lt;/TimezoneDSTOffset&gt;
             *  &lt;/GeoLocation&gt;
             * </pre>
             *
             * @return The XML formatted <code>String</code>.
             */
            public String toXML() {
                    StringBuffer sb = new StringBuffer();
                    sb.append("<GeoLocation>\n");
                    sb.append("\t<LocationName>").append(getLocationName()).append(
                                    "</LocationName>\n");
                    sb.append("\t<Latitude>").append(getLatitude()).append("�").append(
                                    "</Latitude>\n");
                    sb.append("\t<Longitude>").append(getLongitude()).append("�").append(
                                    "</Longitude>\n");
                    sb.append("\t<Elevation>").append(getElevation()).append(" Meters")
                                    .append("</Elevation>\n");
                    sb.append("\t<TimezoneName>").append(getTimeZone().getID()).append(
                                    "</TimezoneName>\n");
                    sb.append("\t<TimeZoneDisplayName>").append(
                                    getTimeZone().getDisplayName()).append(
                                    "</TimeZoneDisplayName>\n");
                    sb.append("\t<TimezoneGMTOffset>").append(
                                    getTimeZone().getRawOffset() / 3600000).append(
                                    "</TimezoneGMTOffset>\n");
                    sb.append("\t<TimezoneDSTOffset>").append(
                                    getTimeZone().getDSTSavings() / 3600000).append(
                                    "</TimezoneDSTOffset>\n");
                    sb.append("</GeoLocation>");
                    return sb.toString();
            }

            /**
             * @see java.lang.Object#equals(Object)
             */
            public boolean equals(Object object) {
                    if (this == object)
                            return true;
                    if (!(object instanceof GeoLocation))
                            return false;
                    GeoLocation geo = (GeoLocation) object;
                    return Double.doubleToLongBits(latitude) == Double
                                    .doubleToLongBits(geo.latitude)
                                    && Double.doubleToLongBits(longitude) == Double
                                                    .doubleToLongBits(geo.longitude)
                                    && elevation == geo.elevation
                                    && (locationName == null ? geo.locationName == null
                                                    : locationName.equals(geo.locationName))
                                    && (timeZone == null ? geo.timeZone == null : timeZone
                                                    .equals(geo.timeZone));
            }

            /**
             * @see java.lang.Object#hashCode()
             */
            public int hashCode() {

                    int result = 17;
                    long latLong = Double.doubleToLongBits(latitude);
                    long lonLong = Double.doubleToLongBits(longitude);
                    long elevLong = Double.doubleToLongBits(elevation);
                    int latInt = (int) (latLong ^ (latLong >>> 32));
                    int lonInt = (int) (lonLong ^ (lonLong >>> 32));
                    int elevInt = (int) (elevLong ^ (elevLong >>> 32));
                    result = 37 * result + getClass().hashCode();
                    result += 37 * result + latInt;
                    result += 37 * result + lonInt;
                    result += 37 * result + elevInt;
                    result += 37 * result
                                    + (locationName == null ? 0 : locationName.hashCode());
                    result += 37 * result + (timeZone == null ? 0 : timeZone.hashCode());
                    return result;
            }

            /**
             * toString method
             *
             * @return String
             */
            public String toString() {
                    StringBuffer sb = new StringBuffer();
                    sb.append("\nLocation Name:\t\t\t").append(getLocationName());
                    sb.append("\nLatitude:\t\t\t").append(getLatitude()).append("degrees");
                    sb.append("\nLongitude:\t\t\t").append(getLongitude()).append("degrees");
                    sb.append("\nElevation:\t\t\t").append(getElevation())
                                    .append(" Meters");
                    sb.append("\nTimezone Name:\t\t\t").append(getTimeZone().getID());
                    sb.append("\nTimezone Display Name:\t\t").append(
                                    getTimeZone().getDisplayName());
                    sb.append("\nTimezone GMT Offset:\t\t").append(
                                    getTimeZone().getRawOffset() / 3600000);
                    sb.append("\nTimezone DST Offset:\t\t").append(
                                    getTimeZone().getDSTSavings() / 3600000);
                    return sb.toString();
            }
    }

