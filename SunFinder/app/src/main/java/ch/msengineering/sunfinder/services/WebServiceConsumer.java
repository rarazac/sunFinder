package ch.msengineering.sunfinder.services;

import ch.msengineering.sunfinder.services.webcam.api.WebCamNearby;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by raphe on 09/10/2017.
 */

public interface WebServiceConsumer {

    void onWebCamNearby(Response<WebCamNearby> response);

    void onFailure(Call<?> call, Throwable t);

}
