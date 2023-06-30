package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.model.TableColumn;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class SizeOnlyTableColumn extends TableColumn {
    public SizeOnlyTableColumn(int size) {
        super(new Column<Entity, Object, RelatedTo>() {
            @Override
            public String getFieldName() {
                return null;
            }

            @Override
            public Class<Object> getFieldClass() {
                return null;
            }

            @Override
            public Object getValue(RelatedTo entity) {
                return null;
            }

            @Override
            public RelatedTo setValue(RelatedTo entity, Object value) {
                return entity;
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
