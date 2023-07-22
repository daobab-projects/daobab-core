package io.daobab.test.dao.table;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.test.dao.column.LanguageId;
import io.daobab.test.dao.column.LastUpdate;
import io.daobab.test.dao.column.NameLang;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableInformation(name = "LANGUAGE")
public class Language extends Table<Language> implements
        LanguageId<Language>,
        NameLang<Language>,
        LastUpdate<Language>,

        PrimaryKey<Language, Integer, LanguageId> {

    @Override
    public List<TableColumn> columns() {
        return DaobabCache.getTableColumns(this,
                () -> Arrays.asList(
                        new TableColumn(colLanguageId()),
                        new TableColumn(colName()),
                        new TableColumn(colLastUpdate())
                ));
    }


    public Language() {
        super();
    }

    public Language(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Language, Integer, LanguageId> colID() {
        return colLanguageId();
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
