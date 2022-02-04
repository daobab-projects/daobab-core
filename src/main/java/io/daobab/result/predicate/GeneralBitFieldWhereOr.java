package io.daobab.result.predicate;

import io.daobab.statement.where.base.Where;
import io.daobab.target.buffer.bytebyffer.BaseByteBuffer;

import java.util.List;

public class GeneralBitFieldWhereOr<E> extends GeneralBitFieldWhereAnd<E> {

    public GeneralBitFieldWhereOr(BaseByteBuffer<E> bufferEntityPointer, Where wrapperWhere, List<Integer> skipSteps) {
        super(bufferEntityPointer, wrapperWhere, skipSteps);
    }

    @Override
    public boolean test(Integer entityPointer) {

        for (int i = 0; i < columnsBufferPositionInWhere.size(); i++) {

            if (skipSteps.contains(i)) {
                continue;
            }

            //if at least one record into OR clause is true, entity is matched
            if (predicates.get(i).bitTest(bufferEntityPointer, entityPointer, columnsEntityPositionInWhere.get(i), columnsBufferPositionInWhere.get(i)))
                return true;
        }

        return false;
    }


    @Override
    public boolean bitTest(BaseByteBuffer bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {

        for (int i = 0; i < predicates.size(); i++) {

            if (skipSteps.contains(i)) {
                continue;
            }
//            if (columnPositionIntoEntity<0){
//                System.out.println("nizej or");
//                if (predicates.get(i).test(entityPosition)){
//                    return true;
//                }
            //if at least one record into OR clause is true, entity is matched
//            }else
            if (predicates.get(i).bitTest(bufferEntityPointer, entityPosition, columnsEntityPositionInWhere.get(i), columnsBufferPositionInWhere.get(i))) {
                return true;
            }
        }

        return false;
    }

}
