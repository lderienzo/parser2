package com.ef.parser;

public class BlockedIP {
 // TODO: should correspond to blocked IP table
    private String IP;
    private String comment;

    public BlockedIP(String IP, String comment) {
        this.IP = IP;
        this.comment = comment;
    }

    @Override
    public String toString() {
        // TODO: use String.format()? I see the pros using it, why?
        return "IP [" + IP + "] was blocked for the following reason: [" + comment + "]";
    }
}
