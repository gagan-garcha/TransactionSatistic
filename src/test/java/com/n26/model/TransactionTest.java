package com.n26.model;

import org.junit.Assert;
import org.junit.Test;


public class TransactionTest {

    @Test
    public void createDefault() {
        Transaction transaction = new Transaction();
        Assert.assertNotNull(transaction.getId());
    }
}
