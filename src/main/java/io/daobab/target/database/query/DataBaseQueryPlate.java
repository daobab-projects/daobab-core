package io.daobab.target.database.query;

import io.daobab.error.MandatoryColumn;
import io.daobab.error.MandatoryEntity;
import io.daobab.model.*;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.query.base.QueryType;
import io.daobab.result.FieldsBuffer;
import io.daobab.result.FieldsProvider;
import io.daobab.result.FlatPlates;
import io.daobab.result.PlateProvider;
import io.daobab.target.buffer.single.Plates;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.frozen.FrozenDataBaseQueryPlate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public final class DataBaseQueryPlate extends DataBaseQueryBase<Entity, DataBaseQueryPlate> implements QueryExpressionProvider<Entity>, PlateProvider {

    private boolean singleEntity = false;

    public DataBaseQueryPlate(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    public DataBaseQueryPlate(QueryTarget target, Entity... entities) {
        if (entities == null || entities.length == 0) {
            throw new MandatoryEntity();
        }
        List<TableColumn> columns = new ArrayList<>();
        for (Entity e : entities) {
            columns.addAll(target.getColumnsForTable(e));
        }

        TableColumn fieldDao = columns.get(0);
        if (fieldDao == null) throw new MandatoryColumn();
        init(target, fieldDao.getColumn().getInstance());
        andColumn(fieldDao.getColumn());

        for (int i = 1; i < columns.size(); i++) {
            getFields().add(columns.get(i));
        }
        setSingleEntity(entities.length == 1);
    }

    public DataBaseQueryPlate(QueryTarget target, Column<? extends Entity, ?, ?>[] columndaos) {

        Column<?, ?, ?> fielddao = columndaos[0];
        if (fielddao == null) throw new MandatoryColumn();
        init(target, fielddao.getInstance());

        andColumn(fielddao);

        Set<String> entities = new HashSet<>();
        for (int i = 1; i < columndaos.length; i++) {
            getFields().add(getInfoColumn(columndaos[i]));
            entities.add(target.getEntityName(columndaos[i].entityClass()));
        }

        setSingleEntity(entities.size() == 1);
    }

    @SuppressWarnings("rawtypes")
    public DataBaseQueryPlate(QueryTarget target, List<? extends Column> columndaos) {

        Column<?, ?, ?> fielddao = columndaos.get(0);
        if (fielddao == null) throw new MandatoryColumn();
        init(target, fielddao.getInstance());
        andColumn(fielddao);

        Set<String> entities = new HashSet<>();
        for (int i = 1; i < columndaos.size(); i++) {
            getFields().add(getInfoColumn(columndaos.get(i)));
            entities.add(target.getEntityName(columndaos.get(i).entityClass()));
        }

        setSingleEntity(entities.size() == 1);
    }

    public FieldsProvider<FlatPlate> flat() {
        return map2(Plate::toFlat);
    }

    private <M> FieldsProvider<M> map2(Function<Plate, ? extends M> mapper) {
        if (mapper == null) return new FieldsBuffer<>();
        return new FieldsBuffer<>(findMany().stream().map(mapper).collect(Collectors.toList()));
    }

    private DataBaseQueryPlate andColumn(Column<?, ?, ?> column) {
        if (getFields() == null) setFields(new ArrayList<>());
        getFields().add(getInfoColumn(column));
        return this;
    }

    @Override
    public Plates findMany() {
        return getTarget().readPlateList(modifyQuery(this));
    }

    @Override
    public Optional<Plate> findFirst() {
        return Optional.ofNullable(getTarget().readPlate(modifyQuery(this)));
    }

    public <M extends Entity> List<M> findManyAs(Class<M> clazz) {
        return findMany()
                .stream()
                .map(p -> p.toEntity(clazz, getFields()))
                .collect(Collectors.toList());
    }

    public <M extends Entity> M findOneAs(Class<M> clazz) {
        return findFirst().map(p -> p.toEntity(clazz, getFields())).orElse(null);
    }

    public FlatPlates findManyAsFlat() {
        return new FlatPlates(flat().findMany());
    }

    public FlatPlate findOneAsFlat() {
        return flat().findFirst().orElse(null);
    }

    public Optional<FlatPlate> findFirstAsFlat() {
        return flat().findFirst();
    }

    public List<Entity> getRelatedEntities() {
        if (getFields() == null) return new ArrayList<>();
        return getFields().stream().map(col -> col.getColumn().getInstance()).distinct().collect(Collectors.toList());
    }

    @Override
    public DataBaseQueryPlate getInnerQuery() {
        return this;
    }

    public boolean isSingleEntity() {
        return singleEntity;
    }

    public void setSingleEntity(boolean singleEntity) {
        this.singleEntity = singleEntity;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.PLATE;
    }

    public FrozenDataBaseQueryPlate freezeQuery() {
        return new FrozenDataBaseQueryPlate(this);
    }


}
