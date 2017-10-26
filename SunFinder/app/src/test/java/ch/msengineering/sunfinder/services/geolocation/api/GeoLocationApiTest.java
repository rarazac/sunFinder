package ch.msengineering.sunfinder.services.geolocation.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by raphe on 26/10/2017.
 */

public class GeoLocationApiTest {

    @Test
    public void testCreationOfApiDoesNotThrow() {
        createGeoLocation();
    }

    @Test
    public void testGettersSuccess() {
        GeoLocation geoLocation = createGeoLocation();
        assertEquals("name", geoLocation.getName());
        assertEquals("countryName", geoLocation.getCountryName());
        assertEquals("countryCode", geoLocation.getCountryCode());
        assertEquals(5, geoLocation.getLatitude(), 0.1);
        assertEquals(7, geoLocation.getLongitude(), 0.1);
    }

    private GeoLocation createGeoLocation() {
        return new GeoLocation("name", "countryName", "countryCode", 5, 7);
    }

}
