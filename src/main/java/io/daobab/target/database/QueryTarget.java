package io.daobab.target.database;

import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryColumn;
import io.daobab.model.*;
import io.daobab.statement.condition.SetField;
import io.daobab.statement.condition.SetFields;
import io.daobab.target.Target;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.database.converter.DatabaseConverterManager;
import io.daobab.target.database.converter.dateformat.DatabaseDateConverter;
import io.daobab.target.database.query.*;
import io.daobab.target.database.query.frozen.NativeDataBaseQueryEntity;
import io.daobab.target.database.query.frozen.NativeDataBaseQueryField;
import io.daobab.target.database.query.frozen.NativeDataBaseQueryPlate;
import io.daobab.target.database.transaction.OpenTransactionDataBaseTargetImpl;
import io.daobab.target.database.transaction.OpenedTransactionDataBaseTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 *
 * <p>
 * <p>
 * Enriches the target by all necessary and useful methods like select,find,insert,update etc...
 * May be implemented by DataBaseTarget only.
 * Provides all basic CRUD methods which are being available as a target methods
 * as a result of implementation.
 * Gathers interfaces Target and QueryDataBaseHandler
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface QueryTarget extends Target, QueryDataBaseHandler {

    OpenTransactionDataBaseTargetImpl beginTransaction();

    boolean getShowSql();

    default DatabaseConverterManager getConverterManager() {
        return null;
    }

    List<TableColumn> getColumnsForTable(final ColumnsProvider entity);

    default <E extends Entity> DataBaseQueryInsert<E> insert(E entity) {
        return new DataBaseQueryInsert<>(this, entity);
    }

    default <E extends Entity> boolean insertAll(Collection<E> entities) {
        OpenedTransactionDataBaseTarget transactionTarget = this.beginTransaction();
        for (E entity : entities) {
            DataBaseQueryInsert queryInsert = new DataBaseQueryInsert<>(transactionTarget, entity);
            queryInsert.execute(false);
        }
        transactionTarget.commit();
        return true;
    }

    default <E extends Entity> boolean insertAll(Collection<E> entities, int commitEvery) {
        Collection<E> sub = new ArrayList<>();
        entities.forEach(entity -> {
            sub.add(entity);
            if (sub.size() > commitEvery) {
                insertAll(sub);
            }
        });
        return insertAll(sub);
    }

    default <E extends PrimaryKey<E, F, ?>, F> DataBaseQueryUpdate<E> update(E entity) {
        return new DataBaseQueryUpdate<>(this, entity);
    }

    default <E extends Entity> DataBaseQueryUpdate<E> updateWhole(E instanceToUpdate) {
        return new DataBaseQueryUpdate<>(this, instanceToUpdate, false);
    }


    default <E extends Entity, F, R extends EntityRelation> DataBaseQueryUpdate<E> update(Column<E, F, R> column, F value) {
        SetFields sf = new SetFields().setValue(column, value);
        return new DataBaseQueryUpdate<>(this, sf);
    }

    @SuppressWarnings("unchecked")
    default <E extends Entity> DataBaseQueryUpdate<E> update(SetField... sets) {
        SetFields sfs = null;

        for (SetField s : sets) {
            sfs = SetFields.setColumn(s.getField(), s.getValue());
        }
        return new DataBaseQueryUpdate<>(this, sfs);
    }

    default <E extends Entity> DataBaseQueryUpdate<E> update(SetFields set) {
        return new DataBaseQueryUpdate<>(this, set);
    }

    default <E extends Entity, F> DataBaseQueryField<E, F> select(Column<E, F, ?> col) {
        return new DataBaseQueryField<>(this, col);
    }

    //=====  FIELD LIST ======
    default <E extends Entity> DataBaseQueryEntity<E> select(E entity) {
        return new DataBaseQueryEntity<>(this, entity);
    }

    default <E extends Entity & PrimaryKey<E, F, R>, T extends Entity & EntityRelation<T>, F, R extends EntityRelation<?>> DataBaseQueryEntity<T> selectRelated(E entity, T rel) {
        return select(rel).whereEqual(entity.colID().transformTo(rel), entity.getId());
    }

    //=====  ENTITY LIST ======
    default <E extends Entity> Entities<E> findAll(E entity) {
        return new DataBaseQueryEntity<>(this, entity).findMany();
    }

    default <E extends Entity, F> List<F> findAll(Column<E, F, ?> column) {
        return new DataBaseQueryField<>(this, column).findMany();
    }

    default <E extends Entity & PrimaryKey<E, F, ? extends EntityRelation>, F> E findOneByPk(E entity, F id) {
        return new DataBaseQueryEntity<>(this, entity).whereEqual(entity.colID(), id).findOne();
    }

    default <E extends Entity & PrimaryKey<E, F, ? extends EntityRelation>, F> Entities<E> findManyByPk(E entity, F id) {
        return new DataBaseQueryEntity<>(this, entity).whereEqual(entity.colID(), id).findMany();
    }

    default <E extends Entity & PrimaryKey<E, F, ?>, F, V> V findOneByPk(Column<E, V, ?> column, F id) {
        return new DataBaseQueryField<>(this, column).whereEqual(column.getInstance().colID(), id).findOne();
    }

    default <E extends Entity & PrimaryCompositeKey<E, K>, K extends Composite> E findOneByPk(E entity, K key) {
        return new DataBaseQueryEntity<>(this, entity).whereEqual(entity.colCompositeId(), key).findOne();
    }

    default <E extends Entity & PrimaryCompositeKey<E, K>, K extends Composite> Entities<E> findManyByPk(E entity, K key) {
        return new DataBaseQueryEntity<>(this, entity).whereEqual(entity.colCompositeId(), key).findMany();
    }

    default <E extends EntityMap & PrimaryKey<E, F, ?>, F> E findFieldsByPk(F id, Column<E, ?, ?>... columns) {
        if (columns == null || columns.length == 0) throw new MandatoryColumn();
        DataBaseQueryPlate query = new DataBaseQueryPlate(this, columns).whereEqual(columns[0].getInstance().colID(), id);
        return query.findOneAs(columns[0].getEntityClass());
    }

    default <E extends EntityMap & PrimaryCompositeKey<E, K>, K extends Composite> E findFieldsByPk(K key, Column<E, ?, ?>... columns) {
        if (columns == null || columns.length == 0) throw new MandatoryColumn();
        DataBaseQueryPlate query = new DataBaseQueryPlate(this, columns).whereEqual(columns[0].getInstance().colCompositeId(), key);
        return query.findOneAs(columns[0].getEntityClass());
    }

    default DataBaseQueryPlate select(Column<?, ?, ?>... columns) {
        return new DataBaseQueryPlate(this, columns);
    }

    default DataBaseQueryPlate select(Entity... entities) {
        return new DataBaseQueryPlate(this, entities);
    }

    default DataBaseQueryPlate select(List<? extends Column> columns) {
        return new DataBaseQueryPlate(this, columns);
    }

    default <E extends PrimaryKey<E, F, ?>, F> DataBaseQueryDelete<E> deletePkEntity(E entity) {
        return new DataBaseQueryDelete<>(this, entity);
    }

    default <E extends PrimaryCompositeKey<E, K>, K extends Composite> DataBaseQueryDelete<E> deletePkEntity(E entity) {
        return new DataBaseQueryDelete<>(this, entity);
    }

    default <E extends Entity> DataBaseQueryDelete<E> delete(E entity) {
        return new DataBaseQueryDelete<>(this, entity);
    }

    default <E extends PrimaryKey<E, ?, ?>> void deleteCollection(QueryTarget target, Collection<E> entityCollection) {
        for (E entity : entityCollection) {
            target.delete(entity);
        }
    }

    //  NATIVE
    default <E extends Entity, F> NativeDataBaseQueryField<E, F> nativeSelect(String nativeQuery, Column<E, F, ?> col) {
        return new NativeDataBaseQueryField<>(nativeQuery, this, col);
    }

    default <E extends Entity> NativeDataBaseQueryEntity<E> nativeSelect(String nativeQuery, E entity) {
        return new NativeDataBaseQueryEntity<>(nativeQuery,this, entity);
    }

    default NativeDataBaseQueryPlate nativeSelect(String nativeQuery, Column<?, ?, ?>... col) {
        return new NativeDataBaseQueryPlate(nativeQuery, this, col);
    }

    default <E extends Entity> DataBaseIdGeneratorSupplier getPrimaryKeyGenerator(E entity) {
        throw new DaobabException("Provide a getPrimaryKeyGenerator() method into " + this.getClass().getName());
    }

    DatabaseDateConverter getDatabaseDateConverter();


}
