package io.daobab.target;

import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.query.*;
import io.daobab.query.base.Query;
import io.daobab.result.Entities;
import io.daobab.result.Plates;
import io.daobab.transaction.Propagation;

import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface QueryReceiver extends Target{

    <E extends Entity> E readEntity(QueryEntity<E> query);

    <E extends Entity> Entities<E> readEntityList(QueryEntity<E> query);

    <E extends Entity, F> F readField(QueryField<E, F> query);

    <E extends Entity, F> List<F> readFieldList(QueryField<E, F> query);

    Plate readPlate(QueryPlate query);

    Plates readPlateList(QueryPlate query);

    <E extends Entity> int delete(QueryDelete<E> query, boolean transaction);

    <E extends Entity> int update(QueryUpdate<E> query, boolean transaction);

    <E extends Entity> E insert(QueryInsert<E> query, boolean transaction);

    <E extends Entity> int delete(QueryDelete<E> query, Propagation propagation);

    <E extends Entity> int update(QueryUpdate<E> query, Propagation propagation);

    <E extends Entity> E insert(QueryInsert<E> query, Propagation propagation);

    <E extends Entity> String toSqlQuery(Query<E, ?> query);
}
