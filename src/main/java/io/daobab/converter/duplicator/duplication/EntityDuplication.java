package io.daobab.converter.duplicator.duplication;

import io.daobab.clone.EntityDuplicator;
import io.daobab.converter.duplicator.DuplicatorManager;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.DaobabException;
import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class EntityDuplication<E extends Entity> {

    @SuppressWarnings("rawtypes")
    private final List<FieldDuplication> fieldJsonConversions;

    @SuppressWarnings("rawtypes")
    private final List<Field> fields;

    E entity;


    public EntityDuplication(E entity) {
        this(entity, DuplicatorManager.INSTANCE);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public EntityDuplication(E entity, DuplicatorManager jsonConverterManager) {
        this.entity=entity;
        fields = entity.columns().stream().map(TableColumn::getColumn).collect(Collectors.toList());
        fieldJsonConversions = entity.columns().stream().map(TableColumn::getColumn).map(c -> new FieldDuplication(c, jsonConverterManager)).collect(Collectors.toList());
    }

    public static String getEntityNameByInstance(Entity entity, ILoggerBean loggerBean) {
        return getEntityName(entity.getEntityClass(), loggerBean);
    }

    public static String getEntityName(Class<? extends Entity> clazz, ILoggerBean loggerBean) {
        TableName annotation = clazz.getAnnotation(TableName.class);
        if (annotation == null) {
            if (loggerBean != null) {
                loggerBean.getLog().warn(format("Entity %s is has no %s annotation. Class name used instead.", clazz.getName(), TableName.class.getSimpleName()));
            }
            return clazz.getName();
        }

        String entityName = annotation.value().trim();
        if (annotation.useMethod() && !entityName.isEmpty()) {
            throw new DaobabException("%s annotation in class %s contains table name, but if parameter useMethod=true is in use, table name shouldn't be provided ", TableName.class.getSimpleName(), clazz.getSimpleName());
        } else if (annotation.useMethod()) {
            Entity entity = EntityDuplicator.createEntity(clazz);
            TableNameMethod tableNameMethod = (TableNameMethod) entity;
            return tableNameMethod.getEntityName();
        }

        if (entityName.isEmpty()) {
            throw new DaobabException("%s annotation doesn't contain any table name for entity %s", TableName.class.getSimpleName(), clazz.getSimpleName());
        }
        return entityName;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public E duplicate(E entity) {
        if (entity == null) return null;

        E rv;
        try {
            rv = (E) entity.getEntityClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new DaobabEntityCreationException(entity.getEntityClass(), e);
        }


        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            FieldDuplication fieldJsonConversion = fieldJsonConversions.get(i);
            rv = (E) field.setValue((RelatedTo) rv, fieldJsonConversion.duplicate(field.getValue((RelatedTo) entity)));

        }
        return rv;
    }

}
