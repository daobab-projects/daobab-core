package io.daobab.target.database.meta.table;

import io.daobab.model.TableInformation;

import java.util.Map;

@TableInformation(name = "META_VIEW")
public class MetaView extends MetaTable {

    public MetaView() {
        super();
    }

    public MetaView(Map<String, Object> parameters) {
        super(parameters);
    }

}
