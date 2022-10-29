package io.daobab.target.database.query;

import io.daobab.error.MandatoryColumn;
import io.daobab.error.NullOrEmptyParameter;
import io.daobab.model.*;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.query.base.QueryType;
import io.daobab.result.FieldsBuffer;
import io.daobab.result.FieldsProvider;
import io.daobab.result.FlatPlates;
import io.daobab.result.PlateProvider;
import io.daobab.statement.condition.Count;
import io.daobab.target.buffer.single.Plates;
import io.daobab.target.database.QueryTarget;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings("unused")
public final class DataBaseQueryPlate extends DataBaseQueryBase<Entity, DataBaseQueryPlate> implements QueryExpressionProvider<Entity>, PlateProvider {

    private boolean singleEntity = false;

    public DataBaseQueryPlate(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    public DataBaseQueryPlate(QueryTarget target, Entity... entities) {
        if (entities == null || entities.length == 0) {
            throw new NullOrEmptyParameter("entities");
        }
        List<TableColumn> columns = new ArrayList<>();
        for (Entity e : entities) {
            columns.addAll(e.columns());
        }

        TableColumn fielddao = columns.get(0);
        if (fielddao == null) throw new MandatoryColumn();
        init(target, fielddao.getColumn().getInstance());
        andColumn(fielddao.getColumn());

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
            entities.add(columndaos[i].getEntityName());
        }

        setSingleEntity(entities.size() == 1);
    }

    public DataBaseQueryPlate(QueryTarget target, List<? extends Column> columndaos) {

        Column<?, ?, ?> fielddao = columndaos.get(0);
        if (fielddao == null) throw new MandatoryColumn();
        init(target, fielddao.getInstance());
        andColumn(fielddao);

        Set<String> entities = new HashSet<>();
        for (int i = 1; i < columndaos.size(); i++) {
            getFields().add(getInfoColumn(columndaos.get(i)));
            entities.add(columndaos.get(i).getEntityName());
        }

        setSingleEntity(entities.size() == 1);
    }


    public DataBaseQueryPlate(String nativeQuery, QueryTarget target, Column<? extends Entity, ?, ?>[] columndaos) {

        Column<?, ?, ?> fielddao = columndaos[0];
        if (fielddao == null) throw new MandatoryColumn();
        init(target, fielddao.getInstance());
        andColumn(fielddao);

        Set<String> entities = new HashSet<>();
        for (int i = 1; i < columndaos.length; i++) {
            getFields().add(getInfoColumn(columndaos[i]));
            entities.add(columndaos[i].getEntityName());
        }

        setSingleEntity(entities.size() == 1);
        this._nativeQuery = nativeQuery;
    }

    @Override
    public long countAny() {
        setTempCount(Count.any());
        return getTarget().count(this);
    }


    public FieldsProvider<FlatPlate> flat() {
        return map2(Plate::toFlat);
    }

    private <M> FieldsProvider<M> map2(Function<Plate, M> mapper) {
        List<Plate> res = findMany();
        List<M> rv = new ArrayList<>();
        if (mapper == null) return new FieldsBuffer<>(rv);

        res.forEach(t -> rv.add(mapper.apply(t)));

        return new FieldsBuffer<>(rv);
    }

    private DataBaseQueryPlate andColumn(Column<?, ?, ?> columndao) {
        if (getFields() == null) setFields(new ArrayList<>());
        getFields().add(getInfoColumn(columndao));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Plates findMany() {
        return getTarget().readPlateList(modifyQuery(this));
    }

    @Override
    public Optional<Plate> findFirst() {
        return Optional.ofNullable(getTarget().readPlate(modifyQuery(this)));
    }

    public <M extends EntityMap> List<M> findManyAs(Class<M> clazz) {
        return findMany()
                .stream()
                .map(p -> p.toEntity(clazz, getFields()))
                .collect(Collectors.toList());
    }

    public <M extends EntityMap> M findOneAs(Class<M> clazz) {
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
        List<Entity> rv = new ArrayList<>();
        if (getFields() == null || getFields().isEmpty()) return rv;
        for (TableColumn col : getFields()) {
            for (Entity e : rv) {
                if (e.getEntityName().equals(col.getColumn().getInstance().getEntityName())) {
                    continue;
                }
                rv.add(col.getColumn().getInstance());
            }
        }
        return rv;
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

    @Override
    public String toSqlQuery() {
        return getTarget().toSqlQuery(this);
    }

}
