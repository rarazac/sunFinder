package ch.msengineering.sunfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.view.View;

import com.google.firebase.database.DatabaseError;

import ch.msengineering.sunfinder.item.LocationContent;
import ch.msengineering.sunfinder.services.RatingServiceConsumer;
import ch.msengineering.sunfinder.services.rating.RatingService;
import ch.msengineering.sunfinder.services.rating.RatingServiceImplementation;

import static android.support.design.widget.BaseTransientBottomBar.LENGTH_LONG;

/**
 * An activity representing a single Location detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link LocationListActivity}.
 */
public class LocationDetailActivity extends AppCompatActivity {
    private RatingService ratingService;
    private int ratingBarValue;
    private RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        final LocationContent.LocationItem mItem = LocationContent.ITEM_MAP.get(getIntent().getStringExtra(LocationDetailFragment.ARG_ITEM_ID));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:" + mItem.webCam.getLocation().getLatitude() + "," + mItem.webCam.getLocation().getLongitude() +
                                "?q=" + mItem.webCam.getLocation().getLatitude() + "," + mItem.webCam.getLocation().getLongitude() + "(" + mItem.webCam.getTitle() + ")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });
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
            public void onRatingGet(String id , int ratingValue) {
                // we have to update the rating bar according to the rating from the database
                ratingBar.setRating(ratingValue);
                Log.v("sunFinder","onRatingGet setting ratinBar value: id="+id + "rating=" + ratingValue);

            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.e("SunFinder", "RatingService: onFailure -> Failure: ",databaseError.toException());
            }
        });
        // pushButton, if we are in tablet mode we dont find the push_rating button because
        // the activity is wrong!
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.push_rating);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRating(mItem.id, ratingBarValue);
            }
        });

        // ratingBar
        ratingBar =  (RatingBar) findViewById(R.id.ratingBar);
        getRating(mItem.id);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                // we use only int values in the database
                ratingBarValue = Math.round(rating);
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(LocationDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(LocationDetailFragment.ARG_ITEM_ID));
            LocationDetailFragment fragment = new LocationDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.location_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, LocationListActivity.class);
            intent.putExtra("home", true);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Snackbar.make(findViewById(R.id.location_detail), message, LENGTH_LONG).show();
    }
}
