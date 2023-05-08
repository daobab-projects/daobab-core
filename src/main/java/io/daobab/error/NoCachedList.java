package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NoCachedList extends DaobabException {

    private static final long serialVersionUID = 1L;

    public NoCachedList() {
        super("CachedList can not be null");
    }

}
