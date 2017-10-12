package ch.msengineering.sunfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

/**
 * An activity representing a list of Locations. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LocationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class GeoListActivity extends AppCompatActivity {

    private GeoLocationService geoLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });

        View recyclerView = findViewById(R.id.geo_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        final Context context = this;

        geoLocationService = new GeoLocationServiceImpl(this, new LocalServiceConsumer() {
            @Override
            public void onGeoLocation(GeoLocation geoLocation) {
                Log.i("SunFinder", "GeoLocationService: onGeoLocation -> Response: " + geoLocation);

                GeoContent.GeoItem item = GeoContent.createItem(geoLocation);
                GeoContent.addItem(item);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.geo_list);
                recyclerView.getAdapter().notifyDataSetChanged();

                Intent intent = new Intent(context, LocationListActivity.class);
                intent.putExtra(LocationListActivity.ARG_LIST_ID, item.id);

                context.startActivity(intent);
            }
            @Override
            public void onGeoLocation(List<GeoLocation> geoLocations) {
                for(GeoLocation geoLocation : geoLocations) {
                    Log.i("SunFinder", "GeoLocationService: onGeoLocation -> Response: " + geoLocation);
                    GeoContent.addItem(GeoContent.createItem(geoLocation));
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.geo_list);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String message, Throwable t) {
                Log.e("SunFinder", "GeoLocationService: onFailure -> Failure: " + message, t);
                Snackbar.make(findViewById(R.id.geo_list), "GeoLocationService: onFailure -> Failure: " + message, Snackbar.LENGTH_LONG).show();
            }
        });

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        try {
            geoLocationService.getCurrentLocation();
        } catch (Exception e) {
            Log.e("SunFinder", "GeoLocationService: getCurrentLocation -> Failure", e);
            Snackbar.make(findViewById(R.id.geo_list), "GeoLocationService: getCurrentLocation -> Failure: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(GeoContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<GeoContent.GeoItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<GeoContent.GeoItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.geo_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).geoLocation.getName());

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

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public GeoContent.GeoItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.id);
                mContentView = view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
