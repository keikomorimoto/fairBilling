package com.bt.polaris;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SessionData {

    Map<String, Integer> sessionCounts = new HashMap();
    Map<String, Long> sessionDurations = new HashMap();
    Map<String, Stack<LocalTime>> sessionStacks = new HashMap();

    public void addStart(String username, LocalTime time) {
        sessionCounts.put(username, sessionCounts.getOrDefault(username, 0) + 1);
        sessionStacks.computeIfAbsent(username, k -> new Stack<>()).push(time);
    }


    public void addEnd(String username, LocalTime time, LocalTime earliestTime) {
        Stack<LocalTime> stack = sessionStacks.get(username);
        if (stack != null && !stack.isEmpty()) {
            long duration = sessionDurations.getOrDefault(username, 0L) + Duration.between(stack.pop(), time).getSeconds();
            sessionDurations.put(username, duration);
        } else {  // if there is END with no matching Start
            sessionCounts.put(username, sessionCounts.getOrDefault(username, 0) + 1);
            long duration = sessionDurations.getOrDefault(username, 0L) + Duration.between(earliestTime, time).getSeconds();
            sessionDurations.put(username, duration);
        }
    }

    public void addExtraStart(LocalTime latestTime) {
        for (String username : sessionStacks.keySet()) {
            Stack<LocalTime> stack = sessionStacks.get(username);
            while (!stack.isEmpty()) {
                long duration = sessionDurations.getOrDefault(username, 0L) + Duration.between(stack.pop(), latestTime).getSeconds();
                sessionDurations.put(username, duration);
            }
        }
    }

    public String formatResult(){
        // Prepare the string message to display
        String result = "";
        for (String username : sessionCounts.keySet()) {
            int sessionCount = sessionCounts.get(username);
            long sessionDuration = sessionDurations.getOrDefault(username, 0L);
            result += String.format("%s %d %d%n", username, sessionCount, sessionDuration);
        }
        return result;


    }
}
