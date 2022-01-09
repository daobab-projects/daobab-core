package io.daobab.target.database;

import io.daobab.error.DaobabException;
import io.daobab.target.QueryReceiver;
import io.daobab.target.Target;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public interface TransactionalTarget extends Target, QueryReceiver {

    TransactionalTarget getSourceTarget();

    //TODO: wywal database na rzecz czysciocha
    default OpenTransactionDataBaseTarget beginTransaction() {
        return new OpenTransactionDataBaseTarget((DataBaseTarget) this);
    }

    default <R> void wrapTransactionAsynch(Function<OpenTransactionDataBaseTarget, R> consumer) {
        CompletableFuture.runAsync(() -> wrapTransaction(consumer));
    }

    default <R> void wrapTransactionAsynch(Function<OpenTransactionDataBaseTarget, R> consumer, Executor executor) {
        CompletableFuture.runAsync(() -> wrapTransaction(consumer), executor);
    }

    default <R> R wrapTransaction(Function<OpenTransactionDataBaseTarget, R> consumer) {

        OpenTransactionDataBaseTarget otx = beginTransaction();
        try {
            R rv = consumer.apply(otx);
            otx.commit();
            return rv;
        } catch (Exception e) {
            otx.rollback();
            throw new DaobabException("Transaction related exception", e);
        }

    }
}
