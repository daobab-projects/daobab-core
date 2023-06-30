package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.ActorId;
import io.daobab.test.dao.column.FirstName;
import io.daobab.test.dao.column.LastName;
import io.daobab.test.dao.column.LastUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class Actor extends Table<Actor> implements
        ActorId<Actor>,
        FirstName<Actor>,
        LastName<Actor>,
        LastUpdate<Actor>,

        PrimaryKey<Actor, Integer, ActorId> {

    @Override
    public String getEntityName() {
        return "ACTOR";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colActorId()),
                new TableColumn(colFirstName()),
                new TableColumn(colLastName()),
                new TableColumn(colLastUpdate())

        );
    }

    @Override
    public Actor clone() {
        return EntityDuplicator.cloneEntity(this);
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
