package io.daobab.result;

import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryWhisperer;
import io.daobab.statement.join.JoinWrapper;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;
import io.daobab.statement.where.base.WhereBase;
import io.daobab.target.buffer.multi.MultiEntityTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntitiesJoined extends WhereBase implements QueryWhisperer {

    private final MultiEntityTarget target;
    private final List<List<Plate>> rows;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public EntitiesJoined(MultiEntityTarget target, List<? extends ColumnsProvider> columnsProviders, Query<?, ?, ?> query) {
        this.target = target;
        rows = new ArrayList<>(columnsProviders.size());
        columnsProviders.forEach(entity -> {
            List<Plate> plates = new ArrayList<>();
            plates.add(new Plate(entity));
            rows.add(plates);
        });
        if (!rows.isEmpty()) {
            join(query);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Plate> toPlates() {

        List<Plate> plateList = new ArrayList<>(rows.size());

        for (List<Plate> columnsProviders : rows) {
            Plate plate = new Plate();
            for (Plate columnsProvider : columnsProviders) {
                for (TableColumn column : columnsProvider.columns()) {
                    plate.setValue(column.getColumn(), columnsProvider.getValue(column.getColumn()));
                }
            }
            plateList.add(plate);
        }
        return plateList;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void join(Query<?, ?, ?> query) {
        List<JoinWrapper> joins = query.getJoins();

        for (JoinWrapper join : joins) {
            Entity joinedTable = join.getTable();
            Column byColumn = join.getByColumn();
            Column joinedTableColumn = joinedTable.columns().stream()
                    .map(TableColumn::getColumn)
                    .filter(col -> col.getColumnName().equals(byColumn.getColumnName()))
                    .findAny()
                    .orElseThrow(() -> new DaobabException(String.format("Invalid operation during join: Entity %s has no column named %s", joinedTable.getEntityName(), byColumn.getColumnName())));

            ColumnAndPointer thisColumn = getThisColumn(byColumn);
            if (thisColumn == null) {
                continue;
            }

            Where composedWhere;
            composedWhere = new WhereAnd().inFields(joinedTableColumn, getThisValuesOfColumn(thisColumn));

//            switch (join.getType()){
//                default:
//                case INNER:
//                    where2=new WhereAnd().inFields(joinedTableColumn, getThisValuesOfColumn(thisColumn));
//                    break;
//                case OUTER:
//                    where2=new WhereAnd().notInFields(joinedTableColumn, getThisValuesOfColumn(thisColumn));
//                    break;
//                case LEFT_JOIN:
//                    where2=new WhereAnd().notInFields(joinedTableColumn, getThisValuesOfColumn(thisColumn));
//                    break;
//            }


            //If there is inner where in join, will be on second key for sure
            Object key = join.getWhere().getWhereMap().get(KEY + 2);
            Object val = join.getWhere().getWhereMap().get(VALUE + 2);
            Object wrapper = join.getWhere().getWhereMap().get(WRAPPER + 2);
            Object relation = join.getWhere().getWhereMap().get(RELATION + 2);
            composedWhere.add(wrapper, key, val, relation);

            Map<Object, List<Plate>> plateMap = target.select(joinedTable.columns()
                            .stream()
                            .map(TableColumn::getColumn)
                            .toArray(Column[]::new))
                    .where(composedWhere)
                    .findMany().stream()
                    .collect(Collectors
                            .groupingBy(e -> e.getValue(joinedTableColumn)));


            List<List<Plate>> joinedRowList = new ArrayList<>();

            for (List<Plate> row : rows) {
                Plate keyLeft = row.get(thisColumn.getPointer());
                Object value = keyLeft.getValue(thisColumn.getColumn());
                if (value == null) {
                    continue;
                }
                List<Plate> right = plateMap.get(value);

                if (right == null) {
                    continue;
                }
                for (Plate rPlate : right) {
                    List<Plate> newRow = new ArrayList<>(row);
                    newRow.add(rPlate);
                    joinedRowList.add(newRow);
                }
            }

            rows.clear();
            rows.addAll(joinedRowList);
        }
    }

    private List<Object> getThisValuesOfColumn(ColumnAndPointer columnAndPointer) {
        return rows.stream()
                .map(list -> list.get(columnAndPointer.getPointer()))
                .map(Plate.class::cast)
                .map(p -> p.getValue(columnAndPointer.getColumn()))
                .distinct()
                .collect(Collectors.toList());
    }

    private ColumnAndPointer getThisColumn(Column byColumn) {
        List<Plate> thisEntity = rows.get(0);
        int counter = 0;
        for (Plate provider : thisEntity) {
            Optional<Column> optionalColumn = getColumnIfExists(provider, byColumn);
            if (optionalColumn.isPresent()) {
                return new ColumnAndPointer(optionalColumn.get(), counter);
            }
            counter++;
        }
        return null;
    }

    private Optional<Column> getColumnIfExists(ColumnsProvider provider, Column byColumn) {
        return provider.columns().stream()
                .map(TableColumn::getColumn)
                .filter(col -> col.getColumnName().equals(byColumn.getColumnName()))
                .findAny();
    }

    @Override
    public String getRelationBetweenExpressions() {
        return null;
    }
}
