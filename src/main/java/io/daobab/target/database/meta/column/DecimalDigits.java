package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface DecimalDigits<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Integer getDecimalDigits() {
        return getColumnParam("DecimalDigits");
    }

    default E setDecimalDigits(Integer val) {
        return setColumnParam("DecimalDigits", val);
    }

    default Column<E, Integer, DecimalDigits> colDecimalDigits() {
        return new Column<E, Integer, DecimalDigits>() {

            @Override
            public String getColumnName() {
                return "DECIMAL_DIGITS";
            }

            @Override
            public String getFieldName() {
                return "DecimalDigits";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Integer> getFieldClass() {
                return Integer.class;
            }

            @Override
            public Integer getValue(DecimalDigits entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "DecimalDigits");
                return entity.getDecimalDigits();
            }

            @Override
            public DecimalDigits setValue(DecimalDigits entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "DecimalDigits");
                return (DecimalDigits) entity.setDecimalDigits(param);
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
