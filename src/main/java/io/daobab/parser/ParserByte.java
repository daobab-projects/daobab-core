package io.daobab.parser;

import io.daobab.error.ParserException;

import java.io.UnsupportedEncodingException;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public class ParserByte {

    public ParserByte() {
    }

    /**
     * Encrypt to base64
     */
    public static String toBase64bytes(byte[] from) {
        if (from == null) return null;
        return Decoder.printBase64Binary(from);
    }


    /**
     * Decrypt from base64
     */
    public static byte[] toBytesFromBase64(String from) {
        if (from == null) return new byte[]{};
        return Decoder.parseBase64Binary(from);
    }


    public static String toString(byte[] from) {
        return toString(from, "UTF-8");
    }


    public static String toString(byte[] from, String charset) {
        if (from == null) return null;
        try {
            return new String(from, charset);
        } catch (UnsupportedEncodingException e) {
            throw new ParserException("conversion from byte array to String with charset: '" + charset + "' has failed", e);
        }
    }


}
