package io.daobab.test.dao.table;

import io.daobab.model.*;
import io.daobab.test.dao.column.ActorId;
import io.daobab.test.dao.column.FirstName;
import io.daobab.test.dao.column.LastName;
import io.daobab.test.dao.column.LastUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableName("ACTOR")
public class Actor extends Table<Actor> implements
        ActorId<Actor>,
        FirstName<Actor>,
        LastName<Actor>,
        LastUpdate<Actor>,

        PrimaryKey<Actor, Integer, ActorId> {


    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colActorId()),
                new TableColumn(colFirstName()),
                new TableColumn(colLastName()),
                new TableColumn(colLastUpdate())

        );
    }


    public Actor() {
        super();
    }

    public Actor(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Actor, Integer, ActorId> colID() {
        return colActorId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PrimaryKey other = (PrimaryKey) obj;
        return Objects.equals(getId(), other.getId());
    }


}
