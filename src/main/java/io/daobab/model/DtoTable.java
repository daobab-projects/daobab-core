package io.daobab.model;

import java.util.Map;

/**
 * A table entity which provides the conversion into its immutable DTO counterpart.
 *
 * @param <E> entity type
 * @param <D> DTO type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class DtoTable<E extends Table, D> extends Table<E> {

    protected DtoTable() {
        super();
    }

    protected DtoTable(Map<String, Object> dtoParameterMap) {
        super(dtoParameterMap);
    }

    public abstract D toDto();
}
