package ch.msengineering.sunfinder.services;

import org.junit.Test;

import ch.msengineering.sunfinder.Constants;

import static org.junit.Assert.assertEquals;

/**
 * Created by raphe on 26/10/2017.
 */

public class ConstantsTest {

    @Test
    public void testConsumeConstants() {
        assertEquals("webcams", Constants.DB_ROOT);
        assertEquals("https://webcamstravel.p.mashape.com", Constants.ENDPOINT);
        assertEquals("SunFinder", Constants.LOG_TAG);
    }

}
