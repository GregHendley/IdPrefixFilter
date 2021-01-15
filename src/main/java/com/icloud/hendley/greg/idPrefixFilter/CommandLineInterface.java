package com.icloud.hendley.greg.idPrefixFilter;

import org.apache.commons.cli.*;

import java.io.PrintWriter;

public class CommandLineInterface {
    Options options;
    CommandLine commandLine;
    PrintWriter printWriter; //Allows unit test to read output.
    int returnCode = 0;
    String resultsOrErrorMessage = "";

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
     * @param printWriter
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
        configure();
        if (helpIsNeeded()) {
            printHelp();
        }
        else {
            run();
            printResults();
        }
        return returnCode();
    }

    private void initializeOptions() {
        options = new Options();
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("print this message")
                .build() );
        options.addOption(Option.builder("d")
                .longOpt("debug")
                .desc("log all messages")
                .build() );
        options.addOption(Option.builder("y")
                .longOpt("ymlConfigFileName")
                .argName("ymlConfigFileName")
                .desc("The name of the YAML file containing the list (allowed-id-prefixes:) of allowed ID prefixes")
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

    private void configure() {

    }

    private void run() {

    }

    private void printResults() {

    }

    private int returnCode() {
        return this.returnCode;
    }

    private boolean helpIsNeeded() {
        boolean answer = false;
        answer = answer || returnCode() != 0;
        answer = answer || options.hasOption("h");
        return answer;
    }

    private void setReturnCodeAndResultsOrErrorMessage(int returnCode, String resultsOrErrorMessage) {
        this.returnCode = returnCode;
        this.resultsOrErrorMessage = resultsOrErrorMessage;
    }

}
