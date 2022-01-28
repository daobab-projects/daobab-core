package io.daobab.target.database;

import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.model.ProcedureParameters;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.Plates;
import io.daobab.target.database.query.*;
import io.daobab.transaction.Propagation;

import java.util.List;

/**
 * Exposes the very basic methods to handle CRUD operation
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface QueryDataBaseHandler extends Target, QueryHandler {

    <E extends Entity> E readEntity(DataBaseQueryEntity<E> query);

    <E extends Entity> Entities<E> readEntityList(DataBaseQueryEntity<E> query);

    <E extends Entity, F> F readField(DataBaseQueryField<E, F> query);

    <E extends Entity, F> List<F> readFieldList(DataBaseQueryField<E, F> query);

    Plate readPlate(DataBaseQueryPlate query);

    Plates readPlateList(DataBaseQueryPlate query);

    <E extends Entity> int delete(DataBaseQueryDelete<E> query, boolean transaction);

    <E extends Entity> int update(DataBaseQueryUpdate<E> query, boolean transaction);

    <E extends Entity> E insert(DataBaseQueryInsert<E> query, boolean transaction);

    <E extends Entity> int delete(DataBaseQueryDelete<E> query, Propagation propagation);

    <E extends Entity> int update(DataBaseQueryUpdate<E> query, Propagation propagation);

    <E extends Entity> E insert(DataBaseQueryInsert<E> query, Propagation propagation);

    <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> query);

    <O extends ProcedureParameters, I extends ProcedureParameters> O callProcedure(String name, I in, O out);

    long count(DataBaseQueryBase<?, ?> query);
}
