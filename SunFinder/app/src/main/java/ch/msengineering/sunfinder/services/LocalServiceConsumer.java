package ch.msengineering.sunfinder.services;

import android.location.Location;

import java.util.List;

import ch.msengineering.sunfinder.services.geolocation.api.GeoLocation;
import ch.msengineering.sunfinder.services.webcam.api.WebCamNearby;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by raphe on 09/10/2017.
 */

public interface LocalServiceConsumer {

    void onGeoLocation(GeoLocation geoLocation);
    void onGeoLocation(List<GeoLocation> geoLocations);

    void onFailure(String message, Throwable t);

}
