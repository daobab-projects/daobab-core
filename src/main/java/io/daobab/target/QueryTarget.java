package io.daobab.target;

import io.daobab.error.ColumnMandatory;
import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.query.*;
import io.daobab.result.Entities;
import io.daobab.statement.condition.SetField;
import io.daobab.statement.condition.SetFields;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")
public interface QueryTarget extends Target, QueryReceiver {

    default <E extends Entity> QueryInsert<E> insert(E entity) {
        return new QueryInsert<>(this, entity);
    }

    default <E extends Entity> boolean insertAll(Collection<E> entities) {
        OpenedTransactionTarget transactionTarget = this.beginTransaction();
        for (E entity : entities) {
            QueryInsert q = new QueryInsert<>(transactionTarget, entity);
            q.execute(false);
        }
        transactionTarget.commit();
        return true;
    }

    default <E extends Entity> boolean insertAll(Collection<E> entities, int commitEvery) {
        Collection<E> sub = new LinkedList<>();
        entities.forEach(entity -> {
            sub.add(entity);
            if (sub.size() > commitEvery) {
                insertAll(sub);
            }
        });
        return insertAll(sub);
    }

    default <E extends PrimaryKey<E, F, ?>, F> QueryUpdate<E> update(E entity) {
        return new QueryUpdate<>(this, entity);
    }

    default <E extends Entity> QueryUpdate<E> updateWhole(E instanceToUpdate) {
        return new QueryUpdate<>(this, instanceToUpdate, false);
    }


    default <E extends Entity, F, R extends EntityRelation> QueryUpdate<E> update(Column<E, F, R> column, F value) {
        SetFields sf = new SetFields().setValue(column, value);
        return new QueryUpdate<>(this, sf);
    }

    @SuppressWarnings("unchecked")
    default <E extends Entity> QueryUpdate<E> update(SetField... sets) {
        SetFields sfs = null;

        for (SetField s : sets) {
            sfs = SetFields.setColumn(s.getField(), s.getValue());
        }
        return new QueryUpdate<>(this, sfs);
    }

    default <E extends Entity> QueryUpdate<E> update(SetFields set) {
        return new QueryUpdate<>(this, set);
    }

    default <E extends Entity, F> QueryField<E, F> select(Column<E, F, ?> col) {
        return new QueryField<>(this, col);
    }


    //=====  FIELD LIST ======
    default <E extends Entity> QueryEntity<E> select(E entity) {
        return new QueryEntity<>(this, entity);
    }

    default <E extends Entity & PrimaryKey<E, F, R>, T extends Entity & EntityRelation<T>, F, R extends EntityRelation<?>> QueryEntity<T> selectRelated(E entity, T rel) {
        return select(rel).whereEqual(entity.colID().transformTo(rel), entity.getId());
    }

    //=====  ENTITY LIST ======
    default <E extends Entity> Entities<E> findAll(E entity) {
        return new QueryEntity<>(this, entity).findMany();
    }

    default <E extends Entity, F> List<F> findAll(Column<E, F, ?> column) {
        return new QueryField<>(this, column).findMany();
    }

    default <E extends Entity & PrimaryKey<E, F, ? extends EntityRelation>, F> E findOneByPk(E entity, F id) {
        return new QueryEntity<>(this, entity).whereEqual(entity.colID(), id).findOne();
    }

    default <E extends Entity & PrimaryKey<E, F, ? extends EntityRelation>, F> Entities<E> findManyByPk(E entity, F id) {
        return new QueryEntity<>(this, entity).whereEqual(entity.colID(), id).findMany();
    }

    default <E extends Entity & PrimaryKey<E, F, ?>, F, V> V findOneByPk(Column<E, V, ?> column, F id) {
        return new QueryField<>(this, column).whereEqual(column.getInstance().colID(), id).findOne();
    }

    default <E extends Entity & PrimaryCompositeKey<E, K>, K extends Composite> E findOneByPk(E entity, K key) {
        return new QueryEntity<>(this, entity).whereEqual(entity.keyColumns(), key).findOne();
    }

    default <E extends Entity & PrimaryCompositeKey<E, K>, K extends Composite> Entities<E> findManyByPk(E entity, K key) {
        return new QueryEntity<>(this, entity).whereEqual(entity.keyColumns(), key).findMany();
    }

    default <E extends EntityMap & PrimaryKey<E, F, ?>, F> E findFieldsByPk(F id, Column<E, ?, ?>... columns) {
        if (columns == null || columns.length == 0) throw new ColumnMandatory();
        QueryPlate query = new QueryPlate(this, columns).whereEqual(columns[0].getInstance().colID(), id);
        return query.findOneAs(columns[0].getEntityClass());
    }

    default <E extends EntityMap & PrimaryCompositeKey<E, K>, K extends Composite> E findFieldsByPk(K key, Column<E, ?, ?>... columns) {
        if (columns == null || columns.length == 0) throw new ColumnMandatory();
        QueryPlate query = new QueryPlate(this, columns).whereEqual(columns[0].getInstance().keyColumns(), key);
        return query.findOneAs(columns[0].getEntityClass());
    }

    default QueryPlate select(Column<?, ?, ?>... columns) {
        return new QueryPlate(this, columns);
    }

    default QueryPlate select(Entity... entities) {
        return new QueryPlate(this, entities);
    }

    default QueryPlate select(List<? extends Column> columns) {
        return new QueryPlate(this, columns);
    }

    default <E extends PrimaryKey<E, F, ?>, F> QueryDelete<E> deletePkEntity(E entity) {
        return new QueryDelete<>(this, entity);
    }

    default <E extends PrimaryCompositeKey<E, K>, K extends Composite> QueryDelete<E> deletePkEntity(E entity) {
        return new QueryDelete<>(this, entity);
    }

    default <E extends Entity> QueryDelete<E> delete(E entity) {
        return new QueryDelete<>(this, entity);
    }

    default <E extends PrimaryKey<E, ?, ?>> void deleteCollection(QueryTarget target, Collection<E> entityCollection) {
        for (E entity : entityCollection) {
            target.delete(entity);
        }
    }

    //  NATIVE
    default <E extends Entity, F> QueryField<E, F> nativeSelect(String nativeQuery, Column<E, F, ?> col) {
        return new QueryField<>(nativeQuery, this, col);
    }

    default <E extends Entity> QueryEntity<E> nativeSelect(String nativeQuery, E entity) {
        return new QueryEntity<>(nativeQuery, this, entity);
    }

    default QueryPlate nativeSelect(String nativeQuery, Column<?, ?, ?>... col) {
        return new QueryPlate(nativeQuery, this, col);
    }

    default <E extends Entity> IdGeneratorSupplier getPrimaryKeyGenerator(E entity) {
        throw new DaobabException("Provide a getPrimaryKeyGenerator() method into " + this.getClass().getName());
    }


}
