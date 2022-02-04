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
                if (rights.contains(Access.NO_READ)) {
                    return false;
                }
                if (rights.contains(Access.FULL) || rights.contains(Access.READ)) {
                    return true;
                }
                break;
            }
            case INSERT: {
                if (rights.contains(Access.NO_INSERT)) {
                    return false;
                }
                if (rights.contains(Access.FULL) || rights.contains(Access.WRITE)) {
                    return true;
                }
                break;
            }
            case UPDATE: {
                if (rights.contains(Access.NO_UPDATE)) {
                    return false;
                }
                if (rights.contains(Access.FULL) || rights.contains(Access.WRITE)) {
                    return true;
                }
                break;
            }
            case DELETE: {
                if (rights.contains(Access.NO_DELETE)) {
                    return false;
                }
                if (rights.contains(Access.FULL) || rights.contains(Access.WRITE)) {
                    return true;
                }
                break;
            }
        }

        return !DefaultAccessStrategy.DENY.equals(defaultAccessStrategy);
    }
}
