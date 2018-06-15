package com.ef.parser.db.generated;

import com.ef.parser.db.ParserApplication;
import com.ef.parser.db.ParserApplicationBuilder;
import com.ef.parser.db.ParserApplicationImpl;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntryManagerImpl;
import com.ef.parser.db.parser.parser.access_log_entry.AccessLogEntrySqlAdapter;
import com.ef.parser.db.parser.parser.blocked_ip.BlockedIpManagerImpl;
import com.ef.parser.db.parser.parser.blocked_ip.BlockedIpSqlAdapter;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.common.injector.Injector;
import com.speedment.runtime.application.AbstractApplicationBuilder;

/**
 * A generated base {@link
 * com.speedment.runtime.application.AbstractApplicationBuilder} class for the
 * {@link com.speedment.runtime.config.Project} named parser.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public abstract class GeneratedParserApplicationBuilder extends AbstractApplicationBuilder<ParserApplication, ParserApplicationBuilder> {
    
    protected GeneratedParserApplicationBuilder() {
        super(ParserApplicationImpl.class, GeneratedParserMetadata.class);
        withManager(AccessLogEntryManagerImpl.class);
        withManager(BlockedIpManagerImpl.class);
        withComponent(AccessLogEntrySqlAdapter.class);
        withComponent(BlockedIpSqlAdapter.class);
    }
    
    @Override
    public ParserApplication build(Injector injector) {
        return injector.getOrThrow(ParserApplication.class);
    }
}