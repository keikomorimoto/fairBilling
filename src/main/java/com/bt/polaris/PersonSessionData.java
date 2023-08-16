package com.bt.polaris;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Stack;

public class PersonSessionData {
    Integer sessionCounts = 0;
    Long sessionDurations = 0L;
    Stack<LocalTime> sessionStacks = new Stack<>();

    public void addStart(LocalTime time) {
        sessionCounts++;
        sessionStacks.push(time);
    }

    public void addEnd(LocalTime time, LocalTime earliestTime) {
        if (sessionStacks != null && !sessionStacks.isEmpty()) {
            sessionDurations += Duration.between(sessionStacks.pop(), time).getSeconds();

        } else {  // if there is END with no matching Start
            sessionCounts++;
            sessionDurations += Duration.between(earliestTime, time).getSeconds();
        }
    }

    public void addExtraStart(LocalTime latestTime) {
        while (!sessionStacks.isEmpty()) {
            sessionDurations += Duration.between(sessionStacks.pop(), latestTime).getSeconds();
        }
    }
    public String outputResult() {
        return String.format("%d %d", sessionCounts, sessionDurations);
    }
}
