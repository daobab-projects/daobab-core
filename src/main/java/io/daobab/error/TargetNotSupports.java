package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class TargetNotSupports extends DaobabException {

    private static final long serialVersionUID = 1L;

    public TargetNotSupports() {
        super("Target does not support such operation");
    }

}
