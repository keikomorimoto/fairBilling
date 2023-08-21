package com.bt.polaris;

import java.io.IOException;

public class FairBilling {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the path to the log file as a command line argument.");
            return;
        }
        String logFilePath = args[0];
        processLogFile(logFilePath);
    }

    public static void processLogFile(String logFilePath) {
        LogFileProcessor processor = new LogFileProcessor();
        try {
            processor.process(logFilePath);
            processor.printSessionData();
        } catch (IOException e) {
            System.out.println("Error reading the log file: \" + e.getMessage())");
        } catch (LogParseException e) {
            throw new RuntimeException(e);
        }
    }
}

