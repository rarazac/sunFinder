package ch.msengineering.sunfinder.item;

import java.util.ArrayList;
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
            ITEMS.add(item);
        }
    }

    public static LocationItem createItem(String id, Webcam webCam) {
        return new LocationItem(id, webCam);
    }

    /**
     * A item representing a piece of content.
     */
    public static class LocationItem {
        public final String id;
        public final Webcam webCam;

        public LocationItem(String id, Webcam webCam) {
            this.id = id;
            this.webCam = webCam;
        }

        @Override
        public String toString() {
            return String.format("id: %s, webCamNearby: %s", id, webCam);
        }
    }
}
