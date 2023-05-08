package io.daobab.result.predicate;

import io.daobab.target.buffer.nonheap.NonHeapBuffer;

import java.util.function.Predicate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface WherePredicate<O> extends Predicate<O> {

    @SuppressWarnings({"unchecked", "rawtypes"})
    default boolean bitTest(NonHeapBuffer bufferEntityPointer, int entityPosition, int columnPositionIntoEntity, int colPosition) {
        return test((O) bufferEntityPointer.getValue(entityPosition, columnPositionIntoEntity, colPosition));
    }
}
