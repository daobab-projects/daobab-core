package io.daobab.target.database.converter;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.model.RelatedTo;
import io.daobab.query.base.QueryWhisperer;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.converter.type.TypeConverterPKBasedList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//E (SRC)- this entity with PK
//E2 (DEST)- other Entity with FK to this entity. Many to One relation
public class TypeConverterPrimaryKeyToManyCache<F, E extends Entity & PrimaryKey<E, F, ?>, E2 extends Entity & PrimaryKey<E2, F, ?>> extends TypeConverterPKBasedList<F, E2> implements EntityListConverter<F, E2>, KeyableCache<F, List<E2>>, QueryWhisperer {

    private final TypeConverterPKBasedList<F, E2> rootConverter;
    private final QueryTarget target;
    private final Map<F, List<E2>> keyEntityMap = new HashMap<>();

    //storeId,Consumer<List<store>>
    private final Map<F, Consumer<List<E2>>> destKeyConsumerMap = new HashMap<>();
    private final Map<F, E2> secondTableEntityMap = new HashMap<>();
    private final E2 destEntity;
    private final Column<E, F, RelatedTo> srcFkColumnToDest;
    private final Column<E2, F, RelatedTo> destColumn;
    private final E srcEntity;

    public TypeConverterPrimaryKeyToManyCache(QueryTarget target, TypeConverterPKBasedList<F, E2> rootConverter, E srcEntity, E2 destEntity) {
        System.out.println(">>> srcEntity: " + srcEntity.getEntityName());
        System.out.println(">>> dstEntity: " + destEntity.getEntityName());
        this.rootConverter = rootConverter;
        this.target = target;
        this.srcEntity = srcEntity;
        this.srcFkColumnToDest = (Column<E, F, RelatedTo>) destEntity.colID().transformTo(srcEntity);
        this.destEntity = destEntity;
        this.destColumn = (Column<E2, F, RelatedTo>) destEntity.colID();
    }

    @Override
    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return rootConverter.readFromResultSet(rs, columnIndex);
    }

    @Override
    public void addKey(F key, Consumer<List<E2>> consumer) {
        destKeyConsumerMap.put(key, consumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readEntities(List<E2> entities) {
        if (destKeyConsumerMap.isEmpty()) {
            return;
        }

        F[] fields = (F[]) destKeyConsumerMap.keySet().toArray();
        List<F> srcPks = entities.stream().map(PrimaryKey::getId).collect(Collectors.toList());
        System.out.println(">>> fields length " + fields.length);

        Entities<E2> fkEntities = target.select(destEntity).whereInFields(destEntity.colID(), destKeyConsumerMap.keySet()).findMany();

        List<TwoKeys> relations = target.select(srcEntity.colID(), destEntity.colID())
                //.join(destEntity, destEntity.colID(),srcFkColumnToDest)
                .where(and().in(destEntity.colID(), fields).inFields(srcEntity.colID(), srcPks))
                .findMany().stream()
                .map(p -> new TwoKeys<>(p.getValue(srcEntity.colID()), p.getValue(destEntity.colID())))
                .collect(Collectors.toList());

        relations.stream()
                .collect(Collectors.groupingBy(TwoKeys::getKeyTab1))
                .forEach((key, value) -> keyEntityMap.put((F) key, fkEntities.select(destEntity).whereInFields(destEntity.colID(), (List<F>) value).findMany()));

//        entities.forEach(e->{
//            F srcfk=srcFkColumnToDest.getValue(e);
//            destKeyConsumerMap.get(srcfk).accept(keyEntityMap.get(srcfk));
//        });
    }

    @Override
    public void applyEntities() {
        for (Map.Entry<F, List<E2>> entry : keyEntityMap.entrySet()) {
            if (entry.getValue() == null) continue;

            List<E2> relatedEntities = new ArrayList<>(entry.getValue().size());
            for (E2 secondTableEntity : entry.getValue()) {
//                E2 secondTableEntity = secondTableEntityMap.get(secondTableKey);
                relatedEntities.add(secondTableEntity);
            }

            Consumer<List<E2>> consumer = destKeyConsumerMap.get(entry.getKey());
            if (consumer == null) continue;
            consumer.accept(new EntityList<>(relatedEntities, destEntity));
        }
    }

    @Override
    public List<E2> convertReadingTarget(F from) {
        return getDataBaseTarget().select(destEntity).whereEqual(destEntity.colID(), from).findMany();
    }

    @Override
    public String convertWritingTarget(List<E2> to) {
        return rootConverter.convertWritingTarget(to);
    }

    @Override
    public E2 getTable() {
        return rootConverter.getTable();
    }

    @Override
    public DataBaseTarget getDataBaseTarget() {
        return rootConverter.getDataBaseTarget();
    }

    @Override
    public boolean isEntityListConverter() {
        return true;
    }

    private static class TwoKeys<F> {
        private F keyTab1;
        private F keyTab2;

        public TwoKeys(F key, F value) {
            this.setKeyTab1(key);
            this.setKeyTab2(value);

            System.out.println("firstKey " + key + " list of second keys " + value);
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
