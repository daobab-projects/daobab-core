package io.daobab.target.database.meta.table;

import io.daobab.model.TableName;

import java.util.Map;

@TableName("META_VIEW")
public class MetaView extends MetaTable {

    public MetaView() {
        super();
    }

    public MetaView(Map<String, Object> parameters) {
        super(parameters);
    }

}
