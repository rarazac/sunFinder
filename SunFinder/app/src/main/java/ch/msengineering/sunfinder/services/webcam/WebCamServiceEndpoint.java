package ch.msengineering.sunfinder.services.webcam;

import ch.msengineering.sunfinder.services.webcam.api.WebCamNearby;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by raphe on 09/10/2017.
 */

public interface WebCamServiceEndpoint {

    @Headers("X-Mashape-Key: E9HH1DLtG1mshXjkbHOWttJ7BXTYp1zSzxOjsnowvONkzPP54P")
    @GET("/webcams/list/nearby={lat},{lng},{radius}?lang=en&show=webcams:image,location,categories,continents,countries,regions,properties")
    Call<WebCamNearby> getNearby(@Path("lat") double latitude, @Path("lng") double longitude, @Path("radius") int radius);

}
