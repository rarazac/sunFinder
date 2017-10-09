package ch.msengineering.sunfinder.services.geolocation.api;

/**
 * Created by raphe on 10/10/2017.
 */

public class GeoLocation {

    private final String name;
    private final String countryName;
    private final String countryCode;
    private final double latitude;
    private final double longitude;

    public GeoLocation(String name, String countryName, String countryCode, double latitude, double longitude) {
        this.name = name;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
