package com.n26.service;

import com.n26.model.Statistic;
import com.n26.model.Transaction;
import com.n26.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    private final DateUtil dateUtil;


    public StatisticService(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }

    public Statistic generateStatisticLastMinute(@NotNull Collection<Transaction> transactions) {
        Statistic statistic = new Statistic();

        List<Transaction> lessMinuteSorted = transactions.stream()
                .filter(transaction -> !dateUtil.isTransactionOld(transaction))
                .sorted(Comparator.comparing(Transaction::getAmount))
                .collect(Collectors.toList());

        // If not have any transaction, return statistic with all values equals 0
        if (CollectionUtils.isEmpty(lessMinuteSorted)) {
            return statistic;
        }

        int totalCount = lessMinuteSorted.size();
        @NotNull BigDecimal sum = lessMinuteSorted.stream().map(Transaction::getAmount).reduce(BigDecimal::add).get();

        statistic.setSum(sum);
        statistic.setMin(lessMinuteSorted.get(0).getAmount());
        statistic.setMax(CollectionUtils.lastElement(lessMinuteSorted).getAmount());
        statistic.setCount(totalCount);
        statistic.setAvg(sum.divide(BigDecimal.valueOf(totalCount), 2, BigDecimal.ROUND_HALF_UP));

        return statistic;
    }
}
