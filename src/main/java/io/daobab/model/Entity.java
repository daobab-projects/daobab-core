package io.daobab.model;

import io.daobab.converter.json.JsonProvider;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

/**
 * The root of every Daobab entity: a row-like object that exposes its {@link ColumnsProvider columns}, knows its
 * own {@link #entityClass() class} and can be serialized to JSON. The seven {@code before*}/{@code after*} hooks
 * are lifecycle callbacks the target invokes around insert/update/delete/select.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface Entity extends ColumnsProvider, JsonProvider {


    /**
     * The entity's own class.
     */
    Class<? extends Entity> entityClass();

    /** Called on the entity just before it is inserted. */
    <T extends Target & QueryHandler> void beforeInsert(T target);

    /** Called on the entity just before it is updated. */
    <T extends Target & QueryHandler> void beforeUpdate(T target);

    /** Called on the entity just before it is deleted. */
    <T extends Target & QueryHandler> void beforeDelete(T target);

    /** Called on the entity just after it is selected. */
    <T extends Target & QueryHandler> void afterSelect(T target);

    /** Called on the entity just after it is inserted. */
    <T extends Target & QueryHandler> void afterInsert(T target);

    /** Called on the entity just after it is updated. */
    <T extends Target & QueryHandler> void afterUpdate(T target);

    /** Called on the entity just after it is deleted. */
    <T extends Target & QueryHandler> void afterDelete(T target);


}
