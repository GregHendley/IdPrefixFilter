package com.icloud.hendley.greg.idPrefixFilter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandLineInterfaceTest {

    static final String expectedHelpString = """
            usage: [-h] [-d] [-y <ymlConfigFileName>] <id-to-filter>
            Echos the id-to-filter followed on the same line by either of:
                matched
                does not match
            -h,--help                                 Print this message.
            -p,--printPrefixes                        Print the prefixes with redundant 
                                                      prefixes omitted.
            -y,--ymlConfigFileName <ymlConfigFileName>The name of the YAML file containing
                                                      the list (allowed-id-prefixes:) of
                                                      allowed ID prefixes.
            """;

    CommandLineInterface commandLineInterface;
    ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        commandLineInterface = new CommandLineInterface();
        outputStream = new ByteArrayOutputStream();
        commandLineInterface.setPrintWriter(new PrintWriter(outputStream, true));
    }

    @AfterEach
    void tearDown() {
        commandLineInterface = null;
        outputStream = null;
    }

    /*
     * return code 0
     */
    @Test
    void testHelpShort() {
        int status = commandLineInterface.runWithArguments(new String[] {"-h"});
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        assertEquals(expectedHelpString, output, "Output");
    }

    /*
     * return code 0
     */
    @Test
    void testHelpLong() {
        int status = commandLineInterface.runWithArguments(new String[] {"--help"});
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        assertEquals(expectedHelpString, output);
    }

    /*
     * return code 1
     */
    @Test
    void testBadOptions() {
        String[] args = "--badOption".split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(1, status, "status");
        String output = outputStream.toString();
        String expected = ""
                + "org.apache.commons.cli.UnrecognizedOptionException: "
                + "Unrecognized option: --badOption\n"
                + expectedHelpString;
        assertEquals(expected, output);
    }

    /*
     * return code 2
     *
     * This can be tested in a container where the
     * prefixes file is made unreadable.
     * See rc2YamlPrefixesFileNameUndefined.dockerfile
     * and its comments.
     */

     /*
     * return code 3
     */
    @Test
    void testMatchMisformattedConfigFile() {
        String argsString = "-y " +
                "./src/test/resources/misformattedPrefixes.yaml " +
                "1.2.3.4.5";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(3, status, "status");
        String output = outputStream.toString();
        String expected = "" +
                "com.amihaiemil.eoyaml.exceptions.YamlIndentationException: " +
                "Indentation of line 3 [40.50] is greater than " +
                "the one of line 2 [- 1.2.3]. " +
                "It should be less or equal.\n" +
                expectedHelpString;
        assertEquals(expected,output);
    }

    /*
     * return code 11
     *
     * See rc2YamlPrefixesFileNameUndefined.dockerfile
     * and its comments for how to test.
     *
     * See onlyCommandLineSpecifiesPrefixes.dockerfile
     * for a contrast.
     */

    /*
     * return code 12
     */
    @Test
    void testMatchNoYamlConfigFileNameNoSystemEnvVar() {
        String[] args = "1.2.3.4".split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(12, status, "status");
        String output = outputStream.toString();
        String expected = "" +
                "com.icloud.hendley.greg.idPrefixFilter.exceptions.YamlPrefixesFileNameUndefined: " +
                "The environment variable com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName " +
                "was not defined.\n" +
                expectedHelpString;
        assertEquals(expected,output);
    }

    /*
     * return code 0
     */
    @Test
    void testMatchEmptyListInConfigFile() {
        String argsString = "-y " +
                "./src/test/resources/emptyPrefixList.yaml " +
                "1.2.3.4.5";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        String expected = "1.2.3.4.5 did not match\n";
        assertEquals(expected,output);
    }

    /*
     * return code 0
     */
    @Test
    void testMatchMissingListInConfigFile() {
        String argsString = "-y " +
                "./src/test/resources/missingPrefixList.yaml " +
                "1.2.3.4.5";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        String expected = "1.2.3.4.5 did not match\n";
        assertEquals(expected,output);
    }

    /*
     * return code 0
     */
    @Test
    void testMatchSpecifyingYamlConfigFileName() {
        String argsString = "-y " +
                "./src/test/resources/testPrefixes1.yaml " +
                "1.2.3.4.5";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        String expected = "" +
                "1.2.3.4.5 matched\n";
        assertEquals(expected,output);
    }

    /*
     * return code 0
     */
    @Test
    void testMatchTooShort() {
        String argsString = "-y " +
                "./src/test/resources/testPrefixes1.yaml " +
                "1.2";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        String expected = "" +
                "1.2 did not match\n";
        assertEquals(expected,output);
    }

    /*
     * return code 0
     */
    @Test
    void testMatchSlightlyDifferent() {
        String argsString = "-y " +
                "./src/test/resources/testPrefixes1.yaml " +
                "1.2.c";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        String expected = "" +
                "1.2.c did not match\n";
        assertEquals(expected,output);
    }

    /*
     * return code 0
     */
    @Test
    void testMatchExact() {
        String argsString = "-y " +
                "./src/test/resources/testPrefixes1.yaml " +
                "1.2.3";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        String expected = "" +
                "1.2.3 matched\n";
        assertEquals(expected,output);
    }

    /*
     * return code 0
     */
    @Test
    void testMatchNormalFirstPrefix() {
        String argsString = "-y " +
                "./src/test/resources/testPrefixes1.yaml " +
                "1.2.3.one.april";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        String expected = "" +
                "1.2.3.one.april matched\n";
        assertEquals(expected,output);
    }

    /*
     * return code 0
     */
    @Test
    void testMatchNormalSecondPrefix() {
        String argsString = "-y " +
                "./src/test/resources/testPrefixes1.yaml " +
                "40.50.60.80";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        String expected = "" +
                "40.50.60.80 matched\n";
        assertEquals(expected,output);
    }

    /*
     * return code 0
     */
    @Test
    void testMatchNormalThirdPrefix() {
        String argsString = "-y " +
                "./src/test/resources/testPrefixes1.yaml " +
                "600.100.900";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        String expected = "" +
                "600.100.900 matched\n";
        assertEquals(expected,output);
    }

    @Test
    void testPrintPrefixesEmpty() {
        String argsString = "-p " +
                "-y " +
                "./src/test/resources/emptyPrefixList.yaml ";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String expected = "\n";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    void testPrintPrefixesMissing() {
        String argsString = "-p " +
                "-y " +
                "./src/test/resources/missingPrefixList.yaml ";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String expected = "\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * return code 12
     */
    @Test
    void testPrintPrefixesNoYamlConfigFileNameNoSystemEnvVar() {
        String argsString = "-p ";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(12, status, "status");
        String expected = "com.icloud.hendley.greg.idPrefixFilter.exceptions.YamlPrefixesFileNameUndefined: " +
                "The environment variable com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName " +
                "was not defined.\n" +
                expectedHelpString;
        assertEquals(expected, outputStream.toString());
    }

    /*
     * return code 3
     */
    @Test
    void testPrintPrefixesMisformatted() {
        String argsString = "-p " +
                "-y " +
                "./src/test/resources/misformattedPrefixes.yaml ";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(3, status, "status");
        String expected = "com.amihaiemil.eoyaml.exceptions.YamlIndentationException: " +
                "Indentation of line 3 [40.50] is greater than the one of line 2 [- 1.2.3]. " +
                "It should be less or equal.\n" +
                expectedHelpString;
        assertEquals(expected, outputStream.toString());
    }

    @Test
    void testPrintPrefixesNormal() {
        String argsString = "-p " +
                "-y " +
                "./src/test/resources/testPrefixes1.yaml ";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String expected = """
                1.2.3
                40.50
                600

                """;
        assertEquals(expected, outputStream.toString());
    }

    @Test
    void testPrintPrefixesRedundant() {
        String argsString = "-p " +
                "-y " +
                "./src/test/resources/redundantPrefixes.yaml ";
        String[] args = argsString.split(" ");
        int status = commandLineInterface.runWithArguments(args);
        assertEquals(0, status, "status");
        String expected = """
                1.2.3

                """;
        assertEquals(expected, outputStream.toString());
    }

}
