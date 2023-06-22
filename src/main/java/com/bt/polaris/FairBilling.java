package com.bt.polaris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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
        String result = "";

        Map<String, Integer> sessionCounts = new HashMap<>();
        Map<String, Long> sessionDurations = new HashMap<>();
        Map<String, Stack<LocalTime>> sessionStacks = new HashMap<>();

        LocalTime earliestTime = null;
        LocalTime latestTime = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] record = line.split(" ");
                    if (record.length == 3) {
                        String timeStr = record[0];
                        String username = record[1];
                        String status = record[2];

                        try {
                            LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));

                            if (earliestTime == null) earliestTime = time;
                            if (latestTime == null || time.isAfter(latestTime)) latestTime = time;

                            if (status.equals("Start")) {
                                sessionCounts.put(username, sessionCounts.getOrDefault(username, 0) + 1);
                                sessionStacks.computeIfAbsent(username, k -> new Stack<>()).push(time);

                            } else if (status.equals("End")) {
                                Stack<LocalTime> stack = sessionStacks.get(username);
                                if (stack != null && !stack.isEmpty()) {
                                    long duration = sessionDurations.getOrDefault(username, 0L) + Duration.between(stack.pop(), time).getSeconds();
                                    sessionDurations.put(username, duration);
                                }
                                else{  // if there is END with no matching Start
                                    sessionCounts.put(username, sessionCounts.getOrDefault(username, 0) + 1);
                                    long duration = sessionDurations.getOrDefault(username, 0L) + Duration.between(earliestTime, time).getSeconds();
                                    sessionDurations.put(username, duration);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(" It was not read correctly: " + e);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the log file: " + e.getMessage());
        }

        // When there were START with no matching END
        for (String username : sessionStacks.keySet()) {
            Stack<LocalTime> stack = sessionStacks.get(username);
            while (!stack.isEmpty()) {
                long duration = sessionDurations.getOrDefault(username, 0L) + Duration.between(stack.pop(), latestTime).getSeconds();
                sessionDurations.put(username, duration);
            }
        }

        // Prepare the string message to display
        for (String username : sessionCounts.keySet()) {
            int sessionCount = sessionCounts.get(username);
            long sessionDuration = sessionDurations.getOrDefault(username, 0L);
            result += String.format("%s %d %d%n", username, sessionCount, sessionDuration);
        }

        return result;
    }
}