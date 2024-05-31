package io.daobab.converter.json.type;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class TestJsonLocalDateTimeConverter {

    @Test
    @Disabled
    void testParser() {
        JsonLocalDateTimeConverter localDateTimeConverter = new JsonLocalDateTimeConverter();
        StringBuilder stringBuilder = new StringBuilder();
        localDateTimeConverter.toJson(stringBuilder, LocalDateTime.now());
        System.out.println(stringBuilder);

        String aaa = DateTimeFormatter.ofPattern(JsonLocalDateTimeConverter.dateTimeFormat).format(LocalDateTime.now());
        System.out.println(aaa);

        LocalDateTime ldt = localDateTimeConverter.fromJson(stringBuilder.toString());


        System.out.println(ldt);
    }
}
