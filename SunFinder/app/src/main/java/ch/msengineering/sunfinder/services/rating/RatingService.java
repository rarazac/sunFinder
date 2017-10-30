package ch.msengineering.sunfinder.services.rating;

/**
 * Created by razac on 14.10.17.
 */
public interface RatingService {

    void getRating(String id);

    void setRating(String id, int ratingValue, int ts);

}
