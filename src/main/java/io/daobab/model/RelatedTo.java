package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface RelatedTo<E extends Entity> extends Entity {

    @SuppressWarnings("unchecked")
    default E getEntity() {
        return (E) this;
    }


}
