package ch.msengineering.sunfinder.services.webcam;

/**
 * Created by raphe on 09/10/2017.
 */

public interface WebCamService {

    void getNearby(double latitude, double longitude, int radius);

}
