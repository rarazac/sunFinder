package ch.msengineering.sunfinder;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import ch.msengineering.sunfinder.item.LocationContent;
import ch.msengineering.sunfinder.services.RatingServiceConsumer;
import ch.msengineering.sunfinder.services.rating.RatingService;
import ch.msengineering.sunfinder.services.rating.RatingServiceImplementation;
import ch.msengineering.sunfinder.services.rating.api.Rating;


import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

import static android.support.design.widget.BaseTransientBottomBar.LENGTH_LONG;

/**
 * A fragment representing a single Location detail screen.
 * This fragment is either contained in a {@link LocationListActivity}
 * in two-pane mode (on tablets) or a {@link LocationDetailActivity}
 * on handsets.
 */
public class LocationDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private LocationContent.LocationItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private RatingService ratingService;
    private RatingBar ratingBar;
    private Activity activity;
    public LocationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this.getActivity();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = LocationContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            // request a rating from db

            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.webCam.getTitle());
                ImageView imageView = activity.findViewById(R.id.image);
                if (imageView != null) {
                    Picasso.with(LocationDetailFragment.this.getContext()).load(mItem.webCam.getImage().getCurrent().getPreview()).into(imageView);
                }
            }
        }
        // ratingService
        ratingService = new RatingServiceImplementation(new RatingServiceConsumer() {
            @Override
            public void onRatingSet(DatabaseError databaseError) {
                // display result of rating push to the user
                if (databaseError == null) {
                    showSnackbar("thanks for your rating!");
                } else {
                    showSnackbar("sorry, your rating could not be published");
                }
            }

            @Override
            public void onRatingGet(Rating rating) {
                // we have to update the rating bar according to the rating from the database
                Log.v("sunFinder", "LocationDetailActivity.onRatingGet(); "
                        + "id = " + rating.getId() + "  "
                        + "ratingValue = " + rating.getRatingValue() + "  "
                        + "timeStamp = " + rating.getTimeStamp());
                // check if timeStamp is not too old
                Long tsLong = System.currentTimeMillis() / 1000;
                int ts = tsLong.intValue();
                if ((ts - rating.getTimeStamp()) < 3600) {
                    // timeStamp is not older than 1h ( 1h = 60min*60sec = 3600s
                     ratingBar.setRating(rating.getRatingValue());
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.e("SunFinder", "RatingService: onFailure -> Failure: ",databaseError.toException());
            }
        });
        // request a rating
        getRating(mItem.id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // set the subtitle in the location_detail
        View rootView = inflater.inflate(R.layout.location_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.name)).setText(mItem.webCam.getTitle());
            ((TextView) rootView.findViewById(R.id.courtesy)).setText("Webcams provided by webcams.travel");
            ((TextView) rootView.findViewById(R.id.country_name)).setText(mItem.webCam.getLocation().getCountry());
            ((TextView) rootView.findViewById(R.id.latitude)).setText(String.format("%s", mItem.webCam.getLocation().getLatitude()));
            ((TextView) rootView.findViewById(R.id.longitude)).setText(String.format("%s", mItem.webCam.getLocation().getLongitude()));
            ((TextView) rootView.findViewById(R.id.lastupdate)).setText(getDate(mItem.webCam.getImage().getUpdate()));

            // rating is only pushed when this button is pressed
            FloatingActionButton fab2 = (FloatingActionButton) rootView.findViewById(R.id.push_rating);
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setRating(mItem.id, (int) Math.ceil(ratingBar.getRating()));
                }
            });

            // ratingBar
            ratingBar = rootView.findViewById(R.id.ratingBar);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
                    //set the rating which the user changes
                    ratingBar.setRating(rating);
                }
            });
        }

        return rootView;
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd.MM.yyyy HH:mm:ss", cal).toString();
        return date;
    }

    // uses ratingService to set the rating of webcam with id: id
    private void setRating(String id, int ratingValue) {
        try {
            ratingService.setRating(id, ratingValue);
        } catch (Exception e) {
            Log.e("SunFinder", "ratingService: setRating -> Failure", e);
        }
    }

    // uses ratingService to get the rating of webcam with id: id
    private void getRating(String id) {
        try {
            ratingService.getRating(id);
        } catch (Exception e) {
            Log.e("SunFinder", "ratingService: getRating -> Failure", e);
        }
    }
    // helper for Snackbar
    private void showSnackbar(final String message) {
        Snackbar.make(activity.findViewById(R.id.location_detail), message, LENGTH_LONG).show();
    }
}
