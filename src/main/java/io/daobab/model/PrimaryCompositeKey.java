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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface PrimaryCompositeKey<E extends Entity, K extends Composite> extends RelatedTo<E>, Composite<E>, QueryWhisperer {


    CompositeColumns<K> colCompositeId();

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

    default E insert(QueryTarget target) {
        target.insert((E) this).execute();
        return (E) this;
    }

    default E insert(QueryTarget target, boolean transaction) {
        target.insert((E) this).execute(transaction);
        return (E) this;
    }

    default E insert(QueryTarget target, Propagation propagation) {
        target.insert((E) this).execute(propagation);
        return (E) this;
    }

    default boolean delete(QueryTarget target) {
        return new DataBaseQueryDelete<>(target, (E) this).where(getCompositeKeyWhere(this)).execute() == 1;
    }

    default boolean delete(OpenedTransactionDataBaseTarget target, boolean transaction) {
        return new DataBaseQueryDelete<>(target, (E) this).where(getCompositeKeyWhere(this)).execute(transaction) == 1;
    }

    default boolean delete(OpenedTransactionDataBaseTarget target, Propagation propagation) {
        return new DataBaseQueryDelete<>(target, (E) this).where(getCompositeKeyWhere(this)).execute(propagation) == 1;
    }

    default E update(QueryTarget target, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey) {
            OptimisticConcurrencyForPrimaryCompositeKey occ = (OptimisticConcurrencyForPrimaryCompositeKey) this;
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

    default E update(QueryTarget target, Propagation propagation, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey) {
            OptimisticConcurrencyForPrimaryCompositeKey occ = (OptimisticConcurrencyForPrimaryCompositeKey) this;
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

    default E update(QueryTarget target) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey) {
            OptimisticConcurrencyForPrimaryCompositeKey occ = (OptimisticConcurrencyForPrimaryCompositeKey) this;
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setInfoColumns((RelatedTo) this, columns().toArray(new TableColumn[0])))
                .where(getCompositeKeyWhere(this))
                .execute();
        return (E) this;
    }

    default E update(OpenedTransactionDataBaseTarget target, boolean transaction) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey) {
            OptimisticConcurrencyForPrimaryCompositeKey occ = (OptimisticConcurrencyForPrimaryCompositeKey) this;
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setValuesArray((E) this, columns().toArray(new Column[0])))
                .where(getCompositeKeyWhere(this))
                .execute(transaction);
        return (E) this;
    }


    default E update(QueryTarget target, boolean transaction, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryCompositeKey) {
            OptimisticConcurrencyForPrimaryCompositeKey occ = (OptimisticConcurrencyForPrimaryCompositeKey) this;
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

    default E findById(QueryTarget target, K id) {
        return target.select((E) this).where(getCompositeKeyWhere(this)).findOne();
    }

    default <F, R extends RelatedTo> E findByColumnValue(QueryTarget target, Column<E, F, R> column, F value) {
        return target.select((E) this).whereEqual(column, value).findOne();
    }

    default String getSqlUpdate(DataBaseTarget target) {
        return target.update(SetFields.setValuesArray((E) this, (Column<E, ?, ?>) target.getColumnsForTable(this))).where(getCompositeKeyWhere(this)) + ";";
    }

    default String getSqlUpdate(DataBaseTarget target, Column<E, ?, ?>... columnsToUpdate) {
        return target.update(SetFields.setValuesArray((E) this, columnsToUpdate)).where(getCompositeKeyWhere(this)).getSQLQuery(target) + ";";
    }

    default <T extends Entity & PrimaryCompositeKey, K extends Composite> T findRelatedOne(QueryTarget target, T entity, Where where) {
        return target.select(entity).where(new WhereAnd().and(getCompositeKeyWhere(this)).and(where)).findOne();
    }


}

