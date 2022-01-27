package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;
import io.daobab.target.meta.column.dict.MetaRule;

import java.util.Objects;

public interface DeleteRule<E extends EntityMap> extends EntityRelationMap<E> {

    default MetaRule getDeleteRule() {
        return getColumnParam("DeleteRule");
    }

    default E setDeleteRule(MetaRule val) {
        setColumnParam("DeleteRule", val);
        return (E) this;
    }

    default Column<E, MetaRule, DeleteRule> colDeleteRule() {
        return new Column<E, MetaRule, DeleteRule>() {

            @Override
            public String getColumnName() {
                return "DELETE_RULE";
            }

            @Override
            public String getFieldName() {
                return "DeleteRule";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<MetaRule> getFieldClass() {
                return MetaRule.class;
            }

            @Override
            public MetaRule getValue(DeleteRule entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "DeleteRule");
                return entity.getDeleteRule();
            }

            @Override
            public void setValue(DeleteRule entity, MetaRule param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "DeleteRule");
                entity.setDeleteRule(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return getDeleteRule().toString();
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