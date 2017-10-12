package ch.msengineering.sunfinder.services.geolocation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.msengineering.sunfinder.services.LocalServiceConsumer;
import ch.msengineering.sunfinder.services.geolocation.api.GeoLocation;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by raphe on 10/10/2017.
 */

public class GeoLocationServiceImpl implements GeoLocationService  {

    private final Activity activity;
    private final LocalServiceConsumer localServiceConsumer;
    private final LocationManager locationManager;

    public GeoLocationServiceImpl(Activity activity, LocalServiceConsumer localServiceConsumer) {
        this.activity = activity;
        this.localServiceConsumer = localServiceConsumer;

        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
    }

    public void getCurrentLocation() {
        int refreshTimeMs = 1000 * 30;
        int refreshDistanceMeter = 1000;

        String bestProvider = locationManager.getBestProvider(new Criteria(), true);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1337);
        } else {
            locationManager.requestLocationUpdates(bestProvider, refreshTimeMs,
                    refreshDistanceMeter, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {
                        }

                        @Override
                        public void onProviderEnabled(String s) {
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                        }
                    });
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
        if (lastKnownLocation != null) {
            localServiceConsumer.onGeoLocation(new GeoLocation(bestProvider, "", "", lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
        } else {
            Log.w("SunFinder", "GeoLocationService: getCurrentLocation -> Warning: No last known location!");
            localServiceConsumer.onGeoLocation(createDummyGeoLocation());
        }
    }

    public void getGeoLocationByName(String locationName) throws IOException {
        if(Geocoder.isPresent()){
            Geocoder gc = new Geocoder(activity);
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
            localServiceConsumer.onGeoLocation(geoLocations);
        } else {
            Log.w("SunFinder", "GeoLocationService: getGeoLocationByName -> Warning: Device does not have a Geocoder!");
            localServiceConsumer.onGeoLocation(createDummyGeoLocation());
        }
    }

    private List<GeoLocation> createDummyGeoLocation() {
        List<GeoLocation> geoLocations = new ArrayList<>();
        geoLocations.add(new GeoLocation("Zurich", "Switzerland", "ch", 47.3769, 8.5417));
        return geoLocations;
    }

}
