package io.daobab.target.buffer;

import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;
import io.daobab.target.buffer.query.*;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.Plates;
import io.daobab.transaction.Propagation;

import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface BufferQueryHandler extends Target, QueryHandler {

    <E extends Entity> E readEntity(BufferQueryEntity<E> query);

    <E extends Entity> Entities<E> readEntityList(BufferQueryEntity<E> query);

    <E extends Entity, F> F readField(BufferQueryField<E, F> query);

    <E extends Entity, F> List<F> readFieldList(BufferQueryField<E, F> query);

    Plate readPlate(BufferQueryPlate query);

    Plates readPlateList(BufferQueryPlate query);

    <E extends Entity> int delete(BufferQueryDelete<E> query, boolean transaction);

    <E extends Entity> int update(BufferQueryUpdate<E> query, boolean transaction);

    <E extends Entity> E insert(BufferQueryInsert<E> query, boolean transaction);

    <E extends Entity> int delete(BufferQueryDelete<E> query, Propagation propagation);

    <E extends Entity> int update(BufferQueryUpdate<E> query, Propagation propagation);

    <E extends Entity> E insert(BufferQueryInsert<E> query, Propagation propagation);

}
