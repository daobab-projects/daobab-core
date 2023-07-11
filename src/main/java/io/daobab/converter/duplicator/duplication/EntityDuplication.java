package io.daobab.converter.duplicator.duplication;

import io.daobab.converter.duplicator.DuplicatorManager;
import io.daobab.creation.EntityCreator;
import io.daobab.error.DaobabDeveloperException;
import io.daobab.error.DaobabEntityCreationException;
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
        this.entity = entity;
        fields = entity.columns().stream().map(TableColumn::getColumn).collect(Collectors.toList());
        fieldJsonConversions = entity.columns().stream().map(TableColumn::getColumn).map(c -> new FieldDuplication(c, jsonConverterManager)).collect(Collectors.toList());
    }

    public static String getEntityNameByInstance(Entity entity, ILoggerBean loggerBean) {
        return getEntityName(entity.entityClass(), loggerBean);
    }

    public static String getEntityName(Class<? extends Entity> clazz, ILoggerBean loggerBean) {
        TableInformation annotation = clazz.getAnnotation(TableInformation.class);
        if (annotation == null) {
            if (loggerBean != null) {
                loggerBean.getLog().warn(format("Entity %s is has no %s annotation. Class name used instead.", clazz.getName(), TableInformation.class.getSimpleName()));
            }
            return clazz.getName();
        }

        String entityName = annotation.name().trim();
        if (annotation.useMethod() && !entityName.isEmpty()) {
            throw new DaobabDeveloperException("%s annotation in class %s contains table name, but if parameter useMethod=true is in use, table name shouldn't be provided ", TableInformation.class.getSimpleName(), clazz.getSimpleName());
        } else if (annotation.useMethod()) {
            Entity entity = EntityCreator.createEntity(clazz);
            if (!(entity instanceof TableNameMethod)) {
                throw new DaobabDeveloperException("Entity %s has %s annotation specified with useMethod parameter. In that case entity has to implement %s", entity.entityClass().getName(), TableInformation.class.getName(), TableNameMethod.class.getName());
            }
            TableNameMethod tableNameMethod = (TableNameMethod) entity;
            return tableNameMethod.tableName();
        }

        if (entityName.isEmpty()) {
            throw new DaobabDeveloperException("%s annotation doesn't contain any table name for entity %s", TableInformation.class.getSimpleName(), clazz.getSimpleName());
        }
        return entityName;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public E duplicate(E entity) {
        if (entity == null) return null;

        E rv;
        try {
            rv = (E) entity.entityClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new DaobabEntityCreationException(entity.entityClass(), e);
        }


        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            FieldDuplication fieldJsonConversion = fieldJsonConversions.get(i);
            rv = (E) field.setValue((RelatedTo) rv, fieldJsonConversion.duplicate(field.getValue((RelatedTo) entity)));

        }
        return rv;
    }

}
