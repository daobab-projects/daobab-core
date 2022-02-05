package io.daobab.target.buffer;

import io.daobab.error.ColumnMandatory;
import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.statement.condition.SetField;
import io.daobab.statement.condition.SetFields;
import io.daobab.target.Target;
import io.daobab.target.buffer.query.*;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.transaction.OpenedTransactionBufferTarget;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unused"})
public interface BufferQueryTarget extends Target, BufferQueryHandler {


    OpenedTransactionBufferTarget beginTransaction();

    default <E extends Entity> BufferQueryInsert<E> insert(E entity) {
        return new BufferQueryInsert<>(this, entity);
    }

    default <E extends Entity> boolean insertAll(Collection<E> entities) {
        OpenedTransactionBufferTarget transactionTarget = this.beginTransaction();
        for (E entity : entities) {
            BufferQueryInsert<E> q = new BufferQueryInsert<>(transactionTarget, entity);
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

    default <E extends PrimaryKey<E, F, ?>, F> BufferQueryUpdate<E> update(E entity) {
        return new BufferQueryUpdate<>(this, entity);
    }

    default <E extends Entity> BufferQueryUpdate<E> updateWhole(E instanceToUpdate) {
        return new BufferQueryUpdate<>(this, instanceToUpdate, false);
    }


    default <E extends Entity, F, R extends EntityRelation> BufferQueryUpdate<E> update(Column<E, F, R> column, F value) {
        SetFields sf = new SetFields().setValue(column, value);
        return new BufferQueryUpdate<>(this, sf);
    }

    @SuppressWarnings("unchecked")
    default <E extends Entity> BufferQueryUpdate<E> update(SetField... sets) {
        SetFields sfs = null;

        for (SetField s : sets) {
            sfs = SetFields.setColumn(s.getField(), s.getValue());
        }
        return new BufferQueryUpdate<>(this, sfs);
    }

    default <E extends Entity> BufferQueryUpdate<E> update(SetFields set) {
        return new BufferQueryUpdate<>(this, set);
    }

    default <E extends Entity, F> BufferQueryField<E, F> select(Column<E, F, ?> col) {
        return new BufferQueryField<>(this, col);
    }


    //=====  FIELD LIST ======
    default <E extends Entity> BufferQueryEntity<E> select(E entity) {
        return new BufferQueryEntity<>(this, entity);
    }

    default <E extends Entity & PrimaryKey<E, F, R>, T extends Entity & EntityRelation<T>, F, R extends EntityRelation<?>> BufferQueryEntity<T> selectRelated(E entity, T rel) {
        return select(rel).whereEqual(entity.colID().transformTo(rel), entity.getId());
    }

    //=====  ENTITY LIST ======
    default <E extends Entity> Entities<E> findAll(E entity) {
        return new BufferQueryEntity<>(this, entity).findMany();
    }

    default <E extends Entity, F> List<F> findAll(Column<E, F, ?> column) {
        return new BufferQueryField<>(this, column).findMany();
    }

    default <E extends Entity & PrimaryKey<E, F, ? extends EntityRelation>, F> E findOneByPk(E entity, F id) {
        return new BufferQueryEntity<>(this, entity).whereEqual(entity.colID(), id).findOne();
    }

    default <E extends Entity & PrimaryKey<E, F, ? extends EntityRelation>, F> Entities<E> findManyByPk(E entity, F id) {
        return new BufferQueryEntity<>(this, entity).whereEqual(entity.colID(), id).findMany();
    }

    default <E extends Entity & PrimaryKey<E, F, ?>, F, V> V findOneByPk(Column<E, V, ?> column, F id) {
        return new BufferQueryField<>(this, column).whereEqual(column.getInstance().colID(), id).findOne();
    }

    default <E extends Entity & PrimaryCompositeKey<E, K>, K extends Composite> E findOneByPk(E entity, K key) {
        return new BufferQueryEntity<>(this, entity).whereEqual(entity.keyColumns(), key).findOne();
    }

    default <E extends Entity & PrimaryCompositeKey<E, K>, K extends Composite> Entities<E> findManyByPk(E entity, K key) {
        return new BufferQueryEntity<>(this, entity).whereEqual(entity.keyColumns(), key).findMany();
    }

    default <E extends EntityMap & PrimaryKey<E, F, ?>, F> E findFieldsByPk(F id, Column<E, ?, ?>... columns) {
        if (columns == null || columns.length == 0) throw new ColumnMandatory();
        BufferQueryPlate query = new BufferQueryPlate(this, columns).whereEqual(columns[0].getInstance().colID(), id);
        return query.findOneAs(columns[0].getEntityClass());
    }

    default <E extends EntityMap & PrimaryCompositeKey<E, K>, K extends Composite> E findFieldsByPk(K key, Column<E, ?, ?>... columns) {
        if (columns == null || columns.length == 0) throw new ColumnMandatory();
        BufferQueryPlate query = new BufferQueryPlate(this, columns).whereEqual(columns[0].getInstance().keyColumns(), key);
        return query.findOneAs(columns[0].getEntityClass());
    }

    default BufferQueryPlate select(Column<?, ?, ?>... columns) {
        return new BufferQueryPlate(this, columns);
    }

    default BufferQueryPlate select(Entity... entities) {
        return new BufferQueryPlate(this, entities);
    }

    default BufferQueryPlate select(List<? extends Column> columns) {
        return new BufferQueryPlate(this, columns);
    }

    default <E extends PrimaryKey<E, F, ?>, F> BufferQueryDelete<E> deletePkEntity(E entity) {
        return new BufferQueryDelete<>(this, entity);
    }

    default <E extends PrimaryCompositeKey<E, K>, K extends Composite> BufferQueryDelete<E> deletePkEntity(E entity) {
        return new BufferQueryDelete<>(this, entity);
    }

    default <E extends Entity> BufferQueryDelete<E> delete(E entity) {
        return new BufferQueryDelete<>(this, entity);
    }

    default <E extends PrimaryKey<E, ?, ?>> void deleteCollection(BufferQueryTarget target, Collection<E> entityCollection) {
        for (E entity : entityCollection) {
            target.delete(entity);
        }
    }

    default <E extends Entity> BufferIdGeneratorSupplier getPrimaryKeyGenerator(E entity) {
        throw new DaobabException("Provide a getPrimaryKeyGenerator() method into " + this.getClass().getName());
    }


}
