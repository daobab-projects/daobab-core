package io.daobab.target.protection;

import io.daobab.test.dao.table.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class TestImmutability {

    @Test
    void testMe() {
        Payment payment = new Payment().setPaymentId(1).setRentalId(new BigDecimal(23));
        Payment payment2 = payment.setRentalId(new BigDecimal(11));

        Assertions.assertNotEquals(payment.getRentalId(), payment2.getRentalId());
    }
}
