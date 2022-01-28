package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class BufferedOperationAllowedOnlyForSingleEntityColumns extends DaobabException {

    private static final long serialVersionUID = 1L;

    public BufferedOperationAllowedOnlyForSingleEntityColumns() {
        super("Query can not be proceeded in cache when points at more than one Entity.");
    }

}
