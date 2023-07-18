package com.bt.polaris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
        LocalTime earliestTime = null;
        LocalTime latestTime = null;
        Map<String, PersonSessionData> Users = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] record = line.split("\\s+");
                if (record.length != 3) {
                    continue;
                }

                String timeStr = record[0];
                String username = record[1];
                String status = record[2];

                if (!username.matches("^[a-zA-Z0-9]*$")) { //check single alphanumeric string
                    continue;
                }

                try {
                    LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));

                    if (earliestTime == null) earliestTime = time;
                    if (latestTime == null || time.isAfter(latestTime)) latestTime = time;

                    Users.putIfAbsent(username, new PersonSessionData());
                    PersonSessionData userData = Users.get(username);

                    if (status.equals("Start")) {
                        userData.addStart(time);
                    } else if (status.equals("End")) {
                        userData.addEnd(time, earliestTime);
                    }
                } catch (Exception e) {
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the log file: " + e.getMessage());
        }

        for (Map.Entry<String, PersonSessionData> user : Users.entrySet()) {
            String username = user.getKey();
            PersonSessionData userPersonSessionData = user.getValue();

            userPersonSessionData.addExtraStart(latestTime); // When there were START with no matching END

            System.out.println(username + " " + userPersonSessionData.outputResult());
        }
    }
}

