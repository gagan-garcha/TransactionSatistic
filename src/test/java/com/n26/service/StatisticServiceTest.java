package com.n26.service;

import com.n26.model.Statistic;
import com.n26.model.Transaction;
import com.n26.util.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class StatisticServiceTest {

    @Mock
    private DateUtil dateUtil;
    @InjectMocks
    private StatisticService statisticService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createStatisticLastMinute() {
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.TEN);
        transaction.setTimestamp(Date.from(Instant.now()));

        Transaction transaction2 = new Transaction();
        transaction2.setTimestamp(Date.from(Instant.now()));
        transaction2.setAmount(BigDecimal.ONE);

        Transaction transactionRoundDown = new Transaction();
        transactionRoundDown.setTimestamp(Date.from(Instant.now()));
        transactionRoundDown.setAmount(BigDecimal.valueOf(2.554));

        Transaction transactionRoundUp = new Transaction();
        transactionRoundUp.setTimestamp(Date.from(Instant.now()));
        transactionRoundUp.setAmount(BigDecimal.valueOf(2.556));

        List<Transaction> transactions = Arrays.asList(transaction, transaction2,
                transactionRoundDown, transactionRoundUp);

        Statistic statisticLastMinute = statisticService.generateStatisticLastMinute(transactions);
        Assert.assertEquals(4, statisticLastMinute.getCount());
        Assert.assertEquals(BigDecimal.valueOf(1), statisticLastMinute.getMin());
        Assert.assertEquals(BigDecimal.valueOf(10), statisticLastMinute.getMax());
        Assert.assertEquals(BigDecimal.valueOf(16.11).setScale(2), statisticLastMinute.getSum().setScale(2));
        Assert.assertEquals(BigDecimal.valueOf(4.03).setScale(2), statisticLastMinute.getAvg().setScale(2));
    }
}
