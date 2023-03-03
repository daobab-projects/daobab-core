package io.daobab.target.database.converter;

import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterPKBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TypeConverterPrimaryKeyToManyCache<F, E extends Entity & PrimaryKey<E, F, ?>> implements EntityConverter<F, List<E>>, TypeConverterPKBased<F, E> {

    private final TypeConverterPKBased<F, E> rootConverter;
    private final DataBaseTarget target;
    private final Map<F, List<F>> keyEntityMap = new HashMap<>();
    private final Map<F, Consumer<List<E>>> keyConsumerMap = new HashMap<>();
    private final Map<F, E> secondTableEntityMap = new HashMap<>();
    private List<TwoKeys> relations;

    public TypeConverterPrimaryKeyToManyCache(TypeConverterPKBased<F, E> rootConverter) {
        this.rootConverter = rootConverter;
        this.target = rootConverter.getDataBaseTarget();
    }

    @Override
    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rootConverter.readFromResultSet(rs, columnIndex);
    }

    @Override
    public void addKey(F key, Consumer<List<E>> consumer) {
        keyConsumerMap.put(key, consumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readEntities() {

        E rootTable = rootConverter.getTable();
        E tab = rootConverter.getTable();
        F[] fields = (F[]) keyConsumerMap.keySet().toArray();
        relations = target.select(rootTable.colID(), tab.colID())
//                .from(rootConverter.getTable())
                .join(tab, rootTable.colID())
                .whereIn(tab.colID(), fields)
                .findMany().stream()
                .map(p -> new TwoKeys<>(p.getValue(rootTable.colID()), p.getValue(tab.colID())))
                .collect(Collectors.toList());

        relations.stream()
                .collect(Collectors.groupingBy(TwoKeys::getKeyTab1))
                .forEach((key, value) -> keyEntityMap.put((F) key, (List<F>) value));

        Set<F> secondTableKeys = (Set<F>) relations.stream().map(TwoKeys::getKeyTab2).collect(Collectors.toSet());
        F[] secondTableFields = (F[]) secondTableKeys.toArray();
        target.select(tab).whereIn(tab.colID(), secondTableFields).findMany().forEach(e -> secondTableEntityMap.put(e.getId(), e));

    }

    @Override
    public void applyEntities() {
        for (Map.Entry<F, List<F>> entry : keyEntityMap.entrySet()) {
            if (entry.getValue() == null) continue;

            List<E> relatedEntities = new ArrayList<>(entry.getValue().size());
            for (F secondTableKey : entry.getValue()) {
                E secondTableEntity = secondTableEntityMap.get(secondTableKey);
                relatedEntities.add(secondTableEntity);
            }

            Consumer<List<E>> consumer = keyConsumerMap.get(entry.getKey());
            if (consumer == null) continue;
            consumer.accept(relatedEntities);
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

    private static class TwoKeys<F> {
        private F keyTab1;
        private F keyTab2;

        public TwoKeys(F key, F value) {
            this.setKeyTab1(key);
            this.setKeyTab2(value);
        }

        public F getKeyTab1() {
            return keyTab1;
        }

        public void setKeyTab1(F keyTab1) {
            this.keyTab1 = keyTab1;
        }

        public F getKeyTab2() {
            return keyTab2;
        }

        public void setKeyTab2(F keyTab2) {
            this.keyTab2 = keyTab2;
        }
    }
}
