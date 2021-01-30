package com.icloud.hendley.greg.idPrefixFilter;

import com.amihaiemil.eoyaml.exceptions.YamlIndentationException;
import com.icloud.hendley.greg.idPrefixFilter.exceptions.YamlPrefixesFileNameUndefined;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.NoSuchElementException;

public class CommandLineInterface {
    Options options;
    CommandLine commandLine;
    PrintWriter printWriter; //Allows unit test to read output.
    int returnCode = 0;
    String resultsOrErrorMessage = "";
    private boolean matched;

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
                run();
                printResults();
            }
        }
        return returnCode();
    }

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

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String header = "Echos the id-to-filter and state if it matched.";
        String footer = "";
        String syntax = "[-h] [-d] [-y <ymlConfigFileName>] <id-to-filter>";
        formatter.printHelp(printWriter,80,syntax,header,options,0,0, footer);
    }

    private void readCommandLineFromArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            setReturnCodeAndResultsOrErrorMessage(1, e.toString());
        }
    }

    private void run() {
        Application application = getInitializedApplication();
        if (application != null && returnCode == 0) {
            SegmentedString idToFilter = getIdToFilter();
            boolean matched = application.match(idToFilter);
            String results = idToFilter.toString() + " ";
            results += matched ? "matched" : "did not match";
            setReturnCodeAndResultsOrErrorMessage(0, results);
        }
    }

    private Application getInitializedApplication() {
        Application application = null;
        if (isYamlConfigFileNameSpecified()) {
            String fileName = getYamlConfigFileName();
            application = getApplicationWithYamlConfigFileName(fileName);
        }
        else {
            application = getApplicationAssumingSystemEnvSpecifiesYamlConfigFileName();
        }
        return application;
    }

    private Application getApplicationWithYamlConfigFileName(String yamlConfigFileName) {
        Application application = null;
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
        return application;
    }

    private Application getApplicationAssumingSystemEnvSpecifiesYamlConfigFileName() {
        Application application = null;
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
        return application;
    }

    private SegmentedString getIdToFilter() {
         List<String> idStrings = commandLine.getArgList();
         String idString = idStrings.size() > 0 ? idStrings.get(0) : "";
         return new SegmentedString(idString);
    }

    private String getYamlConfigFileName() {
        return commandLine.getOptionValue('y');
    }

    private boolean isYamlConfigFileNameSpecified() {
        return commandLine.hasOption('y');
    }

    private void printResults() {
        printWriter.println(resultsOrErrorMessage);
        if (returnCode() != 0) {
            printHelp();
        }
    }

    private int returnCode() {
        return this.returnCode;
    }

    private boolean helpIsRequested() {
        return commandLine != null && commandLine.hasOption('h');
    }

    private void setReturnCodeAndResultsOrErrorMessage(int returnCode, String resultsOrErrorMessage) {
        this.returnCode = returnCode;
        this.resultsOrErrorMessage = resultsOrErrorMessage;
    }

}
