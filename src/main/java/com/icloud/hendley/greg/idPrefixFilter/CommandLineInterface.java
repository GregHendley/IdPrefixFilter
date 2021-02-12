package com.icloud.hendley.greg.idPrefixFilter;

import com.amihaiemil.eoyaml.exceptions.YamlIndentationException;
import com.icloud.hendley.greg.idPrefixFilter.exceptions.YamlPrefixesFileNameUndefined;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * CommandLineInterface is the command line interface
 * to the IdPrefixFilter application.
 * A normal command is just the ID to be matched.
 * The normal output is the ID followed by
 *     "matches" if the ID matches one of the prefixes
 *     "does not match" if the ID does not match any of the prefixes
 * The name of the file containing the prefixes may be specified.
 * This is useful in testing. In production it is preferable
 * to specify the prefixes file name in the system environment variable
 *     com.icloud.hendley.greg.idPrefixFilter.yamlPrefixesFileName
 * An alternate command is to print the prefixes.
 *
 * At present the command line interface exits at the end of each command.
 * Exit codes:
 *    0 : normal
 *    1 : unrecognized option in the command line
 *    2 : YamlPrefixesFileNameUndefined by env or -y
 *    3 : misformatted prefix file
 *   11 : an IOException reading the prefixes file
 *   12 : no prefix file name specified in the environment
 *
 */
public class CommandLineInterface {
    /**
     * The options for parsing and interpreting the command line
     */
    private Options options;

    /**
     * The parsed command line
     */
    private CommandLine commandLine;

    /**
     * Where output is printed, which allows the output
     * to go somethere other than System.out
     */
    private PrintWriter printWriter; //Allows unit test to read output.

    /**
     * The code to be returned
     */
    private int returnCode = 0;

    /**
     * The results or the error message to be written.
     */
    private String resultsOrErrorMessage = "";

    /**
     * The application for which this is a
     * command line interface.
     */
    private Application application;

    public static void main(String[] args) {
        CommandLineInterface instance = new CommandLineInterface();
        int status = instance.runWithArguments(args);
        System.exit(status);
    }

    public CommandLineInterface() {
        printWriter = new PrintWriter(System.out,true);
    }

    /**
     * Unit testing can set this to a print writer
     * so the test can read the command line output.
     *
     * @param printWriter the PrintWriter to be used in testing
     */
    void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    /**
     * Answer the PrintWriter containing the command line output.
     *
     * @return the printWriter containing the
     * command line output.
     */
    PrintWriter getPrintWriter() {
        return this.printWriter;
    }

    /**
     * The primary method called by main(String[])
     * This is an instance method to facilitate
     * unit testing of the command line interface.
     * Output is written to the printWriter.
     * When called from main(String[]) the printWriter
     * is on System.out. For unit testing the printWriter
     *
     * @param args
     * @return the exit code
     *    0 : normal
     *    1 : unrecognized option in the command line
     *    2 : YamlPrefixesFileNameUndefined by env or -y
     *    3 : misformatted prefix file
     *   11 : an IOException reading the prefixes file
     *   12 : no prefix file name specified in the environment
     */
    public int runWithArguments(String[] args) {
        initializeOptions();
        readCommandLineFromArgs(args);
        if (returnCode != 0) {
            printResults();
        }
        else {
            if (helpIsRequested()) {
                printHelp();
            } else {
                configure();
                if (returnCode == 0) {
                    if (printPrefixesIsRequested()) {
                        putPrefixesIntoResults();
                    }
                    else {
                        match();
                    }
                }
                printResults();
            }
        }
        return returnCode();
    }

