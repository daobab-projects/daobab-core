package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class AtLeastOneColumnToUpdateIsRequired extends DaobabException {

    private static final long serialVersionUID = 1L;


    public AtLeastOneColumnToUpdateIsRequired() {
        super("At least one column to update is required");
    }


}
