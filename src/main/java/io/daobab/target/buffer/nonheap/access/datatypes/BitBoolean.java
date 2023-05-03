package io.daobab.target.buffer.nonheap.access.datatypes;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BitBoolean {

    public static final int TRUE=1;
    public static final int FALSE=0;
    public static final int NULL=2;

    private final byte flag;

    public BitBoolean(Boolean bo){
        if (bo==null){
            flag=(byte)2;
        }else{
            flag=(byte)(bo ?1:0);
        }
    }
}
