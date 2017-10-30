package ch.msengineering.sunfinder.services;

import com.google.firebase.database.DatabaseError;

import ch.msengineering.sunfinder.services.rating.api.Rating;

/**
 * Created by razac on 14.10.17.
 */

public interface RatingServiceConsumer {

    // called in RatingServiceImplementation setRating
    void onRatingSet(DatabaseError databaseError);

    // called in RatingServiceImplementation getRating
    void onRatingGet(Rating rating);

    // called in RatingServiceImplementation when something went wrong
    void onFailure(Exception e);
}
