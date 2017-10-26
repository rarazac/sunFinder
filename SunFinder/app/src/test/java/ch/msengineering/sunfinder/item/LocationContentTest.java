package ch.msengineering.sunfinder.item;

import org.junit.Before;
import org.junit.Test;

import ch.msengineering.sunfinder.services.webcam.api.Image;
import ch.msengineering.sunfinder.services.webcam.api.Location;
import ch.msengineering.sunfinder.services.webcam.api.Webcam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LocationContentTest {

    @Before
    public void before() {
        LocationContent.clear();
    }

    @Test
    public void testCreationOfLocationItemDoesNotThrow() throws Exception {
        createLocationItem();
    }

    @Test
    public void testGeoContentAddSuccess() throws Exception {
        LocationContent.LocationItem locationItem = createLocationItem();
        LocationContent.addItem(locationItem);
        assertEquals(locationItem, LocationContent.getItemMap().values().iterator().next());
        assertEquals(locationItem, LocationContent.getItems().get(0));
    }

    @Test
    public void testGeoContentFilterIncludedSuccess() throws Exception {
        LocationContent.LocationItem locationItem = createLocationItem();
        LocationContent.addItem(locationItem);
        LocationContent.filter("title");
        assertEquals(locationItem, LocationContent.getItemMap().values().iterator().next());
        assertEquals(locationItem, LocationContent.getItems().get(0));
    }

    @Test
    public void testGeoContentFilterExcludedSuccess() throws Exception {
        LocationContent.LocationItem locationItem = createLocationItem();
        LocationContent.addItem(locationItem);
        LocationContent.filter("notExisting");
        assertEquals(locationItem, LocationContent.getItemMap().values().iterator().next());
        assertEquals(0, LocationContent.getItems().size());
    }

    @Test
    public void testGeoContentClearSuccess() throws Exception {
        LocationContent.LocationItem locationItem = createLocationItem();
        LocationContent.addItem(locationItem);
        LocationContent.clear();
        assertEquals(0, LocationContent.getItemMap().values().size());
        assertEquals(0, LocationContent.getItems().size());
    }

    @Test
    public void testFilterOfGeoItemYieldsTrue() {
        LocationContent.LocationItem locationItem = createLocationItem();

        assertTrue(locationItem.filter("1"));
        assertTrue(locationItem.filter("title"));
        assertTrue(locationItem.filter("5"));
        assertTrue(locationItem.filter("7"));
    }

    @Test
    public void testFilterOfLocationItemYieldsFalse() {
        LocationContent.LocationItem locationItem = createLocationItem();

        assertFalse(locationItem.filter("notExisting"));
        assertFalse(locationItem.filter(null));
    }

    private LocationContent.LocationItem createLocationItem() {
        return LocationContent.createItem("1", new Webcam("1", "200", "title", new Image(), new Location("city", "region", "regionCode", "country", "countryCode", "continent", "continentCode", 5, 7, "timezone"), 10));
    }

}