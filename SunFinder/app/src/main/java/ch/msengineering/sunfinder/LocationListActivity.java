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
import ch.msengineering.sunfinder.item.LocationContent;
import ch.msengineering.sunfinder.services.WebServiceConsumer;
import ch.msengineering.sunfinder.services.webcam.WebCamService;
import ch.msengineering.sunfinder.services.webcam.WebCamServiceImpl;
import ch.msengineering.sunfinder.services.webcam.api.WebCamNearby;
import ch.msengineering.sunfinder.services.webcam.api.Webcam;
import retrofit2.Call;
import retrofit2.Response;

/**
 * An activity representing a list of Locations. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LocationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class LocationListActivity extends AppCompatActivity {

    /**
     * The list argument representing the list ID that this list
     * represents.
     */
    public static final String ARG_LIST_ID = "list_id";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private WebCamService webCamService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.location_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.location_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        webCamService = new WebCamServiceImpl(new WebServiceConsumer() {
            @Override
            public void onWebCamNearby(Response<WebCamNearby> response) {
                if (response.isSuccessful()) {
                    Log.i("SunFinder", "WebCamService: onWebCamNearby -> Response: " + response.toString());
                    for(Webcam webcam : response.body().getResult().getWebcams()) {
                        LocationContent.addItem(LocationContent.createItem(webcam.getId(), webcam));
                    }
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_list);
                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Log.e("SunFinder", "WebCamService: onWebCamNearby -> Failure: " + response.toString());
                    Snackbar.make(findViewById(R.id.location_list), "WebCamService: onWebCamNearby -> Failure: " + response.toString(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                Log.e("SunFinder", "WebCamService: onFailure -> Failure: " + call.toString(), t);
                Snackbar.make(findViewById(R.id.location_list), "WebCamService: onFailure -> Failure: " + call.toString(), Snackbar.LENGTH_LONG).show();
            }
        });

        if (getIntent().getExtras().containsKey(ARG_LIST_ID)) {
            GeoContent.GeoItem geoItem = GeoContent.ITEM_MAP.get(getIntent().getExtras().getString(ARG_LIST_ID));

            try {
                webCamService.getNearby(geoItem.geoLocation.getLatitude(), geoItem.geoLocation.getLongitude(), 10);
            } catch (Exception e) {
                Log.e("SunFinder", "WebCamService: getNearby -> Failure", e);
                Snackbar.make(findViewById(R.id.location_list), "WebCamService: getNearby -> Failure: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(LocationContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<LocationContent.LocationItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<LocationContent.LocationItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.location_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).webCam.getTitle());

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

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public LocationContent.LocationItem mItem;

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
