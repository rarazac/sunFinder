package ch.msengineering.sunfinder.item;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.msengineering.sunfinder.services.webcam.api.Webcam;

/**
 *
 */
public class LocationContent {

    private static final List<LocationItem> ORIGINAL_ITEMS = new ArrayList<>();
    /**
     * An array of items.
     */
    private static final List<LocationItem> ITEMS = new ArrayList<>();
    /**
     * A map of items, by ID.
     */
    private static final Map<String, LocationItem> ITEM_MAP = new HashMap<>();
    private static String currentQuery = "";

    private LocationContent() {
        //To hide the public constructor
    }

    public static List<LocationItem> getItems() {
        return ITEMS;
    }

    public static Map<String, LocationItem> getItemMap() {
        return ITEM_MAP;
    }

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

        for (LocationItem item : ORIGINAL_ITEMS) {
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

        LocationItem(String id, Webcam webCam) {
            this.id = id;
            this.webCam = webCam;
        }

        boolean filter(String query) {
            return id.toLowerCase().contains(query) ||
                    (webCam != null && (
                            webCam.getId().toLowerCase().contains(query) ||
                                    webCam.getTitle().toLowerCase().contains(query) ||
                                    (webCam.getLocation() != null && (
                                            (Double.toString(webCam.getLocation().getLongitude())).toLowerCase().contains(query) ||
                                                    (Double.toString(webCam.getLocation().getLatitude())).toLowerCase().contains(query)
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            LocationItem that = (LocationItem) o;

            return new EqualsBuilder()
                    .append(id, that.id)
                    .append(webCam.getTitle(), that.webCam.getTitle())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(id)
                    .append(webCam.getTitle())
                    .toHashCode();
        }
    }
}
