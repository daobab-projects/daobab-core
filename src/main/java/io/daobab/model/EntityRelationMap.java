package io.daobab.model;

/**
 * A related entity that is also parameter-map backed - the combination of {@link RelatedTo} and
 * {@link MapHandler} used as a common bound for the shared column interfaces.
 *
 * @param <E> the entity type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface EntityRelationMap<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


}
