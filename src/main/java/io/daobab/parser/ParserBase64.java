package io.daobab.parser;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings("unused")
public interface ParserBase64 {

    static byte[] toNonByteFromBase64Bytes(byte[] from) {
        if (from == null) return null;
        String str = Decoder.printBase64Binary(from);

        return ParserString.toByteArrayFromUTF8(str);
    }

}
