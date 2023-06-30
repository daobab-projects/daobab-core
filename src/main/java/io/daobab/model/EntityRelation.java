package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface EntityRelation<E extends Entity> extends Entity {

    @SuppressWarnings("unchecked")
    default E getEntity() {
        return (E) this;
    }


}
