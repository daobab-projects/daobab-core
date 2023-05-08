package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface EntityRelation<E extends Entity> extends Entity {

    default E getEntity() {
        return (E) this;
    }


}
