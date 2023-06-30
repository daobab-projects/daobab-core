package io.daobab.target.protection;

import io.daobab.error.AccessDenied;
import io.daobab.target.database.MockDataBase;
import io.daobab.test.dao.SakilaTables;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAccessProtector implements SakilaTables {


    @Test
    public void testReadOnly() {
        AccessProtector ac = new BasicAccessProtector(new MockDataBase());
        ac.setDefaultStrategy(DefaultAccessStrategy.READ_ONLY);
        Assertions.assertThrows(AccessDenied.class, () -> ac.validateEntityAllowedFor(tabActor, OperationType.DELETE));
        Assertions.assertThrows(AccessDenied.class, () -> ac.validateEntityAllowedFor(tabActor, OperationType.INSERT));
        Assertions.assertThrows(AccessDenied.class, () -> ac.validateEntityAllowedFor(tabActor, OperationType.UPDATE));
        Assertions.assertDoesNotThrow(() -> ac.validateEntityAllowedFor(tabActor, OperationType.READ));
    }

    @Test
    public void testReadOnlySingleEntity() {
        AccessProtector ac = new BasicAccessProtector(new MockDataBase());
        ac.setDefaultStrategy(DefaultAccessStrategy.DENY).setEntityAccess(tabActor, Access.READ);
        Assertions.assertThrows(AccessDenied.class, () -> ac.validateEntityAllowedFor(tabActor, OperationType.DELETE));
        Assertions.assertThrows(AccessDenied.class, () -> ac.validateEntityAllowedFor(tabActor, OperationType.INSERT));
        Assertions.assertThrows(AccessDenied.class, () -> ac.validateEntityAllowedFor(tabActor, OperationType.UPDATE));
        Assertions.assertDoesNotThrow(() -> ac.validateEntityAllowedFor(tabActor, OperationType.READ));
    }
}
