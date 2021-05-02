package com.n26.config;

import com.n26.model.Transaction;
import com.n26.repository.TransactionRepository;
import com.n26.util.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class SchedulerTest {

    private  Scheduler scheduler;
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository = new TransactionRepository();
        scheduler = new Scheduler(transactionRepository, new DateUtil());
    }

    @Test
    public void deleteOldData() {
        Transaction transaction = new Transaction();
        UUID id = transaction.getId();
        transaction.setAmount(BigDecimal.ONE);
        transaction.setTimestamp(Date.from(Instant.now().minusSeconds(DateUtil.SECONDS_TRANSACTION_BECOME_OLD + 1)));
        transactionRepository.save(transaction);

        Assert.assertTrue(transactionRepository.findById(id).isPresent());

        scheduler.deleteOldData();

        Assert.assertFalse("Old transaction must be delete", transactionRepository.findById(id).isPresent());
    }
}
