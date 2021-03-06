/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 5:52 PM
 */

package com.ef.db.parser.parser.blocked_ip.generated;

import com.ef.db.parser.parser.blocked_ip.BlockedIp;
import com.ef.db.parser.parser.blocked_ip.BlockedIpImpl;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.core.component.SqlAdapter;
import com.speedment.runtime.core.db.SqlFunction;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The generated Sql Adapter for a {@link
 * com.ef.db.parser.parser.blocked_ip.BlockedIp} entity.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public abstract class GeneratedBlockedIpSqlAdapter implements SqlAdapter<BlockedIp> {
    
    private final TableIdentifier<BlockedIp> tableIdentifier;
    
    protected GeneratedBlockedIpSqlAdapter() {
        this.tableIdentifier = TableIdentifier.of("parser", "parser", "blocked_ips");
    }
    
    protected BlockedIp apply(ResultSet resultSet, int offset) throws SQLException {
        return createEntity()
            .setId(        resultSet.getLong(1 + offset))
            .setIpAddress( resultSet.getLong(2 + offset))
            .setComment(   resultSet.getString(3 + offset))
            ;
    }
    
    protected BlockedIpImpl createEntity() {
        return new BlockedIpImpl();
    }
    
    @Override
    public TableIdentifier<BlockedIp> identifier() {
        return tableIdentifier;
    }
    
    @Override
    public SqlFunction<ResultSet, BlockedIp> entityMapper() {
        return entityMapper(0);
    }
    
    @Override
    public SqlFunction<ResultSet, BlockedIp> entityMapper(int offset) {
        return rs -> apply(rs, offset);
    }
}