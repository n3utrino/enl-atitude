package ch.n3utrino.enlatitude.common;

/**
 * Created with IntelliJ IDEA.
 * User: n3utrino
 * Date: 13.12.12
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
public class Location {

    private double lat;
    private double lon;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
