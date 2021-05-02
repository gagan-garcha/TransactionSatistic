package com.n26.util;

import com.n26.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DateUtil {
    public static final int SECONDS_TRANSACTION_BECOME_OLD = 60;


    public boolean isTransactionOld(Transaction transaction) {
        return Instant.now().minusSeconds(SECONDS_TRANSACTION_BECOME_OLD).isAfter(transaction.getTimestamp().toInstant());
    }


    public boolean isTransactionInFuture(Transaction transaction) {
        return Instant.now().isBefore(transaction.getTimestamp().toInstant());
    }
}
