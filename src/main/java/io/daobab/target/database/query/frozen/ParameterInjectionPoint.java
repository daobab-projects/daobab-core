package io.daobab.target.database.query.frozen;

import io.daobab.converter.TypeConverter;

@SuppressWarnings("rawtypes")
public class ParameterInjectionPoint {

    private final DaoParam param;

    private final TypeConverter typeConverter;

    public ParameterInjectionPoint(DaoParam param, TypeConverter typeConverter) {
        this.param = param;
        this.typeConverter=typeConverter;
    }

    public DaoParam getParam() {
        return param;
    }

    public TypeConverter getTypeConverter() {
        return typeConverter;
    }
}
