package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.model.TableColumn;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class SizeOnlyTableColumn extends TableColumn {
    public SizeOnlyTableColumn(int size) {
        super(new Column<Entity, Object, EntityRelation>() {
            @Override
            public String getFieldName() {
                return null;
            }

            @Override
            public Class<Object> getFieldClass() {
                return null;
            }

            @Override
            public Object getValue(EntityRelation entity) {
                return null;
            }

            @Override
            public void setValue(EntityRelation entity, Object value) {

            }

            @Override
            public Entity getInstance() {
                return null;
            }

            @Override
            public String getColumnName() {
                return null;
            }
        });

        this.size(size);
    }
}
