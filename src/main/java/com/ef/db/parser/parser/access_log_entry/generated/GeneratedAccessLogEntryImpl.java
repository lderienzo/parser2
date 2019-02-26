/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 8/15/18 12:29 PM
 */

package com.ef.db.parser.parser.access_log_entry.generated;

import com.ef.db.parser.parser.access_log_entry.AccessLogEntry;
import com.speedment.common.annotation.GeneratedCode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The generated base implementation of the {@link
 * com.ef.db.parser.parser.access_log_entry.AccessLogEntry}-interface.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public abstract class GeneratedAccessLogEntryImpl implements AccessLogEntry {
    
    private long id;
    private LocalDateTime date;
    private long ipAddress;
    private String request;
    private int status;
    private String userAgent;
    
    protected GeneratedAccessLogEntryImpl() {}
    
    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public LocalDateTime getDate() {
        return date;
    }
    
    @Override
    public long getIpAddress() {
        return ipAddress;
    }
    
    @Override
    public String getRequest() {
        return request;
    }
    
    @Override
    public int getStatus() {
        return status;
    }
    
    @Override
    public String getUserAgent() {
        return userAgent;
    }
    
    @Override
    public AccessLogEntry setId(long id) {
        this.id = id;
        return this;
    }
    
    @Override
    public AccessLogEntry setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }
    
    @Override
    public AccessLogEntry setIpAddress(long ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }
    
    @Override
    public AccessLogEntry setRequest(String request) {
        this.request = request;
        return this;
    }
    
    @Override
    public AccessLogEntry setStatus(int status) {
        this.status = status;
        return this;
    }
    
    @Override
    public AccessLogEntry setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
    
    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner(", ", "{ ", " }");
        sj.add("id = "        + Objects.toString(getId()));
        sj.add("date = "      + Objects.toString(getDate()));
        sj.add("ipAddress = " + Objects.toString(getIpAddress()));
        sj.add("request = "   + Objects.toString(getRequest()));
        sj.add("status = "    + Objects.toString(getStatus()));
        sj.add("userAgent = " + Objects.toString(getUserAgent()));
        return "AccessLogEntryImpl " + sj.toString();
    }
    
    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (!(that instanceof AccessLogEntry)) { return false; }
        final AccessLogEntry thatAccessLogEntry = (AccessLogEntry)that;
        if (this.getId() != thatAccessLogEntry.getId()) { return false; }
        if (!Objects.equals(this.getDate(), thatAccessLogEntry.getDate())) { return false; }
        if (this.getIpAddress() != thatAccessLogEntry.getIpAddress()) { return false; }
        if (!Objects.equals(this.getRequest(), thatAccessLogEntry.getRequest())) { return false; }
        if (this.getStatus() != thatAccessLogEntry.getStatus()) { return false; }
        if (!Objects.equals(this.getUserAgent(), thatAccessLogEntry.getUserAgent())) { return false; }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Long.hashCode(getId());
        hash = 31 * hash + Objects.hashCode(getDate());
        hash = 31 * hash + Long.hashCode(getIpAddress());
        hash = 31 * hash + Objects.hashCode(getRequest());
        hash = 31 * hash + Integer.hashCode(getStatus());
        hash = 31 * hash + Objects.hashCode(getUserAgent());
        return hash;
    }
}