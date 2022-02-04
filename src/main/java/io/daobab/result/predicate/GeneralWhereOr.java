package io.daobab.result.predicate;

import io.daobab.model.Column;
import io.daobab.model.EntityRelation;
import io.daobab.statement.where.base.Where;

import java.util.List;

public class GeneralWhereOr<E> extends GeneralWhereAnd<E> {

    public GeneralWhereOr(Where wrapperWhere) {
        super(wrapperWhere);
    }

    public GeneralWhereOr(Where wrapperWhere, List<Integer> skipSteps) {
        super(wrapperWhere, skipSteps);
    }

    @Override
    public boolean test(E entity) {

        for (int i = 0; i < keys.size(); i++) {

            Column column = keys.get(i);

            //if key is null, inner where is in use
            if (column == null) {
                if (predicates.get(i).test(entity)) return true;
                continue;
            }

            Object valueFromEntity = column.getValue((EntityRelation) entity);

            //if at least one record into OR clause is true, entity is matched
            if (predicates.get(i).test(valueFromEntity)) return true;

        }

        return false;
    }


}
