package io.daobab.result.predicate;

import io.daobab.model.Column;
import io.daobab.model.Plate;
import io.daobab.statement.where.base.Where;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class GeneralWhereOrPlate<P extends Plate> extends GeneralWhereAndPlate<P> {

    public GeneralWhereOrPlate(Where wrapperWhere) {
        super(wrapperWhere);
    }

    @Override
    public boolean test(P projection) {

        for (int i = 0; i < keys.size(); i++) {

            Column column = keys.get(i);

            if (column == null) {
                if (predicates.get(i).test(projection)) return true;
                continue;
            }

            Object valueFromEntity = projection.getValue(column);

            //if at least one record into OR clause is true, entity is matched
            if (predicates.get(i).test(valueFromEntity)) return true;

        }

        return false;
    }


}
