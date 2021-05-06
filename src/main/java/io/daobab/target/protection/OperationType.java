package io.daobab.target.protection;

import io.daobab.error.DaobabException;

import java.util.Set;

public enum OperationType {
    READ,
    INSERT,
    UPDATE,
    DELETE;

    public static boolean isAllowed(OperationType operation, DefaultAccessStrategy defaultAccessStrategy, Set<Access> rights) {
        if (operation == null || defaultAccessStrategy == null) {
            throw new DaobabException("Operation or DefaultAccessStrategy wasn't provided");
        }
        if (rights == null || rights.isEmpty()) {
            return DefaultAccessStrategy.ALLOW.equals(defaultAccessStrategy) || (DefaultAccessStrategy.READ_ONLY.equals(defaultAccessStrategy) && operation == READ);
        }
        if (rights.contains(Access.DENIED)) {
            return false;
        }

        switch (operation) {
            case READ: {
                return (rights.contains(Access.FULL) || rights.contains(Access.READ)) && !rights.contains(Access.NO_READ);
            }
            case INSERT: {
                return (rights.contains(Access.FULL) || rights.contains(Access.WRITE)) && !rights.contains(Access.NO_INSERT);
            }
            case UPDATE: {
                return (rights.contains(Access.FULL) || rights.contains(Access.WRITE)) && !rights.contains(Access.NO_UPDATE);
            }
            case DELETE: {
                return (rights.contains(Access.FULL) || rights.contains(Access.WRITE)) && !rights.contains(Access.NO_DELETE);
            }
        }
        return !DefaultAccessStrategy.DENY.equals(defaultAccessStrategy);
    }
}
