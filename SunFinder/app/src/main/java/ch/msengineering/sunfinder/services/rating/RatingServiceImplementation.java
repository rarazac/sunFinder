package ch.msengineering.sunfinder.services.rating;

import ch.msengineering.sunfinder.services.RatingServiceConsumer;
import ch.msengineering.sunfinder.services.rating.api.Rating;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by razac on 14.10.17.
 */

// TODO implement timestamp functionality
public class RatingServiceImplementation implements RatingService {

    private final RatingServiceConsumer ratingServiceConsumer;
    private ArrayList<Rating> mRatings;
    private DatabaseReference mDatabase;

    public RatingServiceImplementation(RatingServiceConsumer ratingServiceConsumer) {
        this.ratingServiceConsumer = ratingServiceConsumer;
        this.mRatings = new ArrayList<>();

        // Read from the database for the first time and store all rating in mRatings
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mDatabase = database.getReference("webcams");
        // We use the addListerForSingleValueEvent because we do not need an actual listener here
        this.mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot webCamSnapshot: dataSnapshot.getChildren()) {
                    String id_remote = (String) webCamSnapshot.getKey();
                    Long lvalue = (Long) webCamSnapshot.getValue();
                    // we dont need Long cast to int
                    int value = lvalue.intValue();
                    // fill ratings into array of ratings
                    Rating rating = new Rating(id_remote,value);
                    mRatings.add(rating);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.v("sunFinder", "Failed to read value.", error.toException());
            }
        });
    }

    /* write to the firebase realtime database
     * the firebase database stucture is:
     * -webcam
     *   - id1:value1
     *   - id2:value2
     *   - id3:value3
     */
    public void setRating(String id, int ratingValue){
        // get the current database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // create a entry with the pair id:rating.
        // if this id already exists the rating_value is just updated
        mDatabase.child("webcams").child(id).setValue(ratingValue, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference mDatabase) {
                // give the error to the caller activity if databaseError == null all OK
                ratingServiceConsumer.onRatingSet(databaseError);
            }
        });
    }

    /* Read from the database, we use addListenerForSingleValueEvent because we do not
     * need an active listener. We are only interested in the data at the moment we call
     * getRating
     */
    public void getRating(final String id){
        // get the current database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("webcams");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // iterate through all children, means all id with rating_value in the db
                for (DataSnapshot webCamSnapshot: dataSnapshot.getChildren()) {
                    // get id and value
                    String id_remote = (String) webCamSnapshot.getKey();
                    Long lvalue = (Long) webCamSnapshot.getValue();
                    int value = lvalue.intValue();
                    // check if it is the rating we are interested in
                    if (id_remote.equals(id)) {
                        // create new rating which will we put in the list
                        Rating rating = new Rating(id_remote,value);
                        if(mRatings.contains(rating) == false){
                            mRatings.add(rating);
                        }
                        // callback to activity which called getRating()
                        ratingServiceConsumer.onRatingGet(rating.id,rating.value);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("sunFinder", "onCancelled", databaseError.toException());
                ratingServiceConsumer.onFailure(databaseError);
            }
        });
    }
}
