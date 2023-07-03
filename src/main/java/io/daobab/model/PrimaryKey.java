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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface PrimaryKey<E extends Entity, F, R extends RelatedTo> extends RelatedTo<E>, QueryWhisperer {

    Column<E, F, R> colID();

    @SuppressWarnings("unchecked")
    default F getId() {
        return colID().getValue((R) this);
    }

    @SuppressWarnings("unchecked")
    default E setId(F val) {
        return (E) colID().setValue((R) this, val);
    }


    default String getSequenceName() {
        IdGenerator idgenerator = this.getClass().getAnnotation(IdGenerator.class);
        if (idgenerator == null) return null;
        return idgenerator.sequenceName();
    }

    default IdGeneratorType getIdGeneratorType() {
        IdGenerator idgenerator = this.getClass().getAnnotation(IdGenerator.class);
        if (idgenerator == null) return IdGeneratorType.NONE;
        return idgenerator.type();
    }

    @SuppressWarnings("unchecked")
    default <T extends Entity & RelatedTo<T>> T findRelatedOne(QueryTarget target, T entity, Column<T, ?, ?>... columns) {
        return target.select(columns).whereEqual(colID().transformTo(entity), getId()).findOneAs(columns[0].entityClass());
    }

    @SuppressWarnings("unchecked")
    default <T extends Entity & RelatedTo<T>> List<T> findRelatedMany(QueryTarget target, T entity, Column<T, ?, ?>... columns) {
        return target.select(columns).whereEqual(colID().transformTo(entity), getId()).findManyAs(columns[0].entityClass());
    }

    @SuppressWarnings("rawtypes")
    default <T extends Entity & RelatedTo<T>, F1, R1 extends RelatedTo> F1 findRelatedOne(QueryTarget target, Column<T, F1, R1> col) {
        return target.select(col).whereEqual(colID().transformTo(col.getInstance()), getId()).findOne();
    }

    @SuppressWarnings("rawtypes")
    default <T extends Entity & RelatedTo<T>, F1, R1 extends RelatedTo> F1 findRelatedOne(QueryTarget target, Column<T, F1, R1> col, Where where) {
        return target.select(col).where(and().equal(colID().transformTo(col.getInstance()), getId()).and(where)).findOne();
    }

    @SuppressWarnings("rawtypes")
    default <T extends Entity & RelatedTo<T>, F1, R1 extends RelatedTo> List<F1> findRelatedMany(QueryTarget target, Column<T, F1, R1> col) {
        return target.select(col).whereEqual(colID().transformTo(col.getInstance()), getId()).findMany();
    }

    default <R1 extends Entity & RelatedTo<R1>> R1 findRelatedOne(QueryTarget target, R1 entity) {
        return target.select(entity).whereEqual(colID().transformTo(entity), getId()).findOne();
    }

    default <R1 extends Entity & RelatedTo<R1>> Entities<R1> findRelatedMany(QueryTarget target, R1 entity) {
        return target.select(entity).whereEqual(colID().transformTo(entity), getId()).findMany();
    }

    @SuppressWarnings("unchecked")
    default <R1 extends RelatedTo<E1>, M extends Entity, T extends PrimaryKey<E1, F, R1>, E1 extends Entity> Entities<T> findRelatedManyByCross(QueryTarget target, M cross, T entityRV) {
        return target.select((T) entityRV.getEntity()).join(cross, colID()).join(entityRV, entityRV.colID().transformTo(cross)).whereEqual(colID(), getId()).findMany();
    }

    @SuppressWarnings("unchecked")
    default <R1 extends RelatedTo<E1>, M extends Entity, T extends PrimaryKey<E1, F, R1>, E1 extends Entity> T findRelatedOneByCross(QueryTarget target, M cross, T entityRV) {
        return target.select((T) entityRV.getEntity()).join(cross, colID()).join(entityRV, entityRV.colID().transformTo(cross)).whereEqual(colID(), getId()).findOne();
    }

//    @SuppressWarnings({"unchecked", "Duplicates"})
//    default <R1 extends EntityRelation<E1>, M extends Entity, T extends EntityMap & PrimaryKey<E1, F, R1>, E1 extends Entity> List<T> findRelatedManyByCross(QueryTarget target, M cross, Column<T, ?, ?>... columns) {
//        T entityRV = columns[0].getInstance();
//        return target.select(columns).join(cross, colID()).join(entityRV, entityRV.colID().transformTo(cross)).whereEqual(colID(), getId()).findManyAs(columns[0].getEntityClass());
//    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    default <M extends Entity, T extends Entity & PrimaryKey> List<T> findRelatedManyByCross(QueryTarget target, M cross, Column<T, ?, ?>... columns) {
        T entityRV = columns[0].getInstance();
        DataBaseQueryPlate dataBaseQueryPlate = target.select(columns).from(entityRV).join(cross, colID()).join(entityRV, entityRV.colID().transformTo(cross)).whereEqual(colID(), getId());
        return (List<T>) dataBaseQueryPlate.findManyAs((Class<? extends Entity>) columns[0].entityClass());
    }

    @SuppressWarnings({"unchecked", "Duplicates"})
    default <R1 extends RelatedTo<E1>, M extends Entity, T extends Entity & PrimaryKey<E1, F, R1>, E1 extends Entity> T findRelatedOneByCross(QueryTarget target, M cross, Column<T, ?, ?>... columns) {
        T entityRV = columns[0].getInstance();
        return target.select(columns).join(cross, colID()).join(entityRV, entityRV.colID().transformTo(cross)).whereEqual(colID(), getId()).findOneAs(columns[0].entityClass());
    }


    @SuppressWarnings("unchecked")
    default E insert(QueryTarget target) {
        target.insert((E) this).execute();
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    default E insert(QueryTarget target, boolean transaction) {
        target.insert((E) this).execute(transaction);
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    default E insert(QueryTarget target, Propagation propagation) {
        target.insert((E) this).execute(propagation);
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    default boolean delete(QueryTarget target) {
        return new DataBaseQueryDelete<>(target, (E) this).whereEqual(colID(), getId()).execute() == 1;
    }

    @SuppressWarnings("unchecked")
    default boolean delete(OpenedTransactionDataBaseTarget target, boolean transaction) {
        return new DataBaseQueryDelete<>(target, (E) this).whereEqual(colID(), getId()).execute(transaction) == 1;
    }

    @SuppressWarnings("unchecked")
    default boolean delete(OpenedTransactionDataBaseTarget target, Propagation propagation) {
        return new DataBaseQueryDelete<>(target, (E) this).whereEqual(colID(), getId()).execute(propagation) == 1;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
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
        target.update(SetFields.setColumns((E) this, columnsToUpdate)).whereEqual(colID(), getId()).execute();
        return (E) this;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
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
        target.update(SetFields.setColumns((E) this, columnsToUpdate)).whereEqual(colID(), getId()).execute(propagation);
        return (E) this;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
    default E update(QueryTarget target) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey) {
            OptimisticConcurrencyForPrimaryKey occ = (OptimisticConcurrencyForPrimaryKey) this;
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setInfoColumns((RelatedTo) this, target.getColumnsForTable(this).toArray(new TableColumn[0])))
                .whereEqual(colID(), getId())
                .execute();
        return (E) this;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
    default E update(OpenedTransactionDataBaseTarget target, boolean transaction) {
        if (this instanceof OptimisticConcurrencyForPrimaryKey) {
            OptimisticConcurrencyForPrimaryKey occ = (OptimisticConcurrencyForPrimaryKey) this;
            occ.handleOCC(target, this);
        }
        target.update(SetFields.setColumns((E) this, target.getColumnsForTable(this).stream()
                        .map(TableColumn::getColumn)
                        .toArray(Column[]::new)))
                .whereEqual(colID(), getId())
                .execute(transaction);
        return (E) this;
    }


    @SuppressWarnings({"unchecked", "rawtypes", "Duplicates"})
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
                .whereEqual(colID(), getId())
                .execute(transaction);
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    default E findById(QueryTarget target, F id) {
        return target.select((E) this).whereEqual(colID(), id).findOne();
    }

    @SuppressWarnings("unchecked")
    default <F1, R1 extends RelatedTo> E findByColumnValue(QueryTarget target, Column<E, F1, R1> column, F1 value) {
        return target.select((E) this).whereEqual(column, value).findOne();
    }

    @SuppressWarnings("unchecked")
    default String getSqlUpdate(DataBaseTarget target) {
        return target.update(SetFields.setColumns((E) this, (Column<E, ?, ?>) target.getColumnsForTable(this))).whereEqual(colID(), getId()) + ";";
    }

    @SuppressWarnings("unchecked")
    default String getSqlUpdate(DataBaseTarget target, Column<E, ?, ?>... columnsToUpdate) {
        return target.update(SetFields.setColumns((E) this, columnsToUpdate)).whereEqual(colID(), getId()).getSQLQuery(target) + ";";
    }


}

