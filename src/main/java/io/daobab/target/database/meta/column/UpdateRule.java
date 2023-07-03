package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;
import io.daobab.target.database.meta.column.dict.MetaRule;

import java.util.Objects;

public interface UpdateRule<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default MetaRule getUpdateRule() {
        return readParam("UpdateRule");
    }

    default E setUpdateRule(MetaRule val) {
        return storeParam("UpdateRule", val);
    }

    default Column<E, MetaRule, UpdateRule> colUpdateRule() {
        return new Column<E, MetaRule, UpdateRule>() {

            @Override
            public String getColumnName() {
                return "UPDATE_RULE";
            }

            @Override
            public String getFieldName() {
                return "UpdateRule";
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
            public MetaRule getValue(UpdateRule entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "UpdateRule");
                return entity.getUpdateRule();
            }

            @Override
            public UpdateRule setValue(UpdateRule entity, MetaRule param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "UpdateRule");
                return (UpdateRule) entity.setUpdateRule(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return getUpdateRule().toString();
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
