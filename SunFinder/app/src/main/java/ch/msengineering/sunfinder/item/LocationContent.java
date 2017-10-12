package ch.msengineering.sunfinder.item;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.msengineering.sunfinder.services.geolocation.api.GeoLocation;
import ch.msengineering.sunfinder.services.webcam.api.WebCamNearby;
import ch.msengineering.sunfinder.services.webcam.api.Webcam;

/**
 *
 */
public class LocationContent {

    private static final List<LocationItem> ORIGINAL_ITEMS = new ArrayList<>();
    private static String currentQuery = "";

    /**
     * An array of items.
     */
    public static final List<LocationItem> ITEMS = new ArrayList<>();

    /**
     * A map of items, by ID.
     */
    public static final Map<String, LocationItem> ITEM_MAP = new HashMap<>();

    public static void addItem(LocationItem item) {
        if (!ITEM_MAP.containsKey(item.id)) {
            ITEM_MAP.put(item.id, item);
            ORIGINAL_ITEMS.add(item);
            LocationItem filterItem = filterItem(item);
            if (filterItem != null) {
                ITEMS.add(item);
            }
            Collections.sort(ITEMS);
        }
    }

    public static void filter(String query) {
        currentQuery = query != null ? query.toLowerCase() : "";

        for(LocationItem item : ORIGINAL_ITEMS) {
            ITEMS.remove(item);

            LocationItem filterItem = filterItem(item);
            if (filterItem != null) {
                ITEMS.add(filterItem);
            }
        }

        Collections.sort(ITEMS);
    }

    public static void clear() {
        currentQuery = null;
        ITEM_MAP.clear();
        ORIGINAL_ITEMS.clear();
        ITEMS.clear();
    }

    public static LocationItem createItem(String id, Webcam webCam) {
        return new LocationItem(id, webCam);
    }

    private static LocationItem filterItem(LocationItem item) {
        if (currentQuery == null || currentQuery.isEmpty()) {
            return item;
        }
        if (item.filter(currentQuery)) {
            return item;
        }
        return null;
    }

    /**
     * A item representing a piece of content.
     */
    public static class LocationItem implements Comparable<LocationItem> {
        public final String id;
        public final Webcam webCam;

        public LocationItem(String id, Webcam webCam) {
            this.id = id;
            this.webCam = webCam;
        }

        public boolean filter(String query) {
            return id.toLowerCase().contains(query) ||
                    (webCam != null && (
                        webCam.getId().toLowerCase().contains(query) ||
                        webCam.getTitle().toLowerCase().contains(query) ||
                            (webCam.getLocation() != null && (
                                ("" + webCam.getLocation().getLongitude()).toLowerCase().contains(query) ||
                                ("" + webCam.getLocation().getLatitude()).toLowerCase().contains(query)
                            ))
                    ));
        }

        @Override
        public String toString() {
            return String.format("id: %s, webCamNearby: %s", id, webCam);
        }

        @Override
        public int compareTo(@NonNull LocationItem other) {
            return webCam.getTitle().compareTo(other.webCam.getTitle());
        }
    }
}
