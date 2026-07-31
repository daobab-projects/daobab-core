package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BufferedOperationAllowedOnlyForSingleEntityColumns extends DaobabException {

    public BufferedOperationAllowedOnlyForSingleEntityColumns() {
        super("Query cannot be processed in cache when it points at more than one Entity.");
    }

}
