package io.daobab.model;

import io.daobab.query.base.QueryWhisperer;
import io.daobab.statement.condition.SetFields;
import io.daobab.statement.where.base.Where;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.DataBaseQueryDelete;
import io.daobab.target.database.query.DataBaseQueryPlate;
import io.daobab.target.database.transaction.OpenedTransactionDataBaseTarget;
import io.daobab.transaction.Propagation;

import java.util.List;

/**
 * An entity with a single-column primary key ({@link #colID()}). On top of the {@link QueryWhisperer} it gains a
 * rich set of default operations: reading and writing the id ({@link #getId()} / {@link #setId(Object)}), CRUD by
 * id ({@code insert}/{@code update}/{@code delete}/{@code findById}) and navigation to related entities
 * ({@code findRelatedOne}/{@code findRelatedMany}, including through a cross/junction table). Optimistic
 * concurrency is honored automatically when the entity also implements {@code OptimisticConcurrencyForPrimaryKey}.
 *
 * @param <E> the entity type
 * @param <F> the primary key value type
 * @param <R> the relation type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface PrimaryKey<E extends Entity, F, R extends RelatedTo> extends RelatedTo<E>, QueryWhisperer {

    /**
     * The primary key column.
     */
    Column<E, F, R> colID();

    /** The primary key value of this entity. */
    @SuppressWarnings("unchecked")
    default F getId() {
        return colID().getValue((R) this);
    }

    /** Sets the primary key value and returns the entity. */
    @SuppressWarnings("unchecked")
    default E setId(F val) {
        return (E) colID().setValue((R) this, val);
    }


    /** The sequence name from {@link TableInformation}, or {@code null} when the annotation is absent. */
    default String getSequenceName() {
        TableInformation idGenerator = this.getClass().getAnnotation(TableInformation.class);
        if (idGenerator == null) return null;
        return idGenerator.sequenceName();
    }

    /** The id-generation strategy from {@link TableInformation} ({@link IdGeneratorType#NONE} when absent). */
    default IdGeneratorType getIdGeneratorType() {
        TableInformation idGenerator = this.getClass().getAnnotation(TableInformation.class);
        if (idGenerator == null) return IdGeneratorType.NONE;
        return idGenerator.idGenerator();
    }

    /** The single row of {@code entity} whose key matches this id, projected to {@code columns}. */
    @SuppressWarnings("unchecked")
    default <T extends Entity & RelatedTo<T>> T findRelatedOne(QueryTarget target, T entity, Column<T, ?, ?>... columns) {
        return target.select(columns).whereEqual(colID().transformTo(entity), getId()).findOneAs(columns[0].entityClass());
    }

    /** The rows of {@code entity} whose key matches this id, projected to {@code columns}. */
    @SuppressWarnings("unchecked")
    default <T extends Entity & RelatedTo<T>> List<T> findRelatedMany(QueryTarget target, T entity, Column<T, ?, ?>... columns) {
        return target.select(columns).whereEqual(colID().transformTo(entity), getId()).findManyAs(columns[0].entityClass());
    }

    /** The single value of {@code col} whose row references this id. */
    @SuppressWarnings("rawtypes")
    default <T extends Entity & RelatedTo<T>, F1, R1 extends RelatedTo> F1 findRelatedOne(QueryTarget target, Column<T, F1, R1> col) {
        return target.select(col).whereEqual(colID().transformTo(col.getInstance()), getId()).findOne();
    }

    /** The single value of {@code col} whose row references this id and matches the extra condition. */
    @SuppressWarnings("rawtypes")
    default <T extends Entity & RelatedTo<T>, F1, R1 extends RelatedTo> F1 findRelatedOne(QueryTarget target, Column<T, F1, R1> col, Where where) {
        return target.select(col).where(and().equal(colID().transformTo(col.getInstance()), getId()).and(where)).findOne();
    }

    /** The values of {@code col} whose rows reference this id. */
    @SuppressWarnings("rawtypes")
    default <T extends Entity & RelatedTo<T>, F1, R1 extends RelatedTo> List<F1> findRelatedMany(QueryTarget target, Column<T, F1, R1> col) {
        return target.select(col).whereEqual(colID().transformTo(col.getInstance()), getId()).findMany();
    }

    /** The single related {@code entity} whose key matches this id. */
    default <R1 extends Entity & RelatedTo<R1>> R1 findRelatedOne(QueryTarget target, R1 entity) {
        return target.select(entity).whereEqual(colID().transformTo(entity), getId()).findOne();
    }

    /** The related {@code entity} rows whose key matches this id. */
    default <R1 extends Entity & RelatedTo<R1>> Entities<R1> findRelatedMany(QueryTarget target, R1 entity) {
        return target.select(entity).whereEqual(colID().transformTo(entity), getId()).findMany();
    }

    /** The entities reachable through the cross/junction table {@code cross} (many side). */
    @SuppressWarnings("unchecked")
    default <R1 extends RelatedTo<E1>, M extends Entity, T extends PrimaryKey<E1, F, R1>, E1 extends Entity> Entities<T> findRelatedManyByCross(QueryTarget target, M cross, T entityRV) {
        return target.select((T) entityRV.getEntity()).join(cross, colID()).join(entityRV, entityRV.colID().transformTo(cross)).whereEqual(colID(), getId()).findMany();
    }

    /** The single entity reachable through the cross/junction table {@code cross}. */
    @SuppressWarnings("unchecked")
    default <R1 extends RelatedTo<E1>, M extends Entity, T extends PrimaryKey<E1, F, R1>, E1 extends Entity> T findRelatedOneByCross(QueryTarget target, M cross, T entityRV) {
        return target.select((T) entityRV.getEntity()).join(cross, colID()).join(entityRV, entityRV.colID().transformTo(cross)).whereEqual(colID(), getId()).findOne();
    }

    /** The rows reachable through the cross/junction table {@code cross}, projected to {@code columns}. */
    @SuppressWarnings({"unchecked", "Duplicates"})
    default <M extends Entity, T extends Entity & PrimaryKey> List<T> findRelatedManyByCross(QueryTarget target, M cross, Column<T, ?, ?>... columns) {
        T entityRV = columns[0].getInstance();
        DataBaseQueryPlate dataBaseQueryPlate = target.select(columns).from(entityRV).join(cross, colID()).join(entityRV, entityRV.colID().transformTo(cross)).whereEqual(colID(), getId());
        return (List<T>) dataBaseQueryPlate.findManyAs((Class<? extends Entity>) columns[0].entityClass());
    }

    /** The single row reachable through the cross/junction table {@code cross}, projected to {@code columns}. */
    @SuppressWarnings({"unchecked", "Duplicates"})
    default <R1 extends RelatedTo<E1>, M extends Entity, T extends Entity & PrimaryKey<E1, F, R1>, E1 extends Entity> T findRelatedOneByCross(QueryTarget target, M cross, Column<T, ?, ?>... columns) {
        T entityRV = columns[0].getInstance();
        return target.select(columns).join(cross, colID()).join(entityRV, entityRV.colID().transformTo(cross)).whereEqual(colID(), getId()).findOneAs(columns[0].entityClass());
    }


    /** Inserts this entity. */
    @SuppressWarnings("unchecked")
    default E insert(QueryTarget target) {
        return target.insert((E) this).execute();
    }

    /** Inserts this entity within (or without) a transaction. */
    @SuppressWarnings("unchecked")
    default E insert(QueryTarget target, boolean transaction) {
        return target.insert((E) this).execute(transaction);
    }

    /** Inserts this entity with the given transaction propagation. */
    @SuppressWarnings("unchecked")
    default E insert(QueryTarget target, Propagation propagation) {
        return target.insert((E) this).execute(propagation);
    }

    /** Inserts or replaces this entity. */
    @SuppressWarnings("unchecked")
    default E replace(QueryTarget target) {
        return target.replace((E) this).execute();
    }

    /** Inserts or replaces this entity within (or without) a transaction. */
    @SuppressWarnings("unchecked")
    default E replace(QueryTarget target, boolean transaction) {
        return target.replace((E) this).execute(transaction);
    }

    /** Inserts or replaces this entity with the given transaction propagation. */
    @SuppressWarnings("unchecked")
    default E replace(QueryTarget target, Propagation propagation) {
        return target.replace((E) this).execute(propagation);
    }

    /** Deletes the row with this id; returns whether exactly one row was deleted. */
    @SuppressWarnings("unchecked")
    default boolean delete(QueryTarget target) {
        return new DataBaseQueryDelete<>(target, (E) this).whereEqual(colID(), getId()).execute() == 1;
    }

    /** Deletes the row with this id within (or without) a transaction. */
    @SuppressWarnings("unchecked")
    default boolean delete(OpenedTransactionDataBaseTarget target, boolean transaction) {
        return new DataBaseQueryDelete<>(target, (E) this).whereEqual(colID(), getId()).execute(transaction) == 1;
    }

    /** Deletes the row with this id with the given transaction propagation. */
    @SuppressWarnings("unchecked")
    default boolean delete(OpenedTransactionDataBaseTarget target, Propagation propagation) {
        return new DataBaseQueryDelete<>(target, (E) this).whereEqual(colID(), getId()).execute(propagation) == 1;
    }

    /** Updates the given columns of the row with this id (adding the OCC column when applicable). */
    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
    default E update(QueryTarget target, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey occ) {
            occ.handleOCC(target, this);

            boolean occColumnUpdated = false;
            for (Column<E, ?, ?> c : columnsToUpdate) {
                if (c.equalsColumn(occ.getOCCColumn())) {
                    occColumnUpdated = true;
                    break;
                }
            }

            if (!occColumnUpdated) {
                Column<E, ?, ?>[] newarray = new Column[columnsToUpdate.length + 1];
                System.arraycopy(columnsToUpdate, 0, newarray, 0, columnsToUpdate.length);
                newarray[columnsToUpdate.length] = occ.getOCCColumn();
                columnsToUpdate = newarray;

            }
        }
        target.update(SetFields.setValuesArray((E) this, columnsToUpdate)).whereEqual(colID(), getId()).execute();
        return (E) this;
    }

    /** Updates the given columns of the row with this id, with the given transaction propagation. */
    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
    default E update(QueryTarget target, Propagation propagation, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey occ) {
            occ.handleOCC(target, this);


            boolean occColumnUpdated = false;
            for (Column<E, ?, ?> c : columnsToUpdate) {
                if (c.equalsColumn(occ.getOCCColumn())) {
                    occColumnUpdated = true;
                    break;
                }
            }

            if (!occColumnUpdated) {
                Column<E, ?, ?>[] newArray = new Column[columnsToUpdate.length + 1];
                System.arraycopy(columnsToUpdate, 0, newArray, 0, columnsToUpdate.length);
                newArray[columnsToUpdate.length] = occ.getOCCColumn();
                columnsToUpdate = newArray;

            }
        }
        target.update(SetFields.setValuesArray((E) this, columnsToUpdate)).whereEqual(colID(), getId()).execute(propagation);
        return (E) this;
    }

    /** Updates all the columns of the row with this id. */
    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
    default E update(QueryTarget target) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey occ) {
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setInfoColumns((RelatedTo) this, target.getColumnsForTable(this).toArray(new TableColumn[0])))
                .whereEqual(colID(), getId())
                .execute();
        return (E) this;
    }

    /** Updates all the columns of the row with this id, within (or without) a transaction. */
    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
    default E update(OpenedTransactionDataBaseTarget target, boolean transaction) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey occ) {
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setValuesArray((E) this, target.getColumnsForTable(this).stream()
                        .map(TableColumn::getColumn)
                        .toArray(Column[]::new)))
                .whereEqual(colID(), getId())
                .execute(transaction);
        return (E) this;
    }


    /** Updates the given columns of the row with this id, within (or without) a transaction. */
    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
    default E update(QueryTarget target, boolean transaction, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey occ) {
            occ.handleOCC(target, this);

            boolean occColumnUpdated = false;
            for (Column<E, ?, ?> c : columnsToUpdate) {
                if (c.equalsColumn(occ.getOCCColumn())) {
                    occColumnUpdated = true;
                    break;
                }
            }

            if (!occColumnUpdated) {
                Column<E, ?, ?>[] newarray = new Column[columnsToUpdate.length + 1];
                System.arraycopy(columnsToUpdate, 0, newarray, 0, columnsToUpdate.length);
                newarray[columnsToUpdate.length] = occ.getOCCColumn();
                columnsToUpdate = newarray;

            }
        }
        target.update(SetFields.setValuesArray((E) this, columnsToUpdate))
                .whereEqual(colID(), getId())
                .execute(transaction);
        return (E) this;
    }

    /** Loads the entity with the given id. */
    @SuppressWarnings("unchecked")
    default E findById(QueryTarget target, F id) {
        return target.select((E) this).whereEqual(colID(), id).findOne();
    }

    /** Loads the entity whose column {@code column} holds {@code value}. */
    @SuppressWarnings("unchecked")
    default <F1, R1 extends RelatedTo> E findByColumnValue(QueryTarget target, Column<E, F1, R1> column, F1 value) {
        return target.select((E) this).whereEqual(column, value).findOne();
    }

    /** The SQL {@code UPDATE} statement (all columns) for this entity, terminated with {@code ;} - not executed. */
    @SuppressWarnings("unchecked")
    default String getSqlUpdate(DataBaseTarget target) {
        return target.update(SetFields.setValuesArray((E) this, (Column<E, ?, ?>) target.getColumnsForTable(this))).whereEqual(colID(), getId()) + ";";
    }

    /** The SQL {@code UPDATE} statement (given columns) for this entity, terminated with {@code ;} - not executed. */
    @SuppressWarnings("unchecked")
    default String getSqlUpdate(DataBaseTarget target, Column<E, ?, ?>... columnsToUpdate) {
        return target.update(SetFields.setValuesArray((E) this, columnsToUpdate)).whereEqual(colID(), getId()).getSQLQuery(target) + ";";
    }


}

