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
            Echos the id-to-filter and state if it matched.
            -d,--debug                                log all messages
            -h,--help                                 print this message
            -y,--ymlConfigFileName <ymlConfigFileName>The name of the YAML file containing
                                                      the list (allowed-id-prefixes:) of
                                                      allowed ID prefixes
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

    @Test
    void testHelpShort() {
        int status = commandLineInterface.runWithArguments(new String[] {"-h"});
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        assertEquals(expectedHelpString, output, "Output");
    }

    @Test
    void testHelpLong() {
        int status = commandLineInterface.runWithArguments(new String[] {"--help"});
        assertEquals(0, status, "status");
        String output = outputStream.toString();
        assertEquals(expectedHelpString, output);
    }


}
