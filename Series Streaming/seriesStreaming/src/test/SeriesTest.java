package test;

import main.Series;
import main.SeriesModel;
import main.Save_Load;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class SeriesTest {
    private List<SeriesModel> testData;

    @BeforeEach
    void setup() {
        // Setting up test data before each test
        testData = new ArrayList<>();

        SeriesModel s1 = new SeriesModel();
        s1.setID(1);
        s1.setName("Naruto");
        s1.setAge(13);
        s1.setNumberOfEpisodes(220);

        SeriesModel s2 = new SeriesModel();
        s2.setID(2);
        s2.setName("One Piece");
        s2.setAge(12);
        s2.setNumberOfEpisodes(1000);

        testData.add(s1);
        testData.add(s2);
    }

    @Test
    void testSearchSeriesFound() {
        // Testing searchSeries for a series that exists
        try (MockedStatic<Save_Load> mocked = org.mockito.Mockito.mockStatic(Save_Load.class)) {
            mocked.when(Save_Load::load).thenReturn(testData);
            SeriesModel result = Series.searchSeries(1);

            assertNotNull(result, "Expected series to be found, but it was null");
            assertEquals(1, result.getID(), "ID should match the search ID");
            assertEquals("Naruto", result.getName(), "Name should match the test series");
            assertEquals(13, result.getAge(), "Age should match the test series");
            assertEquals(220, result.getNumberOfEpisodes(), "Number of episodes should match");
        }
    }

    @Test
    void testSearchSeriesNotFound() {
        // Testing searchSeries for a series that does not exist
        try (MockedStatic<Save_Load> mocked = org.mockito.Mockito.mockStatic(Save_Load.class)) {
            mocked.when(Save_Load::load).thenReturn(testData);
            SeriesModel result = Series.searchSeries(99);
            assertNull(result, "Expected null for non-existent series ID");
        }
    }

    @Test
    void testUpdateSeriesName() {
        // Testing updateSeries for updating the name field
        try (MockedStatic<Save_Load> mocked = org.mockito.Mockito.mockStatic(Save_Load.class)) {
            // Mock load to return test data, and save to do nothing
            mocked.when(Save_Load::load).thenReturn(testData);
            mocked.when(() -> Save_Load.save(org.mockito.ArgumentMatchers.<ArrayList<SeriesModel>>any())).then(invocation -> null);

            boolean updated = Series.updateSeries(1, "name", "Bleach");
            assertTrue(updated, "Expected update to succeed");

            // The name should be updated in the testData list
            SeriesModel updatedModel = testData.stream().filter(s -> s.getID() == 1).findFirst().orElse(null);
            assertNotNull(updatedModel, "Series should exist after update");
            assertEquals("Bleach", updatedModel.getName(), "Name should be updated to 'Bleach'");
        }
    }

    @Test
    void testUpdateSeriesAgeInvalid() {
        // Testing updateSeries with invalid age input (not a number)
        try (MockedStatic<Save_Load> mocked = org.mockito.Mockito.mockStatic(Save_Load.class)) {
            mocked.when(Save_Load::load).thenReturn(testData);
            mocked.when(() -> Save_Load.save(org.mockito.ArgumentMatchers.<ArrayList<SeriesModel>>any())).then(invocation -> null);

            boolean updated = Series.updateSeries(1, "age", "notANumber");
            assertFalse(updated, "Expected update to fail due to invalid age input");
        }
    }

    @Test
    void testUpdateSeriesFieldNotFound() {
        // Testing updateSeries with an unknown field
        try (MockedStatic<Save_Load> mocked = org.mockito.Mockito.mockStatic(Save_Load.class)) {
            mocked.when(Save_Load::load).thenReturn(testData);
            mocked.when(() -> Save_Load.save(org.mockito.ArgumentMatchers.<ArrayList<SeriesModel>>any())).then(invocation -> null);

            boolean updated = Series.updateSeries(1, "unknownField", "value");
            assertFalse(updated, "Expected update to fail due to unknown field");
        }
    }

    @Test
    void testUpdateSeriesNotFound() {
        // Testing updateSeries for a series that does not exist
        try (MockedStatic<Save_Load> mocked = org.mockito.Mockito.mockStatic(Save_Load.class)) {
            mocked.when(Save_Load::load).thenReturn(testData);
            mocked.when(() -> Save_Load.save(org.mockito.ArgumentMatchers.<ArrayList<SeriesModel>>any()))
                    .then(invocation -> null);

            boolean updated = Series.updateSeries(99, "name", "Bleach");
            assertFalse(updated, "Expected update to fail for non-existent series");
        }
    }
    
    @Test
    void testDeleteSeriesSuccess() {
        // Testing deleteSeries for a series that exists
        try (MockedStatic<Save_Load> mocked = org.mockito.Mockito.mockStatic(Save_Load.class)) {
            // Prepare a modifiable copy for removeIf
            ArrayList<SeriesModel> modifiableList = new ArrayList<>(testData);
            mocked.when(Save_Load::load).thenReturn(modifiableList);
            mocked.when(() -> Save_Load.save(org.mockito.ArgumentMatchers.<ArrayList<SeriesModel>>any())).then(invocation -> null);

            boolean result = Series.deleteSeries(1);
            assertTrue(result, "Expected delete to succeed for existing series");

            // Ensure the series is removed
            assertNull(modifiableList.stream().filter(s -> s.getID() == 1).findFirst().orElse(null),
                "Series with ID 1 should be removed from the list");
        }
    }

    @Test
    void testDeleteSeriesNotFound() {
        // Testing deleteSeries for a series that does not exist
        try (MockedStatic<Save_Load> mocked = org.mockito.Mockito.mockStatic(Save_Load.class)) {
            ArrayList<SeriesModel> modifiableList = new ArrayList<>(testData);
            mocked.when(Save_Load::load).thenReturn(modifiableList);
            mocked.when(() -> Save_Load.save(org.mockito.ArgumentMatchers.<ArrayList<SeriesModel>>any())).then(invocation -> null);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                Series.deleteSeries(99);
            });
            assertEquals("Series with ID 99 not found.", exception.getMessage());
        }
    }

    @Test
    void testAgeRestrictionValidAges() {
        // Valid ages: should return true
        assertTrue(Series.ageRestriction(2), "Age 2 should be valid");
        assertTrue(Series.ageRestriction(10), "Age 10 should be valid");
        assertTrue(Series.ageRestriction(18), "Age 18 should be valid");
    }

    @Test
    void testAgeRestrictionInvalidAges() {
        // Invalid ages: should return false
        assertFalse(Series.ageRestriction(1), "Age 1 should be invalid");
        assertFalse(Series.ageRestriction(0), "Age 0 should be invalid");
        assertFalse(Series.ageRestriction(19), "Age 19 should be invalid");
        assertFalse(Series.ageRestriction(-5), "Negative ages should be invalid");
    }
}
