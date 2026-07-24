package io.daobab.model;

/**
 * An entity usable as the right-hand side of a relation (the target of a foreign key). {@link #getEntity()}
 * returns the entity itself, typed as {@code E}.
 *
 * @param <E> the concrete entity type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface RelatedTo<E extends Entity> extends Entity {

    /**
     * This entity, typed as {@code E}.
     */
    @SuppressWarnings("unchecked")
    default E getEntity() {
        return (E) this;
    }


}
