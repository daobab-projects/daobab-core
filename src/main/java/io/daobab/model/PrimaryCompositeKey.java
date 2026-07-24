package io.daobab.model;

import io.daobab.error.DaobabException;
import io.daobab.query.base.QueryWhisperer;
import io.daobab.statement.condition.SetFields;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.DataBaseQueryDelete;
import io.daobab.target.database.transaction.OpenedTransactionDataBaseTarget;
import io.daobab.transaction.Propagation;


/**
 * An entity with a composite (multi-column) primary key ({@link #colCompositeId()}). The counterpart of
 * {@link PrimaryKey} for composite keys: it provides CRUD keyed by the whole composite ({@code insert} /
 * {@code update} / {@code delete} / {@code findById}) and related lookups, matching rows through
 * {@link #getCompositeKeyWhere(Composite)}. Optimistic concurrency is honored when the entity also implements
 * {@code OptimisticConcurrencyForPrimaryCompositeKey}.
 *
 * @param <E> the entity type
 * @param <K> the composite key type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface PrimaryCompositeKey<E extends Entity, K extends Composite> extends RelatedTo<E>, Composite<E>, QueryWhisperer {


    /**
     * The columns forming the composite primary key.
     */
    CompositeColumns<K> colCompositeId();

    /**
     * The {@code AND} where clause matching every key column against {@code keyEntity}'s values.
     *
     * @throws DaobabException when {@code keyEntity} is {@code null}
     */
    default Where getCompositeKeyWhere(Composite<E> keyEntity) {
        if (keyEntity == null) {
            throw new DaobabException("Composite key cannot be null");
        }
        WhereAnd where = new WhereAnd();
        for (TableColumn tableColumn : colCompositeId()) {
            Column column = tableColumn.getColumn();
            where.equal(column, column.getValue((RelatedTo) keyEntity));
        }
        return where;
    }

    /** Inserts this entity. */
    default E insert(QueryTarget target) {
        target.insert((E) this).execute();
        return (E) this;
    }

    /** Inserts this entity within (or without) a transaction. */
    default E insert(QueryTarget target, boolean transaction) {
        target.insert((E) this).execute(transaction);
        return (E) this;
    }

    /** Inserts this entity with the given transaction propagation. */
    default E insert(QueryTarget target, Propagation propagation) {
        target.insert((E) this).execute(propagation);
        return (E) this;
    }

    /** Deletes the row with this composite key; returns whether exactly one row was deleted. */
    default boolean delete(QueryTarget target) {
        return new DataBaseQueryDelete<>(target, (E) this).where(getCompositeKeyWhere(this)).execute() == 1;
    }

    /** Deletes the row with this composite key within (or without) a transaction. */
    default boolean delete(OpenedTransactionDataBaseTarget target, boolean transaction) {
        return new DataBaseQueryDelete<>(target, (E) this).where(getCompositeKeyWhere(this)).execute(transaction) == 1;
    }

    /** Deletes the row with this composite key with the given transaction propagation. */
    default boolean delete(OpenedTransactionDataBaseTarget target, Propagation propagation) {
        return new DataBaseQueryDelete<>(target, (E) this).where(getCompositeKeyWhere(this)).execute(propagation) == 1;
    }

    /** Updates the given columns of the row with this composite key (adding the OCC column when applicable). */
    default E update(QueryTarget target, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey occ) {
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
        target.update(SetFields.setValuesArray((E) this, columnsToUpdate)).where(getCompositeKeyWhere(this)).execute();
        return (E) this;
    }

    /** Updates the given columns of the row with this composite key, with the given transaction propagation. */
    default E update(QueryTarget target, Propagation propagation, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey occ) {
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
                newArray[columnsToUpdate.length + 1] = occ.getOCCColumn();
                columnsToUpdate = newArray;

            }
        }
        target.update(SetFields.setValuesArray((E) this, columnsToUpdate)).where(getCompositeKeyWhere(this)).execute(propagation);
        return (E) this;
    }

    /** Updates all the columns of the row with this composite key. */
    default E update(QueryTarget target) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey occ) {
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setInfoColumns((RelatedTo) this, columns().toArray(new TableColumn[0])))
                .where(getCompositeKeyWhere(this))
                .execute();
        return (E) this;
    }

    /** Updates all the columns of the row with this composite key, within (or without) a transaction. */
    default E update(OpenedTransactionDataBaseTarget target, boolean transaction) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey occ) {
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setValuesArray((E) this, columns().toArray(new Column[0])))
                .where(getCompositeKeyWhere(this))
                .execute(transaction);
        return (E) this;
    }


    /** Updates the given columns of the row with this composite key, within (or without) a transaction. */
    default E update(QueryTarget target, boolean transaction, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey occ) {
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
                newArray[columnsToUpdate.length + 1] = occ.getOCCColumn();
                columnsToUpdate = newArray;
            }
        }
        target.update(SetFields.setValuesArray((E) this, columnsToUpdate))
                .where(getCompositeKeyWhere(this))
                .execute(transaction);
        return (E) this;
    }

    /** Loads the entity with the given composite id. */
    default E findById(QueryTarget target, K id) {
        return target.select((E) this).where(getCompositeKeyWhere(this)).findOne();
    }

    /** Loads the entity whose column {@code column} holds {@code value}. */
    default <F, R extends RelatedTo> E findByColumnValue(QueryTarget target, Column<E, F, R> column, F value) {
        return target.select((E) this).whereEqual(column, value).findOne();
    }

    /** The SQL {@code UPDATE} statement (all columns) for this entity, terminated with {@code ;} - not executed. */
    default String getSqlUpdate(DataBaseTarget target) {
        return target.update(SetFields.setValuesArray((E) this, (Column<E, ?, ?>) target.getColumnsForTable(this))).where(getCompositeKeyWhere(this)) + ";";
    }

    /** The SQL {@code UPDATE} statement (given columns) for this entity, terminated with {@code ;} - not executed. */
    default String getSqlUpdate(DataBaseTarget target, Column<E, ?, ?>... columnsToUpdate) {
        return target.update(SetFields.setValuesArray((E) this, columnsToUpdate)).where(getCompositeKeyWhere(this)).getSQLQuery(target) + ";";
    }

    /** The single related {@code entity} matching this composite key and the extra condition. */
    default <T extends Entity & PrimaryCompositeKey, K extends Composite> T findRelatedOne(QueryTarget target, T entity, Where where) {
        return target.select(entity).where(new WhereAnd().and(getCompositeKeyWhere(this)).and(where)).findOne();
    }


}

