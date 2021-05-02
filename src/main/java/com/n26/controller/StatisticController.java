package com.n26.controller;

import com.n26.model.Statistic;
import com.n26.repository.TransactionRepository;
import com.n26.service.StatisticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/statistics")
public class StatisticController {

    private final TransactionRepository transactionRepository;
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService, TransactionRepository transactionRepository) {
        this.statisticService = statisticService;
        this.transactionRepository = transactionRepository;
    }


    @GetMapping()
    public Statistic statisticsLastMinute() {
        return statisticService.generateStatisticLastMinute(transactionRepository.findAll());
    }
}
