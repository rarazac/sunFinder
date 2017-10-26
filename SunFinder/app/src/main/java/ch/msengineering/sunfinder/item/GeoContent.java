package ch.msengineering.sunfinder.item;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.msengineering.sunfinder.services.geolocation.api.GeoLocation;

/**
 *
 */
public class GeoContent {

    private static final List<GeoItem> ORIGINAL_ITEMS = new ArrayList<>();
    /**
     * An array of items.
     */
    private static final List<GeoItem> ITEMS = new ArrayList<>();
    /**
     * A map of items, by ID.
     */
    private static final Map<String, GeoItem> ITEM_MAP = new HashMap<>();
    private static String currentQuery = null;

    private GeoContent() {
        //To hide the public constructor
    }

    public static List<GeoItem> getItems() {
        return ITEMS;
    }

    public static Map<String, GeoItem> getItemMap() {
        return ITEM_MAP;
    }

    public static void addItem(GeoItem item) {
        if (!ITEM_MAP.containsKey(item.id)) {
            ITEM_MAP.put(item.id, item);
            ORIGINAL_ITEMS.add(item);
            GeoItem filterItem = filterItem(item);
            if (filterItem != null) {
                ITEMS.add(item);
            }
            Collections.sort(ITEMS);
        }
    }

    public static void filter(String query) {
        currentQuery = query != null ? query.toLowerCase() : "";

        for (GeoItem item : ORIGINAL_ITEMS) {
            ITEMS.remove(item);

            GeoItem filterItem = filterItem(item);
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

    public static GeoItem createItem(GeoLocation geoLocation) {
        return new GeoItem(geoLocation);
    }

    private static GeoItem filterItem(GeoItem item) {
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
    public static class GeoItem implements Comparable<GeoItem> {
        public final String id;
        public final GeoLocation geoLocation;

        GeoItem(GeoLocation geoLocation) {
            this.id = geoLocation.getName() + ":" + geoLocation.getLatitude() + ":" + geoLocation.getLongitude();
            this.geoLocation = geoLocation;
        }

        boolean filter(String query) {
            return id.toLowerCase().contains(query) ||
                    (geoLocation != null && (
                            geoLocation.getName().toLowerCase().contains(query) ||
                                    geoLocation.getCountryCode().toLowerCase().contains(query) ||
                                    geoLocation.getCountryName().toLowerCase().contains(query) ||
                                    (Double.toString(geoLocation.getLongitude())).toLowerCase().contains(query) ||
                                    (Double.toString(geoLocation.getLatitude())).toLowerCase().contains(query)
                    ));
        }

        @Override
        public String toString() {
            return String.format("id: %s, geoLocation: %s", id, geoLocation);
        }

        @Override
        public int compareTo(@NonNull GeoItem other) {
            return geoLocation.getName().compareTo(other.geoLocation.getName());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            GeoItem geoItem = (GeoItem) o;

            return new EqualsBuilder()
                    .append(id, geoItem.id)
                    .append(geoLocation.getName(), geoItem.geoLocation.getName())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(id)
                    .append(geoLocation.getName())
                    .toHashCode();
        }
    }
}
