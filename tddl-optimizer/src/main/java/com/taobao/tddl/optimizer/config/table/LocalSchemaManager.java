package com.taobao.tddl.optimizer.config.table;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.taobao.tddl.common.model.lifecycle.AbstractLifecycle;
import com.taobao.tddl.common.utils.TddlToStringStyle;
import com.taobao.tddl.optimizer.config.table.parse.TableMetaParser;

/**
 * 本地文件的schema manager实现
 * 
 * @since 5.1.0
 */
public class LocalSchemaManager extends AbstractLifecycle implements SchemaManager {

    protected Map<String, TableMeta> ss;

    protected void doInit() {
        super.doInit();
        ss = new ConcurrentHashMap<String, TableMeta>();
    }

    protected void doDestory() {
        super.doDestory();
        ss.clear();
    }

    public TableMeta getTable(String tableName) {
        return ss.get((tableName));
    }

    public void putTable(String tableName, TableMeta tableMeta) {
        ss.put(tableName.toUpperCase(), tableMeta);
    }

    public Collection<TableMeta> getAllTables() {
        return ss.values();
    }

    public static LocalSchemaManager parseSchema(String data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("schema is null");
        }

        InputStream sis = null;
        try {
            sis = new ByteArrayInputStream(data.getBytes());
            LocalSchemaManager schemaManager = new LocalSchemaManager();
            List<TableMeta> schemaList = TableMetaParser.parse(sis);
            for (TableMeta t : schemaList) {
                schemaManager.putTable(t.getTableName(), t);
            }
            return schemaManager;
        } finally {
            IOUtils.closeQuietly(sis);
        }
    }

    public static LocalSchemaManager parseSchema(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("in is null");
        }

        try {
            LocalSchemaManager schemaManager = new LocalSchemaManager();
            schemaManager.init();
            List<TableMeta> schemaList = TableMetaParser.parse(in);
            for (TableMeta t : schemaList) {
                schemaManager.putTable(t.getTableName(), t);
            }
            return schemaManager;
        } finally {
            IOUtils.closeQuietly(in);
        }

    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, TddlToStringStyle.DEFAULT_STYLE);
    }
}