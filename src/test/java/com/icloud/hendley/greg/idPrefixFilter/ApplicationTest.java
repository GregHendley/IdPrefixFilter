package com.icloud.hendley.greg.idPrefixFilter;

import com.icloud.hendley.greg.idPrefixFilter.exceptions.YamlPrefixesFileNameUndefined;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {

    private Application application;

    @BeforeEach
    public void setup() {
    }

    @AfterEach
    public void teardown() {
        application = null;
    }

    @Test
    public void testConstructorWhenSystemEnvIsMissingForYamlPrefixesFile() {
        Exception exception = assertThrows(
                YamlPrefixesFileNameUndefined.class,
                Application::new);
        String expected = "The environment variable " +
                "com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName " +
                "was not defined.";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testMatchWhenNonexistentYamlPrefixesFileIsSpecified() {
        Exception exception = assertThrows(IOException.class,
                () -> new Application("nonexistent.yaml")
                );
        String expected = "nonexistent.yaml (No such file or directory)";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testSystemEnvKeyYamlPrefixesFileName() {
        String expected = "com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName";
        String actual = Application.SYSTEM_ENV_KEY_YAML_PREFIXES_FILE_NAME;
        assertEquals(expected, actual);
    }

    @Test
    public void testReadPrefixesFromSpecifiedFile() throws IOException, YamlPrefixesFileNameUndefined {
        application = new Application("src/test/resources/testPrefixes1.yaml");
        List<SegmentedString> actual = application.getPrefixes();
        assertNotNull(actual, "list of prefixes is not null");
        List<SegmentedString> expected = new ArrayList<>();
        expected.add(new SegmentedString("1.2.3"));
        expected.add((new SegmentedString("40.50")));
        expected.add((new SegmentedString("600")));
        assertEquals(3, actual.size());
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void testMatch() throws IOException, YamlPrefixesFileNameUndefined {
        application = new Application("src/test/resources/testPrefixes1.yaml");
        SegmentedString toMatch = new SegmentedString("1.2.3.4.5");
        assertTrue(application.match(toMatch));
    }

    @Test
    public void testMatch2() throws IOException, YamlPrefixesFileNameUndefined {
        application = new Application("src/test/resources/testPrefixes1.yaml");
        SegmentedString toMatch = new SegmentedString("600.700");
        assertTrue(application.match(toMatch));
    }

    @Test
    public void testMatchExact() throws IOException, YamlPrefixesFileNameUndefined {
        application = new Application("src/test/resources/testPrefixes1.yaml");
        SegmentedString toMatch = new SegmentedString("1.2.3");
        assertTrue(application.match(toMatch));
    }

    @Test
    public void testMatchSuffixFail() throws IOException, YamlPrefixesFileNameUndefined {
        application = new Application("src/test/resources/testPrefixes1.yaml");
        SegmentedString toMatch = new SegmentedString("10.20.30.40.50");
        assertFalse(application.match(toMatch));
    }

}
