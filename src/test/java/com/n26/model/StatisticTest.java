package com.n26.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;



public class StatisticTest {

    @Test
    public void createDefault() {
        Statistic statistic = new Statistic();
        Assert.assertEquals(BigDecimal.ZERO, statistic.getAvg());
        Assert.assertEquals(BigDecimal.ZERO, statistic.getMax());
        Assert.assertEquals(BigDecimal.ZERO, statistic.getMin());
        Assert.assertEquals(BigDecimal.ZERO, statistic.getSum());
        Assert.assertEquals(0, statistic.getCount());
    }
}
