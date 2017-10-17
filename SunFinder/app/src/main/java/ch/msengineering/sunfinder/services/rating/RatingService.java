package ch.msengineering.sunfinder.services.rating;

/**
 * Created by razac on 14.10.17.
 */
// TODO implement timestamp functionality
public interface RatingService {

    void getRating(String id) throws  Exception;
    void setRating(String id, int ratingValue) throws Exception;

}
