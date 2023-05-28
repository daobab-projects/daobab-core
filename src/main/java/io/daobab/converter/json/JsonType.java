package io.daobab.converter.json;

public interface JsonType<F> {

    void toJson(StringBuilder sb, F obj);

    F fromJson(String json);
}
