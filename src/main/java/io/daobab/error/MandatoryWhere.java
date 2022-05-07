package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class MandatoryWhere extends DaobabException {

    private static final long serialVersionUID = 1L;

    public MandatoryWhere() {
        super("Where clause is mandatory for this operation.");
    }

}
