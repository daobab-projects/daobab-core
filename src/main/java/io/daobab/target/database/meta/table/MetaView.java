package io.daobab.target.database.meta.table;

import java.util.Map;

public class MetaView extends MetaTable {

    public MetaView() {
        super();
    }

    public MetaView(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public String getEntityName() {
        return "META_VIEW";
    }
}
