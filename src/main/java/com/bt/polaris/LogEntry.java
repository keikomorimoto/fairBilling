package com.bt.polaris;

import java.time.LocalTime;

class LogEntry {
    private final LocalTime time;
    private final String status;

    public LogEntry(LocalTime time, String status) {
        this.time = time;
        this.status = status;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}

