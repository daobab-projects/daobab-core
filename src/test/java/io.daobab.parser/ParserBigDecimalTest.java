package io.daobab.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ParserBigDecimalTest {

    @Test
    void toBigInteger(){
        BigDecimal bigDecimal=new BigDecimal("100");
        BigInteger b100=new BigInteger("100");
        BigInteger bigInteger=ParserBigDecimal.toBigInteger(bigDecimal);
        Assertions.assertEquals(b100,bigInteger);
    }

    @Test
    void toDouble(){
        BigDecimal bigDecimal=new BigDecimal("100");
        Double b100=Double.parseDouble("100");
        Double ddouble=ParserBigDecimal.toDouble(bigDecimal);
        Assertions.assertEquals(b100,ddouble);
    }

}
