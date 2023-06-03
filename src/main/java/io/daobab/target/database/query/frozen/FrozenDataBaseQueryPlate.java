package io.daobab.target.database.query.frozen;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.model.TableColumn;
import io.daobab.query.base.QueryType;
import io.daobab.result.PlateProvider;
import io.daobab.target.buffer.single.Plates;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.query.DataBaseQueryPlate;
import io.daobab.target.protection.OperationType;

import java.util.*;

public class FrozenDataBaseQueryPlate extends FrozenDataBaseQueryBase<Entity, DataBaseQueryPlate, FrozenDataBaseQueryPlate> implements PlateProvider {


    private final List<TableColumn> fields = new ArrayList<>();

    private final DatabaseTypeConverter<?, ?>[] typeConverters;

    public FrozenDataBaseQueryPlate(DataBaseQueryPlate originalQuery) {
        super(originalQuery);
        target.getAccessProtector().removeViolatedInfoColumns(originalQuery.getFields(), OperationType.READ);
        fields.addAll(originalQuery.getFields());


        typeConverters = new DatabaseTypeConverter<?, ?>[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            Column<?, ?, ?> col = fields.get(i).getColumn();
            typeConverters[i] = target.getConverterManager().getConverter(col).orElse(null);
        }
    }

    public PlateProvider withParameters(Object... parameters) {
        return withParameters(Arrays.asList(parameters));
    }

    public PlateProvider withParameters(List<Object> parameters) {
        FrozenDataBaseQueryPlate frozenDataBaseQueryPlate = this;
        return new PlateProvider() {

            @Override
            public Plates findMany() {
                return target.readPlateList(frozenDataBaseQueryPlate, parameters, typeConverters);
            }

            @Override
            public Optional<Plate> findFirst() {
                return Optional.ofNullable(target.readPlate(frozenDataBaseQueryPlate, parameters, typeConverters));
            }

        };
    }


    @Override
    public Plates findMany() {
        validateEmptyParameters();
        return target.readPlateList(this, Collections.emptyList(), typeConverters);
    }

    @Override
    public Optional<Plate> findFirst() {
        validateEmptyParameters();
        return Optional.ofNullable(target.readPlate(this, Collections.emptyList(), typeConverters));
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.PLATE;
    }

    public List<TableColumn> getFields() {
        return fields;
    }
}
