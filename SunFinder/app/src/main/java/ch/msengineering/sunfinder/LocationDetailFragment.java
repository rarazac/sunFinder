package ch.msengineering.sunfinder;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

import ch.msengineering.sunfinder.item.LocationContent;

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
    public LocationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = LocationContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.webCam.getTitle());
                ImageView imageView = activity.findViewById(R.id.image);
                if (imageView != null) {
                    Picasso.with(LocationDetailFragment.this.getContext()).load(mItem.webCam.getImage().getCurrent().getPreview()).into(imageView);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.name)).setText(mItem.webCam.getTitle());
            ((TextView) rootView.findViewById(R.id.country_name)).setText(mItem.webCam.getLocation().getCountry());
            ((TextView) rootView.findViewById(R.id.latitude)).setText(String.format("%s", mItem.webCam.getLocation().getLatitude()));
            ((TextView) rootView.findViewById(R.id.longitude)).setText(String.format("%s", mItem.webCam.getLocation().getLongitude()));
            ((TextView) rootView.findViewById(R.id.lastupdate)).setText(getDate(mItem.webCam.getImage().getUpdate()));
        }

        return rootView;
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd.MM.yyyy HH:mm:ss", cal).toString();
        return date;
    }
}
