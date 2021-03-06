package com.n26.repository;

import com.n26.model.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository = new TransactionRepository();
    }

    @Test
    public void saveAndFindById() {
        Transaction transaction = new Transaction();
        UUID id = transaction.getId();
        transactionRepository.save(transaction);

        Assert.assertNotNull("Object must be saved", transactionRepository.findById(id));
    }

    @Test
    public void saveAllAndFindAllById() {
        Transaction transaction = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> transactions = Arrays.asList(transaction, transaction2);
        transactionRepository.saveAll(transactions);

        List<UUID> uuids = transactions.stream().map(Transaction::getId).collect(Collectors.toList());
        Assert.assertEquals("Object must be saved", 2, transactionRepository.findAllById(uuids).size());
    }

    @Test
    public void findAllAndCount() {
        transactionRepository.deleteAll();

        Transaction transaction = new Transaction();
        Transaction transaction2 = new Transaction();
        transactionRepository.saveAll(Arrays.asList(transaction, transaction2));

        Assert.assertEquals("Object must be saved", 2, transactionRepository.findAll().size());
        Assert.assertEquals("Object must be saved", 2, transactionRepository.count());
    }

    @Test
    public void deleteById() {
        Transaction transaction = new Transaction();
        UUID transactionId = transaction.getId();

        Transaction transaction2 = new Transaction();
        UUID transaction2Id = transaction2.getId();
        transactionRepository.saveAll(Arrays.asList(transaction, transaction2));

        transactionRepository.deleteById(transactionId);
        Assert.assertFalse("Object must be deleted", transactionRepository.findById(transactionId).isPresent());
        Assert.assertTrue("Object must be NOT deleted", transactionRepository.findById(transaction2Id).isPresent());
    }

    @Test
    public void delete() {
        Transaction transaction = new Transaction();
        UUID transactionId = transaction.getId();

        Transaction transaction2 = new Transaction();
        UUID transaction2Id = transaction2.getId();
        transactionRepository.saveAll(Arrays.asList(transaction, transaction2));

        transactionRepository.delete(transaction);
        Assert.assertFalse("Object must be deleted", transactionRepository.findById(transactionId).isPresent());
        Assert.assertTrue("Object must be NOT deleted", transactionRepository.findById(transaction2Id).isPresent());
    }

    @Test
    public void deleteAll() {
        Transaction transaction = new Transaction();
        Transaction transaction2 = new Transaction();
        transactionRepository.saveAll(Arrays.asList(transaction, transaction2));
        Assert.assertTrue("Object must be created", transactionRepository.count() >= 2);

        transactionRepository.deleteAll();
        Assert.assertEquals("Object must be deleted", 0, transactionRepository.count());
    }

    @After
    public void tearDown() {
        transactionRepository = null;
    }
}
