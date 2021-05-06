package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;
import io.daobab.test.dao.Lang;

import java.util.Objects;

public interface NameLang<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: NAME,
     * db type: VARCHAR
     */
    default Lang getName() {
        return getColumnParam("Name");
    }

    default E setName(Lang val) {
        setColumnParam("Name", val);
        return (E) this;
    }

    default Column<E, Lang, NameLang> colName() {
        return new Column<E, Lang, NameLang>() {

            @Override
            public String getColumnName() {
                return "NAME";
            }

            @Override
            public String getFieldName() {
                return "Name";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Lang> getFieldClass() {
                return Lang.class;
            }

            @Override
            public Lang getValue(NameLang entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Name");
                return entity.getName();
            }


            @Override
            public void setValue(NameLang entity, Lang param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Name");
                entity.setName(param);
            }

            public void setValue(NameLang entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Name");
                entity.setName(Lang.valueOf(param));
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