package io.daobab.target.statistic.table;


import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.target.statistic.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class StatisticRecord extends Table implements
        Key<StatisticRecord>,
        Remarks<StatisticRecord>,
        RequestDate<StatisticRecord>,
        ResponseDate<StatisticRecord>,
        RelatedEntity<StatisticRecord>,
        ExecutionTime<StatisticRecord>,
        RequestType<StatisticRecord>,
        ProcedureName<StatisticRecord>,
        SqlQuery<StatisticRecord>,
        ErrorDesc<StatisticRecord>,
        Status<StatisticRecord>,

        PrimaryKey<StatisticRecord, String, Key> {


    public StatisticRecord(String identifier) {
        setId(identifier);
    }

    @Override
    public String getEntityName() {
        return "STATISTIC";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colKey()).size(256),
                new TableColumn(colRemarks()).size(256),
                new TableColumn(colRequestDate()),
                new TableColumn(colRequestDate()),
                new TableColumn(colExecutionTime()),
                new TableColumn(colRequestType()),
                new TableColumn(colStatus()),
                new TableColumn(colProcedureName()).size(256),
                new TableColumn(colRelatedEntity()).size(256),
                new TableColumn(colErrorDesc()).size(1024),
                new TableColumn(colSqlQuery()).size(1024)
        );
    }

    @Override
    public StatisticRecord clone() {
        return EntityDuplicator.cloneEntity(this);
    }

    @Override
    public Column<StatisticRecord, String, Key> colID() {
        return colKey();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PrimaryKey<?,?,?> other = (PrimaryKey<?,?,?>) obj;
        return Objects.equals(getId(), other.getId());
    }

}
