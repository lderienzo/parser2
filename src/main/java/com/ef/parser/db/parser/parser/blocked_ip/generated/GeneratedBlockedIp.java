package com.ef.parser.db.parser.parser.blocked_ip.generated;

import com.ef.parser.db.parser.parser.blocked_ip.BlockedIp;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.runtime.config.identifier.ColumnIdentifier;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.field.LongField;
import com.speedment.runtime.field.StringField;
import com.speedment.runtime.typemapper.TypeMapper;

/**
 * The generated base for the {@link
 * com.ef.parser.db.parser.parser.blocked_ip.BlockedIp}-interface representing
 * entities of the {@code blocked_ips}-table in the database.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public interface GeneratedBlockedIp {
    
    /**
     * This Field corresponds to the {@link BlockedIp} field that can be
     * obtained using the {@link BlockedIp#getId()} method.
     */
    LongField<BlockedIp, Long> ID = LongField.create(
        Identifier.ID,
        BlockedIp::getId,
        BlockedIp::setId,
        TypeMapper.primitive(),
        true
    );
    /**
     * This Field corresponds to the {@link BlockedIp} field that can be
     * obtained using the {@link BlockedIp#getComment()} method.
     */
    StringField<BlockedIp, String> COMMENT = StringField.create(
        Identifier.COMMENT,
        BlockedIp::getComment,
        BlockedIp::setComment,
        TypeMapper.identity(),
        false
    );
    
    /**
     * Returns the id of this BlockedIp. The id field corresponds to the
     * database column parser.parser.blocked_ips.id.
     * 
     * @return the id of this BlockedIp
     */
    long getId();
    
    /**
     * Returns the comment of this BlockedIp. The comment field corresponds to
     * the database column parser.parser.blocked_ips.comment.
     * 
     * @return the comment of this BlockedIp
     */
    String getComment();
    
    /**
     * Sets the id of this BlockedIp. The id field corresponds to the database
     * column parser.parser.blocked_ips.id.
     * 
     * @param id to set of this BlockedIp
     * @return   this BlockedIp instance
     */
    BlockedIp setId(long id);
    
    /**
     * Sets the comment of this BlockedIp. The comment field corresponds to the
     * database column parser.parser.blocked_ips.comment.
     * 
     * @param comment to set of this BlockedIp
     * @return        this BlockedIp instance
     */
    BlockedIp setComment(String comment);
    
    enum Identifier implements ColumnIdentifier<BlockedIp> {
        
        ID      ("id"),
        COMMENT ("comment");
        
        private final String columnId;
        private final TableIdentifier<BlockedIp> tableIdentifier;
        
        Identifier(String columnId) {
            this.columnId        = columnId;
            this.tableIdentifier = TableIdentifier.of(    getDbmsId(), 
                getSchemaId(), 
                getTableId());
        }
        
        @Override
        public String getDbmsId() {
            return "parser";
        }
        
        @Override
        public String getSchemaId() {
            return "parser";
        }
        
        @Override
        public String getTableId() {
            return "blocked_ips";
        }
        
        @Override
        public String getColumnId() {
            return this.columnId;
        }
        
        @Override
        public TableIdentifier<BlockedIp> asTableIdentifier() {
            return this.tableIdentifier;
        }
    }
}