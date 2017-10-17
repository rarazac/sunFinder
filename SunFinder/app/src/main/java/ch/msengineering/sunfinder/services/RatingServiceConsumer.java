package ch.msengineering.sunfinder.services;

import com.google.firebase.database.DatabaseError;

/**
 * Created by razac on 14.10.17.
 */

public interface RatingServiceConsumer {

    // called in RatingServiceImplementation setRating
    void onRatingSet(DatabaseError databaseError);

    // called in RatingServiceImplementation getRating
    void onRatingGet(String id, int ratingValue);

    // called in RatingServiceImplementation when something went wrong
    void onFailure(DatabaseError databaseError);
}
