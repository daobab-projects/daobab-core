package io.daobab.converter.duplicator.duplication;

import io.daobab.converter.duplicator.DuplicatorManager;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.model.Entity;
import io.daobab.model.Field;
import io.daobab.model.RelatedTo;
import io.daobab.model.TableColumn;
import io.daobab.target.buffer.single.Entities;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class EntityDuplication<E extends Entity> {

    @SuppressWarnings("rawtypes")
    private final List<FieldDuplication> fieldJsonConversions;

    @SuppressWarnings("rawtypes")
    private final List<Field> fields;

    E entity;


    public EntityDuplication(E entity) {
        this(entity, DuplicatorManager.INSTANCE);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public EntityDuplication(E entity, DuplicatorManager jsonConverterManager) {
        this.entity=entity;
        fields = entity.columns().stream().map(TableColumn::getColumn).collect(Collectors.toList());
        fieldJsonConversions = entity.columns().stream().map(TableColumn::getColumn).map(c -> new FieldDuplication(c, jsonConverterManager)).collect(Collectors.toList());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public E duplicate(E entity) {
        if (entity == null) return null;

        E rv;
        try {
            rv = (E) entity.getEntityClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new DaobabEntityCreationException(entity.getEntityClass(), e);
        }


        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            FieldDuplication fieldJsonConversion = fieldJsonConversions.get(i);
            rv = (E) field.setValue((RelatedTo) rv, fieldJsonConversion.duplicate(field.getValue((RelatedTo) entity)));

    }
        return rv;
    }

    public Entities<E> duplicateList(StringBuilder sb, Entities<E> entities) {

        return null;
    }
}
