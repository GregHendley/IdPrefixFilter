package com.icloud.hendley.greg.idPrefixFilter;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlInput;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlSequence;
import com.amihaiemil.eoyaml.exceptions.YamlIndentationException;
import com.icloud.hendley.greg.idPrefixFilter.exceptions.YamlPrefixesFileNameUndefined;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Application {
    static public final String SYSTEM_ENV_KEY_YAML_PREFIXES_FILE_NAME
            = "com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName";

    private String yamlPrefixesFileName;
    private final SegmentedStringPrefixMatcher matcher;

    public Application() throws IOException, YamlPrefixesFileNameUndefined, YamlIndentationException {
        matcher = new SegmentedStringPrefixMatcher();
        initializePrefixes();
    }

    public Application(String yamlPrefixesFileName)
            throws IOException, YamlPrefixesFileNameUndefined, YamlIndentationException
    {
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

    private void initializePrefixes()
            throws IOException, YamlPrefixesFileNameUndefined {
        for (String prefixString : readPrefixStrings()) {
            matcher.add(new SegmentedString(prefixString));
        }
    }

    private List<String> readPrefixStrings()
            throws IOException, YamlPrefixesFileNameUndefined {
        return prefixStringsFromYamlFile(getYamlPrefixesFile());
    }

    private File getYamlPrefixesFile() throws YamlPrefixesFileNameUndefined {
        return new File(getYamlPrefixesFileName());
    }

    private String getYamlPrefixesFileName() throws YamlPrefixesFileNameUndefined {
        String answer;
        if (yamlPrefixesFileName != null) {
            answer = yamlPrefixesFileName;
        }
        else {
            answer = System.getenv(SYSTEM_ENV_KEY_YAML_PREFIXES_FILE_NAME);
            if (answer == null) {
                String message = "The environment variable " +
                        SYSTEM_ENV_KEY_YAML_PREFIXES_FILE_NAME +
                        " was not defined.";
                throw new YamlPrefixesFileNameUndefined(message);
            }
        }
        return answer;
    }

    private List<String> prefixStringsFromYamlFile(File yamlFile) throws IOException {
        List<String> prefixStrings = new ArrayList<>();
        YamlInput yamlInput = Yaml.createYamlInput(yamlFile);
        YamlMapping yamlMapping = yamlInput.readYamlMapping();
        YamlSequence prefixesYamlSequence;
        try {
            prefixesYamlSequence = yamlMapping.yamlSequence("id-prefix");
        } catch (NoSuchElementException e) {//the list existed but had no elements
            prefixesYamlSequence = null;
        }
        if (prefixesYamlSequence != null) {
            for (int i = 0; i < prefixesYamlSequence.size(); i++) {
                prefixStrings.add(prefixesYamlSequence.string(i));
            }
        }
        return prefixStrings;
    }

}
