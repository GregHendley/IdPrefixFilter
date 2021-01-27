package com.icloud.hendley.greg.idPrefixFilter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationTest {

    private Application application;

    @BeforeEach
    public void setup() {
    }

    @AfterEach
    public void teardown() {
    }

    @Test
    public void testMatchWhenSystemEnvIsMissingForYamlPrefixesFile() {
        fail("Not yet implemented");
    }

    @Test
    public void testMatchWhenSystemEnvSpecifiesNoYamlPrefixesFile() {
        fail("Not yet implemented");
    }

    @Test
    public void testMatchWhenSystemEnvSpecifiesNonExistantYamlPrefixesFile() {
        fail("Not yet implemented");
    }

    @Test
    public void testMatchUsingSimpleYamlPrefixesFileFromSystemEnv() {
        fail("Not yet implemented");
    }

    @Test
    public void testMatchUsingMediumYamlPrefixesFileFromSystemEnv() {
        fail("Not yet implemented");
    }

    @Test
    public void testSystemEnvKeyYamlPrefixesFileName() {
        String expected = "com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName";
        String actual = Application.SYSTEM_ENV_KEY_YAML_PREFIXES_FILE_NAME;
        assertEquals(expected, actual);
    }

    @Test
    public void testReadPrefixesFromDefaultFile() {
        List<SegmentedString> actual = application.getPrefixes();
        assertNotNull(actual, "list of prefixes is not null");
        List<SegmentedString> expected = new ArrayList<>();
        expected.add(new SegmentedString("a.b.c"));
        expected.add((new SegmentedString("i.j")));
        expected.add((new SegmentedString("z")));
        assertEquals(3, actual.size());
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void testReadPrefixesFromSpecifiedFile() throws IOException {
        application = new Application("test/resources/testPrefixes1.yaml");
        List<SegmentedString> actual = application.getPrefixes();
        assertNotNull(actual, "list of prefixes is not null");
        List<SegmentedString> expected = new ArrayList<>();
        expected.add(new SegmentedString("1.2.3"));
        expected.add((new SegmentedString("40.50")));
        expected.add((new SegmentedString("600")));
        assertEquals(3, actual.size());
        assertTrue(actual.containsAll(expected));

    }

}
