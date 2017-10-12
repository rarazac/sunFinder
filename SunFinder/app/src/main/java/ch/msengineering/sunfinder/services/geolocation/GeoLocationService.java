package ch.msengineering.sunfinder.services.geolocation;

import java.io.IOException;

/**
 * Created by raphe on 10/10/2017.
 */

public interface GeoLocationService {

    void getCurrentLocation();
    void getGeoLocationByName(String locationName) throws IOException;

}
