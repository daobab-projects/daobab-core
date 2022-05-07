package io.daobab.statement.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdentifierStorageTest {
    String ONE="one";
    String TWO="two";

    @Test
    public void testIdentifiers(){
        IdentifierStorage identifierStorage = new IdentifierStorage();
        String id1 = identifierStorage.getIdentifierFor(ONE);
        String id2 = identifierStorage.getIdentifierFor(TWO);
        Assertions.assertEquals(id1, IdentifierStorage.IDENTIFIER + "1");
        Assertions.assertEquals(id2, IdentifierStorage.IDENTIFIER + "2");

//        identifierStorage.getAllDao().forEach(System.out::println);
    }
}
