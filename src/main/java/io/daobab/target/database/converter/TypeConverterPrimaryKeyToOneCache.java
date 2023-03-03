package io.daobab.target.database.converter;

import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterPKBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TypeConverterPrimaryKeyToOneCache<F, E extends Entity & PrimaryKey<E, F, ?>> implements EntityConverter<F, E>, TypeConverterPKBased<F, E> {

    private final TypeConverterPKBased<F, E> rootConverter;
    private final DataBaseTarget target;
    private final Map<F, E> keyEntityMap = new HashMap<>();
    private final Map<F, Consumer<E>> keyConsumerMap = new HashMap<>();

    public TypeConverterPrimaryKeyToOneCache(TypeConverterPKBased<F, E> rootConverter) {
        this.rootConverter = rootConverter;
        this.target = rootConverter.getDataBaseTarget();
    }

    @Override
    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rootConverter.readFromResultSet(rs, columnIndex);
    }

    @Override
    public void addKey(F key, Consumer<E> consumer) {
        keyConsumerMap.put(key, consumer);
    }

    @Override
    public void readEntities() {
        E tab = rootConverter.getTable();
        target.select(tab)
                .whereInCollection(tab.colID(), keyConsumerMap.keySet())
                .findMany()
                .forEach(e -> keyEntityMap.put(e.getId(), e));

    }

    @Override
    public void applyEntities() {
        for (Map.Entry<F, E> entry : keyEntityMap.entrySet()) {
            Consumer<E> consumer = keyConsumerMap.get(entry.getKey());
            if (consumer == null || entry.getValue() == null) continue;
            consumer.accept(entry.getValue());
        }
    }

    @Override
    public E convertReadingTarget(F from) {
        return rootConverter.convertReadingTarget(from);
    }

    @Override
    public String convertWritingTarget(E to) {
        return rootConverter.convertWritingTarget(to);
    }

    @Override
    public E getTable() {
        return rootConverter.getTable();
    }

    @Override
    public DataBaseTarget getDataBaseTarget() {
        return rootConverter.getDataBaseTarget();
    }

    @Override
    public boolean isEntityConverter() {
        return true;
    }
}
