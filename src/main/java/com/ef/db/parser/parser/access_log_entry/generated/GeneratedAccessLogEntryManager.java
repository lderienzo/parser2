/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 8/15/18 12:29 PM
 */

package com.ef.db.parser.parser.access_log_entry.generated;

import com.ef.db.parser.parser.access_log_entry.AccessLogEntry;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.core.manager.Manager;
import com.speedment.runtime.field.Field;
import java.util.List;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * The generated base interface for the manager of every {@link
 * com.ef.db.parser.parser.access_log_entry.AccessLogEntry} entity.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public interface GeneratedAccessLogEntryManager extends Manager<AccessLogEntry> {
    
    TableIdentifier<AccessLogEntry> IDENTIFIER = TableIdentifier.of("parser", "parser", "access_log");
    List<Field<AccessLogEntry>> FIELDS = unmodifiableList(asList(
        AccessLogEntry.ID,
        AccessLogEntry.DATE,
        AccessLogEntry.IP_ADDRESS,
        AccessLogEntry.REQUEST,
        AccessLogEntry.STATUS,
        AccessLogEntry.USER_AGENT
    ));
    
    @Override
    default Class<AccessLogEntry> getEntityClass() {
        return AccessLogEntry.class;
    }
}