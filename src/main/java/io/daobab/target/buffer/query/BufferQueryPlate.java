package io.daobab.target.buffer.query;

import io.daobab.error.ColumnMandatory;
import io.daobab.error.NullOrEmptyParameter;
import io.daobab.model.*;
import io.daobab.query.base.QueryJoin;
import io.daobab.query.base.QueryType;
import io.daobab.result.FieldsBuffer;
import io.daobab.result.FieldsProvider;
import io.daobab.result.FlatPlates;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.function.BufferFunctionManager;
import io.daobab.target.buffer.single.Plates;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings("unused")
public final class BufferQueryPlate extends BufferQueryBase<Entity, BufferQueryPlate> implements FieldsProvider, QueryJoin<BufferQueryPlate> {

    private boolean singleEntity = false;

    public BufferQueryPlate(BufferQueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    public BufferQueryPlate(BufferQueryTarget target, Entity... entities) {
        if (entities == null || entities.length == 0) {
            throw new NullOrEmptyParameter("entities");
        }
        List<TableColumn> columns = new LinkedList<>();
        for (Entity e : entities) {
            columns.addAll(e.columns());
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

    HashMap<Integer, ColumnFunction<?, ?, ?, ?>> functionMap = new HashMap<>();

    public BufferQueryPlate(BufferQueryTarget target, Column<? extends Entity, ?, ?>[] columns) {

        Column<?, ?, ?> column;
        if (columns[0] instanceof ColumnFunction) {
            ColumnFunction function = (ColumnFunction<?, ?, ?, ?>) columns[0];

            column = function.getFinalColumn();
            functionMap.put(0, function);
        } else {
            column = columns[0];
        }
        if (column == null) throw new ColumnMandatory();
        init(target, column.getInstance());

        andColumn(column);

        Set<String> entities = new HashSet<>();
        for (int i = 1; i < columns.length; i++) {
            if (columns[i] instanceof ColumnFunction) {
                ColumnFunction function = (ColumnFunction) columns[i];
                functionMap.put(i, function);
                Column functionFinalColumn = function.getFinalColumn();
                getFields().add(getInfoColumn(functionFinalColumn));
                entities.add(functionFinalColumn.getEntityName());
            } else {
                getFields().add(getInfoColumn(columns[i]));
                entities.add(columns[i].getEntityName());
            }

        }
        setSingleEntity(entities.size() == 1);
    }

    @SuppressWarnings("rawtypes")
    public BufferQueryPlate(BufferQueryTarget target, List<? extends Column> columndaos) {

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

    @Override
    public long countAny() {
        return findMany().size();
    }

    public FieldsProvider<FlatPlate> flat() {
        return map2(Plate::toFlat);
    }

    private <M> FieldsProvider<M> map2(Function<Plate, M> mapper) {
        List<Plate> res = findMany();
        List<M> rv = new LinkedList<>();
        if (mapper == null) {
            return new FieldsBuffer<>(rv);
        }

        res.forEach(t -> rv.add(mapper.apply(t)));

        return new FieldsBuffer<>(rv);
    }

    private void andColumn(Column<?, ?, ?> column) {
        if (getFields() == null) setFields(new LinkedList<>());
        getFields().add(getInfoColumn(column));
    }

    @Override
    public Plates findMany() {
        Plates plates = getTarget().readPlateList(modifyQuery(this));
        return new BufferFunctionManager().applyFunctions(plates, functionMap);

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

}
