package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;
import io.daobab.target.database.meta.column.dict.MetaRule;

import java.util.Objects;

public interface DeleteRule<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default MetaRule getDeleteRule() {
        return readParam("DeleteRule");
    }

    default E setDeleteRule(MetaRule val) {
        return storeParam("DeleteRule", val);
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "DeleteRule");
                return entity.getDeleteRule();
            }

            @Override
            public DeleteRule setValue(DeleteRule entity, MetaRule param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "DeleteRule");
                return (DeleteRule) entity.setDeleteRule(param);
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
