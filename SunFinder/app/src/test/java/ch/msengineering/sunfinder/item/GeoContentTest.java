package ch.msengineering.sunfinder.item;

import org.junit.Before;
import org.junit.Test;

import ch.msengineering.sunfinder.services.geolocation.api.GeoLocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GeoContentTest {

    @Before
    public void before() {
        GeoContent.clear();
    }

    @Test
    public void testCreationOfGeoItemDoesNotThrow() throws Exception {
        createGeoItem();
    }

    @Test
    public void testGeoContentAddSuccess() throws Exception {
        GeoContent.GeoItem geoItem = createGeoItem();
        GeoContent.addItem(geoItem);
        assertEquals(geoItem, GeoContent.getItemMap().values().iterator().next());
        assertEquals(geoItem, GeoContent.getItems().get(0));
    }

    @Test
    public void testGeoContentFilterIncludedSuccess() throws Exception {
        GeoContent.GeoItem geoItem = createGeoItem();
        GeoContent.addItem(geoItem);
        GeoContent.filter("name");
        assertEquals(geoItem, GeoContent.getItemMap().values().iterator().next());
        assertEquals(geoItem, GeoContent.getItems().get(0));
    }

    @Test
    public void testGeoContentFilterExcludedSuccess() throws Exception {
        GeoContent.GeoItem geoItem = createGeoItem();
        GeoContent.addItem(geoItem);
        GeoContent.filter("notExisting");
        assertEquals(geoItem, GeoContent.getItemMap().values().iterator().next());
        assertEquals(0, GeoContent.getItems().size());
    }

    @Test
    public void testGeoContentClearSuccess() throws Exception {
        GeoContent.GeoItem geoItem = createGeoItem();
        GeoContent.addItem(geoItem);
        GeoContent.clear();
        assertEquals(0, GeoContent.getItemMap().values().size());
        assertEquals(0, GeoContent.getItems().size());
    }

    @Test
    public void testFilterOfGeoItemYieldsTrue() {
        GeoContent.GeoItem geoItem = createGeoItem();

        assertTrue(geoItem.filter("name"));
        assertTrue(geoItem.filter("countryName"));
        assertTrue(geoItem.filter("countryCode"));
        assertTrue(geoItem.filter("country"));
        assertTrue(geoItem.filter("5"));
        assertTrue(geoItem.filter("7"));
    }

    @Test
    public void testFilterOfGeoItemYieldsFalse() {
        GeoContent.GeoItem geoItem = createGeoItem();

        assertFalse(geoItem.filter("notExisting"));
        assertFalse(geoItem.filter(null));
    }

    private GeoContent.GeoItem createGeoItem() {
        return GeoContent.createItem(new GeoLocation("name", "countryName", "countryCode", 5, 7));
    }

}