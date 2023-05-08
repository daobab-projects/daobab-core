package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;
import io.daobab.query.base.QueryType;

import java.util.Objects;

public interface RequestType<E extends EntityMap> extends EntityRelationMap<E> {

    default QueryType getRequestType() {
        return getColumnParam("RequestType");
    }

    @SuppressWarnings("unchecked")
    default E setRequestType(QueryType val) {
        setColumnParam("RequestType", val);
        return (E) this;
    }

    default Column<E, QueryType, RequestType<E>> colRequestType() {
        return new Column<E, QueryType, RequestType<E>>() {

            @Override
            public String getColumnName() {
                return "REQUEST_TYPE";
            }

            @Override
            public String getFieldName() {
                return "RequestType";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<QueryType> getFieldClass() {
                return QueryType.class;
            }

            @Override
            public QueryType getValue(RequestType<E> entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "RequestType");
                return entity.getRequestType();
            }

            @Override
            public void setValue(RequestType<E> entity, QueryType param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "RequestType");
                entity.setRequestType(param);
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
