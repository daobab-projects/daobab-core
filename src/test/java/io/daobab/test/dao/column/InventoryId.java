package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.math.BigDecimal;
import java.util.Objects;

public interface InventoryId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: INVENTORY_ID,
     * db type: INTEGER
     */
    default BigDecimal getInventoryId() {
        return readParam("InventoryId");
    }

    default E setInventoryId(BigDecimal val) {
        return storeParam("InventoryId", val);
    }

    default Column<E, BigDecimal, InventoryId> colInventoryId() {
        return new Column<E, BigDecimal, InventoryId>() {

            @Override
            public String getColumnName() {
                return "INVENTORY_ID";
            }

            @Override
            public String getFieldName() {
                return "InventoryId";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<BigDecimal> getFieldClass() {
                return BigDecimal.class;
            }

            @Override
            public BigDecimal getValue(InventoryId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "InventoryId");
                return entity.getInventoryId();
            }

            @Override
            public InventoryId setValue(InventoryId entity, BigDecimal param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "InventoryId");
                return (InventoryId) entity.setInventoryId(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return getEntityClass().getName() + "." + getFieldName();
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
