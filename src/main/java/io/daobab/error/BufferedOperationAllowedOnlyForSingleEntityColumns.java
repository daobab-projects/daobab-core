package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BufferedOperationAllowedOnlyForSingleEntityColumns extends DaobabException {

    public BufferedOperationAllowedOnlyForSingleEntityColumns() {
        super("Query can not be proceeded in cache when points at more than one Entity.");
    }

}
