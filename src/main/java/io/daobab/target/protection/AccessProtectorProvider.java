package io.daobab.target.protection;

import io.daobab.error.DaobabException;

public interface AccessProtectorProvider {

    default AccessProtector getAccessProtector() {
        throw new DaobabException("Please initialise AccessProtector into " + this.getClass().getSimpleName() + " if you want to use it there.");
    }
}
