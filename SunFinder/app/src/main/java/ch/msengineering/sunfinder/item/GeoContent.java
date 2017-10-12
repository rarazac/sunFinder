package ch.msengineering.sunfinder.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.msengineering.sunfinder.services.geolocation.api.GeoLocation;
import ch.msengineering.sunfinder.services.webcam.api.Webcam;

/**
 *
 */
public class GeoContent {

    /**
     * An array of items.
     */
    public static final List<GeoItem> ITEMS = new ArrayList<>();

    /**
     * A map of items, by ID.
     */
    public static final Map<String, GeoItem> ITEM_MAP = new HashMap<>();

    public static void addItem(GeoItem item) {
        if (!ITEM_MAP.containsKey(item.id)) {
            ITEM_MAP.put(item.id, item);
            ITEMS.add(item);
        }
    }

    public static GeoItem createItem(GeoLocation geoLocation) {
        return new GeoItem(geoLocation);
    }

    /**
     * A item representing a piece of content.
     */
    public static class GeoItem {
        public final String id;
        public final GeoLocation geoLocation;

        public GeoItem(GeoLocation geoLocation) {
            this.id = geoLocation.getName() + ":" + geoLocation.getLatitude() + ":" + geoLocation.getLongitude();
            this.geoLocation = geoLocation;
        }

        @Override
        public String toString() {
            return String.format("id: %s, geoLocation: %s", id, geoLocation);
        }
    }
}