    /**
     * Initialize the options for reading and
     * interpreting the command line.
     */
    private void initializeOptions() {
        options = new Options();
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Print this message.")
                .build() );
        options.addOption(Option.builder("p")
                .longOpt("printPrefixes")
                .desc("Print the prefixes with redundant prefixes omitted.")
                .build() );
        options.addOption(Option.builder("y")
                .longOpt("ymlConfigFileName")
                .argName("ymlConfigFileName")
                .desc("The name of the YAML file containing the list (allowed-id-prefixes:) of allowed ID prefixes.")
                .hasArg()
                .build() );
    }

    /**
     * Print help on the printWriter,
     * which in production is System.out
     */
    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String header = """
                Echos the id-to-filter followed on the same line by either of:
                    matched
                    does not match""";
        String footer = "";
        String syntax = "[-h] [-d] [-y <ymlConfigFileName>] <id-to-filter>";
        formatter.printHelp(printWriter,80,syntax,header,options,0,0, footer);
    }

    /**
     * Read and parse the args.
     * This initializes the private instance variable commandLine
     *
     * @param args from main
     */
    private void readCommandLineFromArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            setReturnCodeAndResultsOrErrorMessage(1, e.toString());
        }
    }

    /**
     * Configure this command line interface based on
     * the parsed command line.
     */
    private void configure() {
        initializeApplication();
    }

    /**
     * Answer if the command line requests
     * the prefixes to be printed.
     * @return true if prefixes are to be printed, false otherwise
     */
    private boolean printPrefixesIsRequested() {
        return commandLine.hasOption('p');
    }

    /**
     * Create a string of the prefixes, each on its own line
     * and make that the results.
     */
    private void putPrefixesIntoResults() {
        List<SegmentedString> prefixes = application.getPrefixes();
        StringBuilder stringBuilder = new StringBuilder();
        for (SegmentedString prefix : prefixes) {
            stringBuilder.append(prefix);
            stringBuilder.append('\n');
        }
        setReturnCodeAndResultsOrErrorMessage(0, stringBuilder.toString());
    }

    /**
     * Determine if the passed in ID matches any of the prefixes.
     * Make that the result.
     */
    private void match() {
        SegmentedString idToFilter = getIdToFilter();
        boolean matched = application.match(idToFilter);
        String results = idToFilter.toString() + " ";
        results += matched ? "matched" : "did not match";
        setReturnCodeAndResultsOrErrorMessage(0, results);
    }

    /**
     * Initialize the application, allowing for the command line
     * to specify the name of the prefix file.
     */
    private void initializeApplication() {
        if (isYamlConfigFileNameSpecified()) {
            String fileName = getYamlConfigFileName();
            initializeApplicationWithYamlConfigFileName(fileName);
        }
        else {
            initializeApplicationAssumingSystemEnvSpecifiesYamlConfigFileName();
        }
    }

    /**
     * Initialize the application with the prefix file name
     * from the command line.
     * @param yamlConfigFileName
     */
    private void initializeApplicationWithYamlConfigFileName(String yamlConfigFileName) {
        try {
            application = new Application(yamlConfigFileName);
        } catch (IOException ioException) {
            setReturnCodeAndResultsOrErrorMessage(
                    1,
                    ioException.toString()
            );
        } catch (YamlPrefixesFileNameUndefined yamlPrefixesFileNameUndefined) {
            setReturnCodeAndResultsOrErrorMessage(
                    2,
                    yamlPrefixesFileNameUndefined.toString()
            );
        } catch (YamlIndentationException yamlIndentationException) {
            setReturnCodeAndResultsOrErrorMessage(
                    3,
                    yamlIndentationException.toString()
            );
        }
    }

    /**
     * Initialize the application assuming the prefixes file name is specified
     * by an environment variable.
     */
    private void initializeApplicationAssumingSystemEnvSpecifiesYamlConfigFileName() {
        try {
            application = new Application();
        } catch (IOException ioE) {
            setReturnCodeAndResultsOrErrorMessage(
                    11,
                    ioE.toString()
            );
        } catch (YamlPrefixesFileNameUndefined yE) {
            setReturnCodeAndResultsOrErrorMessage(
                    12,
                    yE.toString()
            );
        }
    }

    /**
     * Return a SegmentedString from the first argument
     * in the command line. This is the string to match/filter
     *
     * @return the ID to filter as a SegmentedString
     */
    private SegmentedString getIdToFilter() {
         List<String> idStrings = commandLine.getArgList();
         String idString = idStrings.size() > 0 ? idStrings.get(0) : "";
         return new SegmentedString(idString);
    }

    /**
     * Return the name of the prefix file
     * specified on the command line.
     *
     * @return the name of the prefix file to use
     */
    private String getYamlConfigFileName() {
        return commandLine.getOptionValue('y');
    }

    /**
     * Answer true if a prefix file is specified
     * on the command line.
     *
     * @return true if a prefix file name is specified,
     * false otherwise
     */
    private boolean isYamlConfigFileNameSpecified() {
        return commandLine.hasOption('y');
    }

    /**
     * Print to the printwriter the result or error message string
     */
    private void printResults() {
        printWriter.println(resultsOrErrorMessage);
        if (returnCode() != 0) {
            printHelp();
        }
    }

    /**
     * Return the return code
     * @return the return code
     */
    private int returnCode() {
        return this.returnCode;
    }

    /**
     * Return true if help is requested
     * on the command line
     *
     * @return true if help is requested, false otherwise
     */
    private boolean helpIsRequested() {
        return commandLine != null && commandLine.hasOption('h');
    }

    /**
     * Set both the return code and the results or error message.
     * These should always be set together.
     *
     * @param returnCode the integer return code
     * @param resultsOrErrorMessage the results string or the error message
     */
    private void setReturnCodeAndResultsOrErrorMessage(int returnCode, String resultsOrErrorMessage) {
        this.returnCode = returnCode;
        this.resultsOrErrorMessage = resultsOrErrorMessage;
    }

}
