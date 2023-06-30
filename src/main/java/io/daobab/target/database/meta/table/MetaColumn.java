package io.daobab.target.database.meta.table;


import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.model.TableName;
import io.daobab.target.database.meta.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@TableName("META_COLUMN")
public class MetaColumn extends Table<MetaColumn> implements
        MetaColumnName<MetaColumn>,
        MetaColumnSize<MetaColumn>,
        DecimalDigits<MetaColumn>,
        Remarks<MetaColumn>,
        MetaTableName<MetaColumn>,
        MetaSchemaName<MetaColumn>,
        MetaCatalogName<MetaColumn>,
        Nullable<MetaColumn>,
        TableColumnName<MetaColumn>,
        CamelName<MetaColumn>,
        MetaColumnDefault<MetaColumn>,
        OrdinalPosition<MetaColumn>,
        FieldClass<MetaColumn>,
        Datatype<MetaColumn> {

    public MetaColumn() {
        super();
    }

    public MetaColumn(Map<String, Object> parameters) {
        super(parameters);
    }


    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colColumnName()).size(256),
                new TableColumn(colRemarks()).size(256),
                new TableColumn(colColumnSize()).size(10),
                new TableColumn(colTableName()).size(256),
                new TableColumn(colSchemaName()).size(256),
                new TableColumn(colCatalogName()).size(256),
                new TableColumn(colCamelName()).size(256),
                new TableColumn(colNullable()),
                new TableColumn(colDecimalDigits()),
                new TableColumn(colTableColumnName()).size(10),
                new TableColumn(colColumnDefault()).size(10),
                new TableColumn(colOrdinalPosition()),
                new TableColumn(colFieldClass()),
                new TableColumn(colDatatype())
        );
    }

}
