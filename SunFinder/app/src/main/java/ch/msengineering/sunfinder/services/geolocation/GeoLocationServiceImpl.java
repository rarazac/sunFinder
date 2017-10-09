package ch.msengineering.sunfinder.services.geolocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.msengineering.sunfinder.services.geolocation.api.GeoLocation;

/**
 * Created by raphe on 10/10/2017.
 */

public class GeoLocationServiceImpl {

    public List<GeoLocation> getGeoLocationByName(Context context, String locationName) {
        if(Geocoder.isPresent()){
            try {
                Geocoder gc = new Geocoder(context);
                List<Address> addresses= gc.getFromLocationName(locationName, 10);

                List<GeoLocation> geoLocations = new ArrayList<>(addresses.size());
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        geoLocations.add(
                                new GeoLocation(
                                    a.getFeatureName(),
                                    a.getCountryName(),
                                    a.getCountryCode(),
                                    a.getLatitude(),
                                    a.getLongitude()));
                    }
                }
                return geoLocations;
            } catch (IOException e) {
                Log.e("SunFinder", "GeoLocationService: getGeoLocationByName -> Failure", e);
                return createDummyGeoLocation();
            }
        } else {
            Log.e("SunFinder", "GeoLocationService: getGeoLocationByName -> Failure: Device does not have a Geocoder");
            return createDummyGeoLocation();
        }
    }

    private List<GeoLocation> createDummyGeoLocation() {
        List<GeoLocation> geoLocations = new ArrayList<>();
        geoLocations.add(new GeoLocation("Zurich", "Switzerland", "ch", 47.3769, 8.5417));
        return geoLocations;
    }

}
