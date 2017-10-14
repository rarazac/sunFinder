package ch.msengineering.sunfinder.services.rating;

/**
 * Created by razac on 14.10.17.
 */

public interface RatingServiceInterface {

    void getRating(String id) throws  Exception;
    void setRating(String id) throws Exception;

}
