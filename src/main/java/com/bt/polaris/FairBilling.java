package com.bt.polaris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FairBilling {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the path to the log file as a command line argument.");
            return;
        }

        String logFilePath = args[0];
        String result = processLogFile(logFilePath);
        System.out.println(result);
    }

    public static String processLogFile(String logFilePath) {
        LocalTime earliestTime = null;
        LocalTime latestTime = null;
        SessionData SessionData = new SessionData();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] record = line.split(" ");
                if (record.length != 3) {
                    continue;
                }

                String timeStr = record[0];
                String username = record[1];
                String status = record[2];

                try {
                    LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));

                    if (earliestTime == null) earliestTime = time;
                    if (latestTime == null || time.isAfter(latestTime)) latestTime = time;

                    if (status.equals("Start")) {
                        SessionData.addStart(username, time);
                    } else if (status.equals("End")) {
                        SessionData.addEnd(username, time, earliestTime);
                    }
                } catch (Exception e) {}
            }
        } catch (IOException e) {
            System.out.println("Error reading the log file: " + e.getMessage());
        }

        // When there were START with no matching END
        SessionData.addExtraStart(latestTime);

        return SessionData.formatResult();
    }
}

