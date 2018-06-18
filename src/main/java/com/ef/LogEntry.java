package com.ef;


public class LogEntry {
    private String date;
    private String IP;
    private String request;
    private String userAgent;

    public LogEntry(String date, String IP, String request, String userAgent) {
        this.date = date;
        this.IP = IP;
        this.request = request;
        this.userAgent = userAgent;
    }

}
