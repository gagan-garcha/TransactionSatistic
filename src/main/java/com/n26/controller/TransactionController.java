package com.n26.controller;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.exception.TransactionTimestampException;
import com.n26.model.Transaction;
import com.n26.repository.TransactionRepository;
import com.n26.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/transactions")
@Slf4j
public class TransactionController {

    private final TransactionRepository transactionRepository;

    private final DateUtil dateUtil;

    public TransactionController(TransactionRepository transactionRepository, DateUtil dateUtil) {
        this.transactionRepository = transactionRepository;
        this.dateUtil = dateUtil;
    }

    @PostMapping(consumes = "application/json")
    ResponseEntity<?> createTransaction(@Valid @NotNull @RequestBody Transaction transaction) throws TransactionTimestampException {

        // If the transaction date is in the future then return 422
        if (dateUtil.isTransactionInFuture(transaction)) {
            throw new TransactionTimestampException(HttpStatus.UNPROCESSABLE_ENTITY, "Transaction " + transaction + " is in the future");
        }

        // If the transaction is older than {@link DateUtil#SECONDS_TRANSACTION_BECOME_OLD} seconds then return 204
        if (dateUtil.isTransactionOld(transaction)) {
            throw new TransactionTimestampException(HttpStatus.NO_CONTENT, "Transaction " + transaction + " is older than "
                    + DateUtil.SECONDS_TRANSACTION_BECOME_OLD + " seconds");
        }
        transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping()
    ResponseEntity<?> deleteTransactions() {
        transactionRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(TransactionTimestampException.class)
    ResponseEntity<?> transactionTimestampException(TransactionTimestampException exception) {
        log.error("Invalid Date Format ", exception);
        return ResponseEntity.status(exception.getHttpStatus()).build();
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public void messageNotReadableException(InvalidFormatException exception) {
        log.error("Invalid JSON", exception);
    }
}
