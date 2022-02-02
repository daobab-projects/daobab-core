package io.daobab.target.database.query;

import io.daobab.error.ColumnMandatory;
import io.daobab.error.NullOrEmptyParameter;
import io.daobab.error.TargetNoCacheNoEntityManagerException;
import io.daobab.model.*;
import io.daobab.query.base.*;
import io.daobab.result.FieldsProvider;
import io.daobab.result.FlatPlates;
import io.daobab.result.PlateProvider;
import io.daobab.target.buffer.single.Plates;
import io.daobab.statement.condition.Count;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.DataBaseTarget;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public final class DataBaseQueryPlate extends DataBaseQueryBase<Entity, DataBaseQueryPlate> implements QueryExpressionProvider<Entity, DataBaseQueryPlate>, PlateProvider, QueryJoin<DataBaseQueryPlate> {

    private boolean singleEntity = false;

    public DataBaseQueryPlate(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    public DataBaseQueryPlate(QueryTarget target, Entity... entities) {
        if (entities == null || entities.length == 0) {
            throw new NullOrEmptyParameter("entities");
        }
        List<TableColumn> columns = new LinkedList<>();
        for (Entity e : entities) {
            for (TableColumn ecol : e.columns()) {
                columns.add(ecol);
            }
        }

        TableColumn fielddao = columns.get(0);
        if (fielddao == null) throw new ColumnMandatory();
        init(target, fielddao.getColumn().getInstance());
        andColumn(fielddao.getColumn());

        for (int i = 1; i < columns.size(); i++) {
            getFields().add(columns.get(i));
        }
        setSingleEntity(entities.length == 1);
    }

    public DataBaseQueryPlate(QueryTarget target, Column<? extends Entity, ?, ?>[] columndaos) {

        Column<?, ?, ?> fielddao = columndaos[0];
        if (fielddao == null) throw new ColumnMandatory();
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
        if (fielddao == null) throw new ColumnMandatory();
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
        if (fielddao == null) throw new ColumnMandatory();
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
        return countBy(Count.any());
    }

    public long countBy(Count cnt) {
        setTempCount(cnt);
        if (getTarget().isBuffer()) {
            if (cnt.countEntities()) {
                return findMany().size();
            } else {
                //TODO: czy tu ma byc _unique??
                return 0;////return new Long(resultFieldUniqueSetFromCache((ColumnDefinition<E, ?,?>)cnt.getFieldForPointer(1)).size());
            }

        }
        if (getTarget() instanceof DataBaseTarget) {
            DataBaseTarget emprov = (DataBaseTarget) getTarget();
            return emprov.count(this);
        }
        throw new TargetNoCacheNoEntityManagerException(getTarget());
    }

    public FieldsProvider<FlatPlate> flat() {
        return map(Plate::toFlat);
    }

    private DataBaseQueryPlate andColumn(Column<?, ?, ?> columndao) {
        if (getFields() == null) setFields(new LinkedList<>());
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
                .map(p->p.toEntity(clazz, getFields()))
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
        List<Entity> rv = new LinkedList<>();
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
    public Query getSelect() {
        return this;
    }

    @Override
    public boolean isResultCached() {
        return getTarget().isBuffer();
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
