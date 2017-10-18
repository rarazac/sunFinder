package ch.msengineering.sunfinder.services.rating;

import ch.msengineering.sunfinder.services.RatingServiceConsumer;
import ch.msengineering.sunfinder.services.rating.api.Rating;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by razac on 14.10.17.
 */

public class RatingServiceImplementation implements RatingService {

    private final RatingServiceConsumer ratingServiceConsumer;
    private DatabaseReference mDatabase;

    public RatingServiceImplementation(RatingServiceConsumer ratingServiceConsumer) {
        this.ratingServiceConsumer = ratingServiceConsumer;
    }

    /* write to the firebase realtime database
     * the firebase database stucture is:
     * -webcams
     *      - id1
     *          - ratingValue:value1
     *          - timeStamp: ts_UTC
     *      - id2
     *          - ratingValue:value2
     *          - timeStamp: ts_UTC
     *      - id3
     *          - ratingValue:value3
     *          - timeStamp: ts_UTC
     */
    public void setRating(String id, int ratingValue){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // set ratingValue
        mDatabase.child("webcams").child(id).child("ratingValue").setValue(ratingValue, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference mDatabase) {
                // give the error to the caller activity if databaseError == null all OK
                ratingServiceConsumer.onRatingSet(databaseError);
            }
        });

        // set timeStamp
        Long tsLong = System.currentTimeMillis()/1000;
        int ts = tsLong.intValue();
        mDatabase.child("webcams").child(id).child("timeStamp").setValue(ts, new DatabaseReference.CompletionListener() {
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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("webcams");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id_remote;
                Rating rating;
                // iterate through all ids and check if we have one in the db
                for (DataSnapshot webCamSnapshot: dataSnapshot.getChildren()) {
                    // get the rating and check if it matches ou rating
                    id_remote = webCamSnapshot.getKey();
                    rating =  webCamSnapshot.getValue(Rating.class);
                    rating.setId(id_remote);
                    // check if it is the rating we are interested in
                    if (id_remote.equals(id)) {
                        // callback to activity which called getRating()
                        ratingServiceConsumer.onRatingGet(rating);
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
