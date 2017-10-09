package ch.msengineering.sunfinder.services.webcam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.msengineering.sunfinder.services.ServiceConsumer;
import ch.msengineering.sunfinder.services.webcam.api.WebCamNearby;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by raphe on 09/10/2017.
 */

public class WebCamServiceImpl implements WebCamService, Callback<WebCamNearby> {

    private final WebCamServiceEndpoint webCamServiceEndpoint;
    private final ServiceConsumer serviceConsumer;

    public WebCamServiceImpl(ServiceConsumer serviceConsumer) {
        this.serviceConsumer = serviceConsumer;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebCamServiceEndpoint.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        webCamServiceEndpoint = retrofit.create(WebCamServiceEndpoint.class);
    }

    public void getNearby(double latitude, double longitude, int radius) {
        Call<WebCamNearby> call = webCamServiceEndpoint.getNearby(latitude, longitude, radius);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<WebCamNearby> call, Response<WebCamNearby> response) {
        this.serviceConsumer.onWebCamNearby(response);
    }

    @Override
    public void onFailure(Call<WebCamNearby> call, Throwable t) {
        this.serviceConsumer.onFailure(call, t);
    }
}
