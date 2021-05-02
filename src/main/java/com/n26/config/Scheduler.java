package com.n26.config;

import com.n26.model.Transaction;
import com.n26.repository.TransactionRepository;
import com.n26.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@Slf4j
public class Scheduler {
    private static final long FIVE_MINUTES_IN_MILLIS = 5 * 60_000;

    private final TransactionRepository transactionRepository;
    private final DateUtil dateUtil;

    public Scheduler(TransactionRepository transactionRepository, DateUtil dateUtil) {
        this.transactionRepository = transactionRepository;
        this.dateUtil = dateUtil;
    }

    @Scheduled(initialDelay = FIVE_MINUTES_IN_MILLIS, fixedDelay = FIVE_MINUTES_IN_MILLIS)
    public void deleteOldData() {
        Collection<Transaction> transactions = transactionRepository.findAll();
        if (CollectionUtils.isEmpty(transactions)) {
            return;
        }

        List<Transaction> oldTransactions = transactions.stream().filter(dateUtil::isTransactionOld).collect(Collectors.toList());

        transactionRepository.deleteAll(oldTransactions);
        log.info("Flush old transactions", oldTransactions.size());
    }
}
