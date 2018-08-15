/*
 * Created by Luke DeRienzo on 8/15/18 12:29 PM
 * Copyright (c) 2018. All rights reserved
 *
 * Last modified: 6/18/18 4:20 PM
 */

package com.ef.db.parser.parser.access_log_entry.generated;

import com.ef.db.parser.parser.access_log_entry.AccessLogEntry;
import com.ef.db.parser.parser.access_log_entry.AccessLogEntryManager;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.core.manager.AbstractManager;
import com.speedment.runtime.field.Field;
import java.util.stream.Stream;

/**
 * The generated base implementation for the manager of every {@link
 * com.ef.db.parser.parser.access_log_entry.AccessLogEntry} entity.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public abstract class GeneratedAccessLogEntryManagerImpl 
extends AbstractManager<AccessLogEntry> 
implements GeneratedAccessLogEntryManager {
    
    private final TableIdentifier<AccessLogEntry> tableIdentifier;
    
    protected GeneratedAccessLogEntryManagerImpl() {
        this.tableIdentifier = TableIdentifier.of("parser", "parser", "access_log");
    }
    
    @Override
    public TableIdentifier<AccessLogEntry> getTableIdentifier() {
        return tableIdentifier;
    }
    
    @Override
    public Stream<Field<AccessLogEntry>> fields() {
        return AccessLogEntryManager.FIELDS.stream();
    }
    
    @Override
    public Stream<Field<AccessLogEntry>> primaryKeyFields() {
        return Stream.of(
            AccessLogEntry.ID
        );
    }
}