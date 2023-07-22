package io.daobab.creation;

import io.daobab.converter.json.JsonType;

public abstract class TypeIntroducer<T> implements JsonType<T> {

    public abstract Class<T> getTypeClass();


}
