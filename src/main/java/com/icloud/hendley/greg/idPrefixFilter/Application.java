package com.icloud.hendley.greg.idPrefixFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlInput;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlSequence;

public class Application {
    static public final String SYSTEM_ENV_KEY_YAML_PREFIXES_FILE_NAME
            = "com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName";

    private String yamlPrefixesFileName;
    private SegmentedStringPrefixMatcher matcher;

    public Application() throws IOException {
        matcher = new SegmentedStringPrefixMatcher();
        initializePrefixes();
    }

    public Application(String yamlPrefixesFileName) throws IOException {
        this.yamlPrefixesFileName = yamlPrefixesFileName;
        matcher = new SegmentedStringPrefixMatcher();
        initializePrefixes();
    }

    public boolean match(SegmentedString toMatch) {
        return matcher.match(toMatch);
    }

    public List<SegmentedString> getPrefixes() {
        return matcher.getPrefixes();
    }

    private void initializePrefixes() throws IOException {
        for (String prefixString : readPrefixStrings()) {
            matcher.add(new SegmentedString(prefixString));
        }
    }

    private List<String> readPrefixStrings() throws IOException {
        return prefixStringsFromYamlFile(getYamlPrefixesFile());
    }

    private File getYamlPrefixesFile() {
        return new File(getYamlPrefixesFileName());
    }

    private String getYamlPrefixesFileName() {
        if (yamlPrefixesFileName != null) {
            return yamlPrefixesFileName;
        }
        else {
            return System.getenv(SYSTEM_ENV_KEY_YAML_PREFIXES_FILE_NAME);
        }
    }

    private List<String> prefixStringsFromYamlFile(File yamlFile) throws IOException {
        List<String> prefixStrings = new ArrayList<>();
        YamlInput yamlInput = Yaml.createYamlInput(yamlFile);
        YamlMapping yamlMapping = yamlInput.readYamlMapping();
        YamlSequence prefixesYamlSequence = yamlMapping.yamlSequence("id-prefix");
        if (prefixesYamlSequence != null) {
            for (int i = 0; i < prefixesYamlSequence.size(); i++) {
                prefixStrings.add(prefixesYamlSequence.string(i));
            }
        }
        return prefixStrings;
    }

}
