package io.daobab.property;


public class DaobabPropertyGenerator extends DaobabPropertyCacheMapProvider {


    private final DaobabPropertyKey<Boolean> override;

    public DaobabPropertyGenerator() {
        override = new DaobabPropertyKey<>(this, "override", "false", PropertyReader::readBooleanSmall);
    }

    public Boolean getOverride() {
        return override.getValue();
    }

}
