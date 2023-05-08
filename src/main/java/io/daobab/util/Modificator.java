package io.daobab.util;

import io.daobab.clone.EntityDuplicator;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.MandatoryEntity;
import io.daobab.model.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Modificator {

    private Modificator() {
    }

    @SuppressWarnings("unchecked")
    public static <E extends EntityMap> E merge(E dbEntity, E patchOnly) {
        if (dbEntity == null) {
            throw new MandatoryEntity();
        }
        if (patchOnly == null || patchOnly.isEmpty()) {
            return dbEntity;
        }

        List<String> doNotModifyKeys = new ArrayList<>();

        if (dbEntity instanceof PrimaryKey) {
            PrimaryKey<E, ?, ?> pk = (PrimaryKey<E, ?, ?>) dbEntity;
            doNotModifyKeys.add(pk.colID().getFieldName());
        } else if (dbEntity instanceof PrimaryCompositeKey) {
            PrimaryCompositeKey<E, ?> pk = (PrimaryCompositeKey<E, ?>) dbEntity;
            List<TableColumn> tableColumns = pk.colCompositeId();
            tableColumns.stream().map(TableColumn::getColumn).map(Column::getFieldName).forEach(doNotModifyKeys::add);
        }

        E clonedDbEntity = EntityDuplicator.cloneEntity(dbEntity);

        patchOnly.entrySet().stream().filter(e -> !doNotModifyKeys.contains(e.getKey())).forEach(e -> clonedDbEntity.setColumnParam(e.getKey(), e.getValue()));

        return clonedDbEntity;
    }


    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E extends EntityMap> E exposeOnly(E dbEntity, Column<E, ?, ?>... columnsOnlyToExpose) {
        if (dbEntity == null) {
            throw new MandatoryEntity();
        }
        E exposedEntity;
        try {
            exposedEntity = (E) dbEntity.getEntityClass().getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new DaobabEntityCreationException(dbEntity.getEntityClass(), e);
        }

        if (columnsOnlyToExpose == null || columnsOnlyToExpose.length == 0) {
            return exposedEntity;
        }

        Arrays.stream(columnsOnlyToExpose)
                .map(Field::getFieldName)
                .forEach(e -> exposedEntity.setColumnParam(e, dbEntity.getColumnParam(e)));

        return exposedEntity;


    }
}
