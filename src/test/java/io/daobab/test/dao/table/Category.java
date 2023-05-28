package io.daobab.test.dao.table;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.test.dao.column.CategoryId;
import io.daobab.test.dao.column.LastUpdate;
import io.daobab.test.dao.column.Name;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@TableInformation(name = "CATEGORY", idGenerator = IdGeneratorType.SEQUENCE, sequenceName = "CATEGORY_SEQ")
public class Category extends Table<Category> implements
        CategoryId<Category>,
        Name<Category>,
        LastUpdate<Category>,

        PrimaryKey<Category, Integer, CategoryId> {

    @Override
    public List<TableColumn> columns() {
        return DaobabCache.getTableColumns(this,
                () -> Arrays.asList(
                        new TableColumn(colCategoryId()),
                        new TableColumn(colName()),
                        new TableColumn(colLastUpdate())
                ));
    }


    public Category() {
        super();
    }

    public Category(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Category, Integer, CategoryId> colID() {
        return colCategoryId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PrimaryKey other = (PrimaryKey) obj;
        return Objects.equals(getId(), other.getId());
    }

}
