package io.daobab.transaction;

import io.daobab.error.DaobabException;
import io.daobab.error.TargetMandatoryException;
import io.daobab.error.TransactionClosedException;
import io.daobab.error.TransactionNotAllowerForPropagationNever;
import io.daobab.target.Target;

import static io.daobab.transaction.TransactionIndicator.*;

public enum Propagation {

    /**
     * Support a current transaction, throw an exception if none exists.
     * Does not start a new Transaction, just checks whether a transaction is active
     */
    MANDATORY,

    /**
     * Execute within a nested transaction if a current transaction exists, behave like PROPAGATION_REQUIRED else.
     * <p>
     * Start a nested transaction if a transaction exists, start a new transaction otherwise.
     */
    NESTED,

    /**
     * Execute non-transactionally, throw an exception if a transaction exists.
     * <p>
     * Does not start a transaction. Fails if a transaction is present.
     */
    NEVER,

    /**
     * Execute non-transactionally, suspend the current transaction if one exists.
     * <p>
     * Does not start a transaction. Suspends any existing transaction.
     */
    NOT_SUPPORTED,

    /**
     * Support a current transaction, create a new one if none exists.
     * <p>
     * If a transaction exists, use that, if not, create a new one. In 95% of cases, this is what you need.
     */
    REQUIRED,

    /**
     * Create a new transaction, suspend the current transaction if one exists.
     * <p>
     * Always creates a new transaction, no matter if an existing transaction is present. If there is, it will be suspended for the duration of this method execution.
     */
    REQUIRED_NEW,

    /**
     * Support a current transaction, execute non-transactionally if none exists.
     * <p>
     * Can use a transaction if one is present, but doesn't need one (and won't start a new one either)
     */
    SUPPORTS;

    public TransactionIndicator mayBeProceeded(Target target) {
        if (target == null) {
            throw new TargetMandatoryException();
        }

        if ((this == MANDATORY || this == SUPPORTS || this == REQUIRED) && target.isTransactionActive()) {
            return GO_AHEAD;
        }


        if (this == REQUIRED_NEW || (this == REQUIRED && !target.isTransactionActive())) {
            return START_NEW_JUST_FOR_IT;
        }


        if (this == NOT_SUPPORTED || ((this == NEVER || this == SUPPORTS) && !target.isTransactionActive())) {
            return EXECUTE_WITHOUT;
        }

        if (this == MANDATORY && !target.isTransactionActive()) {
            throw new TransactionClosedException(target);
        }


        if (this == NEVER && target.isTransactionActive()) {
            throw new TransactionNotAllowerForPropagationNever();
        }

        throw new DaobabException("Unhandled transaction case: " + this + " and transaction opened:" + target.isTransactionActive());

    }

}
