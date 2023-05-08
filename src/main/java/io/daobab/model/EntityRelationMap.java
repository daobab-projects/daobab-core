package io.daobab.model;

import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface EntityRelationMap<E extends EntityMap> extends EntityRelation<E>, Map<String, Object>, MapParameterHandler {


}
