package ch.msengineering.sunfinder.services.rating;

import ch.msengineering.sunfinder.services.RatingServiceConsumer;
import android.app.Activity;

/**
 * Created by razac on 14.10.17.
 */


public class RatingServiceImplementation implements RatingServiceInterface {

    private final Activity activity;
    private final RatingServiceConsumer ratingServiceConsumer;

    public RatingServiceImplementation(Activity activity, RatingServiceConsumer ratingServiceConsumer) {
        this.activity = activity;
        this.ratingServiceConsumer = ratingServiceConsumer;
    }

    // gets the rating from webcam(id) from the firebase database
    public void getRating(String id){

    }

    // sets the rating of webcam(id) in the firebase database
    public void setRating(String id){

    }
}
