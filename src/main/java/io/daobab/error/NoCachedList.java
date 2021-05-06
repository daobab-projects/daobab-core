package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class NoCachedList extends DaobabException {

    private static final long serialVersionUID = 1L;


    public NoCachedList() {
        super("CachedList can not be null");
    }


}
