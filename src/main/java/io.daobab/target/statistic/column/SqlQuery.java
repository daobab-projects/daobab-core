package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface SqlQuery<E extends EntityMap> extends EntityRelationMap<E> {

    default String getSqlQuery() {
        return getColumnParam("SqlQuery");
    }

    default E setSqlQuery(String val) {
        setColumnParam("SqlQuery", val);
        return (E) this;
    }

    default Column<E, String, SqlQuery> colSqlQuery() {
        return new Column<E, String, SqlQuery>() {

            @Override
            public String getColumnName() {
                return "SqlQuery";
            }

            @Override
            public String getFieldName() {
                return "SqlQuery";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<String> getFieldClass() {
                return String.class;
            }

            @Override
            public String getValue(SqlQuery entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "SqlQuery");
                return entity.getSqlQuery();
            }

            @Override
            public void setValue(SqlQuery entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "SqlQuery");
                entity.setSqlQuery(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return getEntityName() + "." + getFieldName();
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null) return false;
                if (getClass() != obj.getClass()) return false;
                Column other = (Column) obj;
                return Objects.equals(hashCode(), other.hashCode());
            }
        };
    }

}