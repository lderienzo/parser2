/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 8/15/18 12:29 PM
 */

package com.ef.db.parser.parser.blocked_ip.generated;

import com.ef.db.parser.parser.blocked_ip.BlockedIp;
import com.ef.db.parser.parser.blocked_ip.BlockedIpManager;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.core.manager.AbstractManager;
import com.speedment.runtime.field.Field;
import java.util.stream.Stream;

/**
 * The generated base implementation for the manager of every {@link
 * com.ef.db.parser.parser.blocked_ip.BlockedIp} entity.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public abstract class GeneratedBlockedIpManagerImpl 
extends AbstractManager<BlockedIp> 
implements GeneratedBlockedIpManager {
    
    private final TableIdentifier<BlockedIp> tableIdentifier;
    
    protected GeneratedBlockedIpManagerImpl() {
        this.tableIdentifier = TableIdentifier.of("parser", "parser", "blocked_ips");
    }
    
    @Override
    public TableIdentifier<BlockedIp> getTableIdentifier() {
        return tableIdentifier;
    }
    
    @Override
    public Stream<Field<BlockedIp>> fields() {
        return BlockedIpManager.FIELDS.stream();
    }
    
    @Override
    public Stream<Field<BlockedIp>> primaryKeyFields() {
        return Stream.of(
            BlockedIp.ID
        );
    }
}