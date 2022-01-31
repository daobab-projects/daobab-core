package io.daobab.result;

import io.daobab.converter.JsonListHandler;
import io.daobab.error.NullConsumer;
import io.daobab.model.Entity;
import io.daobab.query.base.Query;
import io.daobab.target.QueryReceiver;
import io.daobab.target.QueryTarget;
import io.daobab.target.Target;
import io.daobab.target.multi.AboveMultiEntityTarget;
import io.daobab.target.multi.MultiEntityTarget;
import io.daobab.target.multi.SimpleMulti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Provides cached (in memory) entities
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface Entities<E extends Entity> extends EntitiesProvider<E>, Serializable, List<E>, Target, Cloneable, JsonListHandler, QueryTarget, QueryReceiver {

    default E findOne() {
        return findFirst().orElse(null);
    }

    Entities<E> calculateIndexes();

    Class<E> getEntityClass();

    @Override
    default EntityByteBuffer<E> createByteBuffer() {
        return new EntityByteBuffer<>(this);
    }

    Entities<E> copy();

    Entities<E> clone();

    void markRefreshCache();

    void refreshImmediately();

    List<E> filter(Query<E, ?> query);

    default MultiEntityTarget joinEntities(Entities<? extends Entity>... targets){
        List<Entities<? extends Entity>> list=new ArrayList<>();
        list.add(this);
        for (Entities<? extends Entity> e:targets){
            list.add(e);
        }
        return new SimpleMulti(list);
    }

}
