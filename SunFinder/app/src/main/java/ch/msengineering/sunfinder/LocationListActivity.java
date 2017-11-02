package ch.msengineering.sunfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.List;

import ch.msengineering.sunfinder.item.GeoContent;
import ch.msengineering.sunfinder.item.LocationContent;
import ch.msengineering.sunfinder.services.RatingServiceConsumer;
import ch.msengineering.sunfinder.services.WebServiceConsumer;
import ch.msengineering.sunfinder.services.geolocation.api.GeoLocation;
import ch.msengineering.sunfinder.services.rating.RatingService;
import ch.msengineering.sunfinder.services.rating.RatingServiceImplementation;
import ch.msengineering.sunfinder.services.rating.api.Rating;
import ch.msengineering.sunfinder.services.webcam.WebCamService;
import ch.msengineering.sunfinder.services.webcam.WebCamServiceImpl;
import ch.msengineering.sunfinder.services.webcam.api.WebCamNearby;
import ch.msengineering.sunfinder.services.webcam.api.Webcam;
import retrofit2.Call;
import retrofit2.Response;

import static android.support.design.widget.BaseTransientBottomBar.LENGTH_LONG;
import static ch.msengineering.sunfinder.Constants.LOG_TAG;

/**
 * An activity representing a list of Locations. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LocationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class LocationListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    /**
     * The list argument representing the list ID that this list
     * represents.
     */
    public static final String ARG_LIST_ID = "list_id";
    private static final int INITIAL_RADIUS_OF_SEARCH_KM = 20;
    private static final int EXTENSION_FACTOR_RADIUS_OF_SEARCH_KM = 10;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private WebCamService webCamService;
    private GeoLocation lastSearchedLocation;
    private int lastRadiusKm;
    private RatingService ratingService;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.location_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        webCamService = createWebCamService();

        ratingService = createRatingService();


        if (getIntent() != null && getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey(ARG_LIST_ID)) {

            LocationContent.clear();

            recyclerView.getAdapter().notifyDataSetChanged();

            GeoContent.GeoItem geoItem = GeoContent.getItemMap().get(getIntent().getExtras().getString(ARG_LIST_ID));
            if (geoItem != null) {
                getWebCamNearby(geoItem.geoLocation, INITIAL_RADIUS_OF_SEARCH_KM);
            }
        }
    }

    @NonNull
    private RatingServiceImplementation createRatingService() {
        return new RatingServiceImplementation(new RatingServiceConsumer() {
            @Override
            public void onRatingSet(DatabaseError databaseError) {
                // dont need this here, we can only rate in LocationDetailActivity
            }

            @Override
            public void onRatingGet(Rating rating) {
                int ts;
                Long tsLong;
                for (int i = 0; i < LocationContent.getItems().size(); i++) {
                    if (LocationContent.getItems().get(i).webCam.getId().equals(rating.getId())) {
                        // check if timeStamp is not too old
                        tsLong = System.currentTimeMillis() / 1000;
                        ts = tsLong.intValue();
                        if ((ts - rating.getTimeStamp()) < 3600) {
                            // timeStamp is not older than 1h ( 1h = 60min*60sec = 3600s
                            Log.v(LOG_TAG, "LocationListActivity.onRatingGet(); "
                                    + "id = " + rating.getId() + "  "
                                    + "ratingValue = " + rating.getRatingValue() + "  "
                                    + "timeStamp = " + rating.getTimeStamp());
                            // we found the webcam which has the same id as one in the db, set the rating
                            Webcam webcam = LocationContent.getItems().get(i).webCam;
                            webcam.setRating(rating.getRatingValue());
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_list);
                            recyclerView.getAdapter().notifyItemChanged(i);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                showSnackbar("this webcam was already rated, wait a bit");
                Log.e(LOG_TAG, "RatingService: onFailure -> Failure: ", e);
            }
        });
    }

    @NonNull
    private WebCamServiceImpl createWebCamService() {
        return new WebCamServiceImpl(new WebServiceConsumer() {
            @Override
            public void onWebCamNearby(Response<WebCamNearby> response) {
                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, "WebCamService: onWebCamNearby -> Response: " + response.toString());

                    if (lastSearchedLocation != null && response.body().getResult().getWebcams().isEmpty()) {
                        getWebCamNearby(lastSearchedLocation, lastRadiusKm * EXTENSION_FACTOR_RADIUS_OF_SEARCH_KM);
                        return;
                    }

                    lastSearchedLocation = null;
                    lastRadiusKm = 0;

                    for (Webcam webcam : response.body().getResult().getWebcams()) {
                        LocationContent.addItem(LocationContent.createItem(webcam.getId(), webcam));
                        // check if we have a rating for every webcam we find
                        getRating(webcam.getId());
                    }
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_list);
                    recyclerView.getAdapter().notifyDataSetChanged();

                } else {
                    Log.e(LOG_TAG, "WebCamService: onWebCamNearby -> Failure: " + response.toString());
                    showSnackbar("WebCamService: onWebCamNearby -> Failure: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                Log.e(LOG_TAG, "WebCamService: onFailure -> Failure: " + call.toString(), t);
                showSnackbar("WebCamService: onFailure -> Failure: " + call.toString());
            }
        });
    }

    // login to firebase db before we start any service
    // see https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/AnonymousAuthActivity.java#L83-L104
    @Override
    public void onStart() {
        super.onStart();

        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LOG_TAG, "signInAnonymously:success");
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LOG_TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // update webcams and ratings when coming back from detail view or search view
        GeoContent.GeoItem geoItem = GeoContent.getItemMap().get(getIntent().getExtras().getString(ARG_LIST_ID));
        // check if we have a location  otherwise we dont have to get the webcams
        if (geoItem != null) {
            getWebCamNearby(geoItem.geoLocation, INITIAL_RADIUS_OF_SEARCH_KM);
        }
    }

    private void getWebCamNearby(GeoLocation geoLocation, int radiusOfSearchKm) {
        try {
            lastSearchedLocation = geoLocation;
            lastRadiusKm = radiusOfSearchKm;
            webCamService.getNearby(geoLocation.getLatitude(), geoLocation.getLongitude(), radiusOfSearchKm);
        } catch (Exception e) {
            Log.e(LOG_TAG, "WebCamService: getNearby -> Failure", e);
            showSnackbar("WebCamService: getNearby -> Failure: " + e.getMessage());
        }
    }

    private void getRating(String id) {
        try {
            ratingService.getRating(id);
        } catch (Exception e) {
            Log.e(LOG_TAG, "RatingService: getRating -> Failure", e);
            showSnackbar("RatingService: getRating -> Failure: " + e.getMessage());
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return onQueryTextChange(query);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        LocationContent.filter(query);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_list);
        recyclerView.getAdapter().notifyDataSetChanged();

        return true;
    }

    private void showSnackbar(final String message) {
        Snackbar.make(findViewById(R.id.location_list), message, LENGTH_LONG).show();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(LocationContent.getItems()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.LocationListViewHolder> {

        private List<LocationContent.LocationItem> mValues;

        private SimpleItemRecyclerViewAdapter(List<LocationContent.LocationItem> items) {
            // add with mValues.add() otherwise recyclerView.getAdapter().notifyItemChanged(i)
            // does not work!
            mValues = items;
            mValues.clear();
            for (int i = 0; i < items.size(); i++) {
                mValues.add(i, items.get(i));
            }
        }

        @Override
        public LocationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.location_list_content, parent, false);
            return new LocationListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final LocationListViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            Picasso.with(LocationListActivity.this).load(mValues.get(position).webCam.getImage().getCurrent().getThumbnail()).into(holder.mImageView);
            holder.mNameView.setText(mValues.get(position).webCam.getTitle());
            holder.mCountryNameView.setText(mValues.get(position).webCam.getLocation().getCountry());
            holder.mLatitudeView.setText(String.format("%s", mValues.get(position).webCam.getLocation().getLatitude()));
            holder.mLongitudeView.setText(String.format("%s", mValues.get(position).webCam.getLocation().getLongitude()));
            holder.mRatingBar.setRating(mValues.get(position).webCam.getRating());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(LocationDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        LocationDetailFragment fragment = new LocationDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.location_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, LocationDetailActivity.class);
                        intent.putExtra(LocationDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class LocationListViewHolder extends RecyclerView.ViewHolder {
            private final View mView;
            private final ImageView mImageView;
            private final TextView mNameView;
            private final TextView mCountryNameView;
            private final TextView mLatitudeView;
            private final TextView mLongitudeView;
            private final RatingBar mRatingBar;
            private LocationContent.LocationItem mItem;

            private LocationListViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = view.findViewById(R.id.image);
                mNameView = view.findViewById(R.id.name);
                mCountryNameView = view.findViewById(R.id.country_name);
                mLatitudeView = view.findViewById(R.id.latitude);
                mLongitudeView = view.findViewById(R.id.longitude);
                mRatingBar = view.findViewById(R.id.ratingBar);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }
}
