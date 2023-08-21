package com.bt.polaris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

class LogFileProcessor {
    private LocalTime earliestTime = null;
    private LocalTime latestTime = null;
    private Map<String, PersonSessionData> users = new HashMap<>();

    public void process(String logFilePath) throws IOException, LogParseException {
        Map<String, LogEntry> logEntries = LogFileParser.parse(logFilePath);

        for (Map.Entry<String, LogEntry> entry : logEntries.entrySet()) {
            String username = entry.getKey();
            LogEntry logEntry = entry.getValue();

            updateEarliestAndLatestTime(logEntry.getTime());
            users.putIfAbsent(username, new PersonSessionData());
            PersonSessionData userData = users.get(username);

            if (logEntry.getStatus().equals("Start")) {
                userData.addStart(logEntry.getTime());
            } else if (logEntry.getStatus().equals("End")) {
                userData.addEnd(logEntry.getTime(), earliestTime);
            }
        }
    }

    public void printSessionData() {
        for (Map.Entry<String, PersonSessionData> user : users.entrySet()) {
            String username = user.getKey();
            PersonSessionData userData = user.getValue();
            userData.addExtraStart(latestTime);
            System.out.println(String.format("%s %s", username, userData.outputResult()));
        }
    }

    private void updateEarliestAndLatestTime(LocalTime time) {
        if (earliestTime == null) {
            earliestTime = time;
        }
        if (latestTime == null || time.isAfter(latestTime)) {
            latestTime = time;
        }
    }
}