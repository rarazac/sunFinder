package ch.msengineering.sunfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;

import java.util.List;

import ch.msengineering.sunfinder.item.GeoContent;
import ch.msengineering.sunfinder.services.LocalServiceConsumer;
import ch.msengineering.sunfinder.services.geolocation.GeoLocationService;
import ch.msengineering.sunfinder.services.geolocation.GeoLocationServiceImpl;
import ch.msengineering.sunfinder.services.geolocation.api.GeoLocation;

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
public class GeoListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private GeoLocationService geoLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.geo_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        geoLocationService = new GeoLocationServiceImpl(this, new LocalServiceConsumer() {
            @Override
            public void onGeoLocation(GeoLocation geoLocation) {
                Log.i(LOG_TAG, "GeoLocationService: onGeoLocation -> Response: " + geoLocation);

                GeoContent.addItem(GeoContent.createItem(geoLocation));

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.geo_list);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onGeoLocation(List<GeoLocation> geoLocations) {
                for (GeoLocation geoLocation : geoLocations) {
                    Log.i(LOG_TAG, "GeoLocationService: onGeoLocation -> Response: " + geoLocation);

                    GeoContent.addItem(GeoContent.createItem(geoLocation));

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.geo_list);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String message, Throwable t) {
                Log.e(LOG_TAG, "GeoLocationService: onFailure -> Failure: " + message, t);
                showSnackbar("GeoLocationService: onFailure -> Failure: " + message);
            }
        });

        if (getIntent() != null && getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey("home") && getIntent().getExtras().getBoolean("home")) {

            return;
        }

        getCurrentLocation();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            GeoContent.clear();

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.geo_list);
            recyclerView.getAdapter().notifyDataSetChanged();

            geoLocationService.getGeoLocationByName(query);
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, "GeoLocationService: getGeoLocationByName -> Failure", e);
            showSnackbar("GeoLocationService: getGeoLocationByName -> Failure: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean onQueryTextChange(String query) {
        GeoContent.filter(query);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.geo_list);
        recyclerView.getAdapter().notifyDataSetChanged();

        return true;
    }

    private void getCurrentLocation() {
        try {
            geoLocationService.getCurrentLocation();
        } catch (Exception e) {
            Log.e(LOG_TAG, "GeoLocationService: getCurrentLocation -> Failure", e);
            showSnackbar("GeoLocationService: getCurrentLocation -> Failure: " + e.getMessage());
        }
    }

    private void showSnackbar(final String message) {
        Snackbar.make(findViewById(R.id.geo_list), message, LENGTH_LONG).show();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(GeoContent.getItems()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.GeoListViewHolder> {

        private final List<GeoContent.GeoItem> mValues;

        private SimpleItemRecyclerViewAdapter(List<GeoContent.GeoItem> items) {
            mValues = items;
        }

        @Override
        public GeoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.geo_list_content, parent, false);
            return new GeoListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final GeoListViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(mValues.get(position).geoLocation.getName());
            holder.mCountryNameView.setText(mValues.get(position).geoLocation.getCountryName());
            holder.mLatitudeView.setText(String.format("%s", mValues.get(position).geoLocation.getLatitude()));
            holder.mLongitudeView.setText(String.format("%s", mValues.get(position).geoLocation.getLongitude()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, LocationListActivity.class);
                    intent.putExtra(LocationListActivity.ARG_LIST_ID, holder.mItem.id);

                    context.startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class GeoListViewHolder extends RecyclerView.ViewHolder {
            private final View mView;
            private final TextView mNameView;
            private final TextView mCountryNameView;
            private final TextView mLatitudeView;
            private final TextView mLongitudeView;

            private GeoContent.GeoItem mItem;

            private GeoListViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = view.findViewById(R.id.name);
                mCountryNameView = view.findViewById(R.id.country_name);
                mLatitudeView = view.findViewById(R.id.latitude);
                mLongitudeView = view.findViewById(R.id.longitude);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }
}
