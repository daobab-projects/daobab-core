package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Picture<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: PICTURE,
     * db type: VARBINARY
     */
    default byte[] getPicture() {
        return getColumnParam("Picture");
    }

    default E setPicture(byte[] val) {
        return setColumnParam("Picture", val);
    }

    default Column<E, byte[], Picture> colPicture() {
        return new Column<E, byte[], Picture>() {

            @Override
            public String getColumnName() {
                return "PICTURE";
            }

            @Override
            public String getFieldName() {
                return "Picture";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<byte[]> getFieldClass() {
                return byte[].class;
            }

            @Override
            public byte[] getValue(Picture entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Picture");
                return entity.getPicture();
            }

            @Override
            public Picture setValue(Picture entity, byte[] param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Picture");
                return (Picture) entity.setPicture(param);
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
