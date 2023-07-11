package io.daobab.target.statistic.table;


import io.daobab.model.*;
import io.daobab.target.statistic.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableInformation(name = "STATISTIC")
public class StatisticRecord extends Table<StatisticRecord> implements
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


    public StatisticRecord() {
        super();
    }

    public StatisticRecord(Map<String, Object> parameters) {
        super(parameters);
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
    public Column<StatisticRecord, String, Key> colID() {
        return colKey();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PrimaryKey<?, ?, ?> other = (PrimaryKey<?, ?, ?>) obj;
        return Objects.equals(getId(), other.getId());
    }

}
