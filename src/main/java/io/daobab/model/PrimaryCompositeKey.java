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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface PrimaryCompositeKey<E extends Entity, K extends Composite> extends EntityRelation<E>, Composite<E>, QueryWhisperer {


    CompositeColumns<K> keyColumns();

    default Where getKeyWhere(Composite<E> keyEntity) {
        if (keyEntity == null) {
            throw new DaobabException("Composite key cannot be null");
        }
        WhereAnd where = new WhereAnd();
        for (TableColumn tCol : keyColumns()) {
            Column col = tCol.getColumn();
            where.equal(col, col.getValue((EntityRelation) keyEntity));
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
        return new DataBaseQueryDelete<>(target, (E) this).where(getKeyWhere(this)).execute() == 1;
    }

    default boolean delete(OpenedTransactionDataBaseTarget target, boolean transaction) {
        return new DataBaseQueryDelete<>(target, (E) this).where(getKeyWhere(this)).execute(transaction) == 1;
    }

    default boolean delete(OpenedTransactionDataBaseTarget target, Propagation propagation) {
        return new DataBaseQueryDelete<>(target, (E) this).where(getKeyWhere(this)).execute(propagation) == 1;
    }

    default E update(QueryTarget target, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey) {
            OptimisticConcurrencyForPrimaryKey occ = (OptimisticConcurrencyForPrimaryKey) this;
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
        target.update(SetFields.setColumns((E) this, columnsToUpdate)).where(getKeyWhere(this)).execute();
        return (E) this;
    }

    default E update(QueryTarget target, Propagation propagation, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey) {
            OptimisticConcurrencyForPrimaryKey occ = (OptimisticConcurrencyForPrimaryKey) this;
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
                newarray[columnsToUpdate.length + 1] = occ.getOCCColumn();
                columnsToUpdate = newarray;

            }
        }
        target.update(SetFields.setColumns((E) this, columnsToUpdate)).where(getKeyWhere(this)).execute(propagation);
        return (E) this;
    }

    default E update(QueryTarget target) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey) {
            OptimisticConcurrencyForPrimaryKey occ = (OptimisticConcurrencyForPrimaryKey) this;
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setInfoColumns((EntityRelation) this, columns().toArray(new TableColumn[columns().size()])))
                .where(getKeyWhere(this))
                .execute();
        return (E) this;
    }

    default E update(OpenedTransactionDataBaseTarget target, boolean transaction) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey) {
            OptimisticConcurrencyForPrimaryKey occ = (OptimisticConcurrencyForPrimaryKey) this;
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setColumns((E) this, columns().toArray(new Column[columns().size()])))
                .where(getKeyWhere(this))
                .execute(transaction);
        return (E) this;
    }


    default E update(QueryTarget target, boolean transaction, Column<E, ?, ?>... columnsToUpdate) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey) {
            OptimisticConcurrencyForPrimaryKey occ = (OptimisticConcurrencyForPrimaryKey) this;
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
                newarray[columnsToUpdate.length + 1] = occ.getOCCColumn();
                columnsToUpdate = newarray;

            }
        }
        target.update(SetFields.setColumns((E) this, columnsToUpdate))
                .where(getKeyWhere(this))
                .execute(transaction);
        return (E) this;
    }

    default E findById(QueryTarget target, K id) {
        return target.select((E) this).where(getKeyWhere(this)).findOne();
    }

    default <F, R extends EntityRelation> E findByColumnValue(QueryTarget target, Column<E, F, R> column, F value) {
        return target.select((E) this).whereEqual(column, value).findOne();
    }

    default String getSqlUpdate(DataBaseTarget target) {
        return target.update(SetFields.setColumns((E) this, (Column<E, ?, ?>) this.columns())).where(getKeyWhere(this)) + ";";
    }

    default String getSqlUpdate(DataBaseTarget target, Column<E, ?, ?>... columnsToUpdate) {
        return target.update(SetFields.setColumns((E) this, columnsToUpdate)).where(getKeyWhere(this)).getSQLQuery(target) + ";";
    }

}

