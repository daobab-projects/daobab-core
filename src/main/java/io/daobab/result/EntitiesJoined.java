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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class EntitiesJoined extends WhereBase implements QueryWhisperer {

    private final MultiEntityTarget target;
    private final List<Plate> rows;
    private final Plate rootPlate;

    public EntitiesJoined(MultiEntityTarget target, List<? extends Entity> columnsProviders, Query<?, ?, ?> query) {
        this.target = target;
        rows = new ArrayList<>(columnsProviders.size());

        columnsProviders.forEach(entity -> rows.add(new Plate(entity)));
        if (!rows.isEmpty()) {
            rootPlate = rows.get(0);
            join(query);
        }else{
            rootPlate = new Plate(query.getFields());
        }
    }

    public List<Plate> results() {
        return rows;
    }

    @SuppressWarnings({"rawtypes", "unchecked", "java:S3776", "java:S135"})
    private void join(Query<?, ?, ?> query) {
        List<JoinWrapper> joins = query.getJoins();

        for (JoinWrapper join : joins) {
            Entity rightTable = join.getTable();
            rootPlate.joinPlate(new Plate(rightTable));
            Column byColumn = join.getByColumn();

            Optional<Column> leftColumn = getColumnIfExists(rootPlate, byColumn);
            if (!leftColumn.isPresent()) {
                continue;
            }

            Column joinedTableColumn = rightTable.columns().stream()
                    .map(TableColumn::getColumn)
                    .filter(col -> col.getColumnName().equals(byColumn.getColumnName()))
                    .findAny()
                    .orElseThrow(() -> new DaobabException(String.format("Invalid operation during join: Entity %s has no column named %s", query.getTarget().getEntityName(rightTable.getEntityClass()), byColumn.getColumnName())));

            List<Object> columnValues = getThisValuesOfColumn(leftColumn.get());
            Where composedWhereInner = new WhereAnd().inFields(joinedTableColumn, columnValues);
            Where composedWhereRight = new WhereAnd().notInFields(joinedTableColumn, columnValues);

            boolean joinAllNotCommonFromLeft = false;
            boolean joinAllNotCommonFromRight = false;
            boolean joinCommons = false;

            switch (join.getType()) {
                default:
                case INNER:
                    joinCommons = true;
                    break;
                case OUTER:
                    joinAllNotCommonFromLeft = true;
                    joinAllNotCommonFromRight = true;
                    break;
                case LEFT_JOIN:
                    joinCommons = true;
                    joinAllNotCommonFromLeft = true;
                    break;
                case RIGHT_JOIN:
                    joinCommons = true;
                    joinAllNotCommonFromRight = true;
                    break;
            }

            Map<String, Object> joinWhereMap = join.getWhere().getWhereMap();
            //If there is an inner where in join, will be on second key for sure
            Object key = joinWhereMap.get(KEY + 2);
            Object val = joinWhereMap.get(VALUE + 2);
            Object wrapper = joinWhereMap.get(WRAPPER + 2);
            Object relation = joinWhereMap.get(RELATION + 2);
            composedWhereInner.add(wrapper, key, val, relation);
            composedWhereRight.add(wrapper, key, val, relation);
            Column[] rightColumns = rightTable.columns()
                    .stream()
                    .map(TableColumn::getColumn)
                    .toArray(Column[]::new);

            Map<Object, List<Plate>> rightMap = target.select(rightColumns)
                    .where(composedWhereInner)
                    .findMany()
                    .stream()
                    .collect(Collectors
                            .groupingBy(e -> e.getValue(joinedTableColumn)));

            List<Plate> joinedRowList = new ArrayList<>();

            for (Plate leftRow : rows) {
                Object leftValue = leftRow.getValue(leftColumn.get());
                if (leftValue == null) {
                    continue;
                }

                List<Plate> rightRelatedRecords = rightMap.get(leftValue);
                if (rightRelatedRecords == null || rightRelatedRecords.isEmpty()) {
                    if (joinAllNotCommonFromLeft) {
                        joinedRowList.add(leftRow);
                    }
                    continue;
                }
                if (joinCommons) {
                    for (Plate rPlate : rightRelatedRecords) {
                        Plate newRow = new Plate(leftRow);
                        newRow.joinPlate(rPlate);
                        joinedRowList.add(newRow);
                    }
                }
            }

            if (joinAllNotCommonFromRight) {
                joinedRowList.addAll(target.select(rightColumns)
                        .where(composedWhereRight)
                        .findMany());
            }

            rows.clear();
            rows.addAll(joinedRowList);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<Object> getThisValuesOfColumn(Column column) {

        Set<Object> set = new HashSet<>();
        for (Plate row : rows) {
            set.add(row.getValue(column));
        }
        return new ArrayList<>(set);

    }


    @SuppressWarnings("rawtypes")
    private Optional<Column> getColumnIfExists(ColumnsProvider provider, Column byColumn) {
        return target.getColumnsForTable(provider).stream()
                .map(TableColumn::getColumn)
                .filter(col -> col.getColumnName().equals(byColumn.getColumnName()))
                .findAny();
    }

    @Override
    public String getRelationBetweenExpressions() {
        return null;
    }
}
