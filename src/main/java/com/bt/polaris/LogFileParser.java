package com.bt.polaris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

class LogFileParser {
    public static Map<String, LogEntry> parse(String logFilePath) throws IOException, LogParseException {
        Map<String, LogEntry> logEntries = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] record = line.split("\\s+");
                if (record.length != 3) {
                    throw new LogParseException("Invalid log entry format: " + line);
                }

                String timeStr = record[0];
                String username = record[1];
                String status = record[2];

                if (!isValidUsername(username)) {
                    throw new LogParseException("Invalid username: " + username);
                }

                LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                logEntries.put(username, new LogEntry(time, status));
            }
        }

        return logEntries;
    }

    private static boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]*$");
    }
}

