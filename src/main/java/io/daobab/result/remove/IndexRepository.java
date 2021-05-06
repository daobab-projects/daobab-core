package io.daobab.result.remove;

import io.daobab.model.Entity;

import java.util.HashMap;

public class IndexRepository<E extends Entity> extends HashMap<String, Index<E, ?>> {

//    public  IndexRepository(List<E> entities) {
//        if (size() < 500) {
//            log.info("No indexes");
//            return;
//        }
//        E entity = entities.get(0);
//
//        List<Column> columns = entity.columns();
//        for (Column<E, ?, EntityRelation> col : columns) {
//            Index index;
//            if (Number.class.isAssignableFrom(col.getFieldClass())) {
//                index=new IndexNumber<>(col, this);
//                if (!index.isWorthless()) put(col.getColumnName(),index);
//            }else if (String.class.isAssignableFrom(col.getFieldClass()) || col.getFieldClass().isEnum()) {
//                index=new IndexString<>(col, this);
//                if (!index.isWorthless()) put(col.getColumnName(), index);
//            }else if (Date.class.isAssignableFrom(col.getFieldClass())) {
//                index=new IndexDate<>(col, this);
//                if (!index.isWorthless()) put(col.getColumnName(), index);
//            }
//        }
//    }
}
