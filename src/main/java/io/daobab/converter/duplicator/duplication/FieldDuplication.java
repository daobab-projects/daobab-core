package io.daobab.converter.duplicator.duplication;

import io.daobab.converter.duplicator.Duplicator;
import io.daobab.converter.duplicator.DuplicatorManager;
import io.daobab.error.DaobabException;
import io.daobab.model.Field;

import java.util.List;
import java.util.stream.Collectors;

public class FieldDuplication<F> {

    final Field<?, ?, ?> targetField;

    final String fieldName;
    final Duplicator<F> duplicator;

    public FieldDuplication(Field<?, F, ?> field) {
        this(field, DuplicatorManager.INSTANCE);
    }

    @SuppressWarnings("unchecked")
    public FieldDuplication(Field<?, F, ?> field, DuplicatorManager manager) {
        targetField = field;
        fieldName = "\"" + field.getFieldName() + "\"";
        duplicator = (Duplicator<F>) manager.getConverter(field).orElseThrow(() -> new DaobabException("Cannot find a duplicator for %s", field.getFieldClass()));
    }

    public F duplicate(F value) {
            return duplicator.duplicate(value);
    }

    public List<F> duplicateList(List<F> values) {
        return values.stream().map(this::duplicate).collect(Collectors.toList());
    }


}
