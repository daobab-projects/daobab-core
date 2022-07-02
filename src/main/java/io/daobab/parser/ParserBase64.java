package io.daobab.parser;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings("unused")
public class ParserBase64 {

    public ParserBase64() {
    }

    public static byte[] toNonByteFromBase64Bytes(byte[] from) {
        if (from == null) return new byte[0];
        String str = Decoder.printBase64Binary(from);

        return ParserString.toByteArrayFromUTF8(str);
    }

}
