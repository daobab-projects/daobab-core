package io.daobab.model;

import java.util.Map;

/**
 * A {@link Table} entity paired with an immutable DTO. {@link #toDto()} converts the entity to its DTO, and the
 * generated {@code fromDto(dto)} static factory converts back. The generator and the annotation processor emit
 * such entities (the ones with the {@code Entity} suffix) out of a {@code @DaobabTable} definition.
 *
 * @param <E> the entity type
 * @param <D> the DTO type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class DtoTable<E extends Table, D> extends Table<E> {

    protected DtoTable() {
        super();
    }

    protected DtoTable(Map<String, Object> dtoParameterMap) {
        super(dtoParameterMap);
    }

    /**
     * Converts this entity into its immutable DTO.
     */
    public abstract D toDto();
}
