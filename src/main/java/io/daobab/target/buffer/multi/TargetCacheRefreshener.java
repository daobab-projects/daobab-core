package io.daobab.target.buffer.multi;

import io.daobab.target.buffer.single.Entities;

import java.util.List;

public interface TargetCacheRefreshener {

    default void add(Entities<?> daocached) {
        getTarget().add(daocached);
    }

    <C extends Entities<?>> List<C> getTarget();

    default void refreshAction() {
        getTarget().forEach(dao -> {
            if (refreshWhen(dao) && !skipWhen(dao))
                dao.refreshImmediately();
        });
    }

    default boolean refreshWhen(Entities<?> dao) {
        return true;
    }

    default boolean skipWhen(Entities<?> dao) {
        return false;
    }

    default void addAll(List<Entities<?>> daocached) {
        if (daocached == null) return;
        daocached.forEach(this::add);
    }

}
