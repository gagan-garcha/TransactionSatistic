package com.n26.util;

import com.n26.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

public class DateUtilTest {
    private DateUtil dateUtil;

    @Before
    public void setUp() {
        dateUtil = new DateUtil();
    }

    @Test
    public void isTransactionOlderMinute() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(Date.from(Instant.now()));
        Assert.assertFalse("Transaction created now", dateUtil.isTransactionOld(transaction));

        transaction.setTimestamp(Date.from(Instant.now().minusSeconds(DateUtil.SECONDS_TRANSACTION_BECOME_OLD + 1)));
        Assert.assertTrue("Transaction is older one minute", dateUtil.isTransactionOld(transaction));
    }

    @Test
    public void isTransactionInFuture() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(Date.from(Instant.now()));
        Assert.assertFalse("Transaction created now", dateUtil.isTransactionInFuture(transaction));

        transaction.setTimestamp(Date.from(Instant.now().plusSeconds(10)));
        Assert.assertTrue("Transaction is from future", dateUtil.isTransactionInFuture(transaction));

        transaction.setTimestamp(Date.from(Instant.now().minusSeconds(1)));
        Assert.assertFalse("Transaction from past", dateUtil.isTransactionInFuture(transaction));
    }
}
