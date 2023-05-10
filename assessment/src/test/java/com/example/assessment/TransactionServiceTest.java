package com.example.assessment;

import com.example.assessment.entities.Customer;
import com.example.assessment.entities.Transaction;
import com.example.assessment.repositories.CustomerDao;
import com.example.assessment.repositories.TransactionDao;
import com.example.assessment.requestbody.TransactionRequestBody;
import com.example.assessment.services.LoyaltyService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private TransactionDao transactionDao;

    @InjectMocks
    private LoyaltyService transactionService;

    @Test
    public void testSaveTransactions() {
        MockitoAnnotations.openMocks(this);

        // create test data
        List<TransactionRequestBody> requestBodyList = new ArrayList<>();
        TransactionRequestBody t1 = new TransactionRequestBody();
        t1.customerId = 1;
        t1.amount = 120.00;
        t1.transactionDate = "2023-04-01 10:00:00";

        TransactionRequestBody t2 = new TransactionRequestBody();
        t2.customerId = 2;
        t2.amount = 70.00;
        t2.transactionDate = "2023-04-02 12:00:00";
        requestBodyList.add(t1);
        requestBodyList.add(t2);

        Customer customer1 = new Customer();
        customer1.setId(1);
        customer1.setName("John Doe");

        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setName("Jane Smith");

        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);

        // mock customer DAO response
        List<Integer> customerIds = new ArrayList<>();
        customerIds.add(1);
        customerIds.add(2);
        when(customerDao.findAllById(customerIds)).thenReturn(customers);

        // mock transaction DAO response
        List<Transaction> savedTransactions = new ArrayList<>();
        Transaction savedTransaction1 = new Transaction();
        savedTransaction1.setId(1);
        savedTransaction1.setTransactionDate(LocalDateTime.parse("2023-04-01T10:00:00"));
        savedTransaction1.setAmount(120.00);
        savedTransaction1.setCustomer(customer1);
        savedTransaction1.setPoints(90);

        Transaction savedTransaction2 = new Transaction();
        savedTransaction2.setId(2);
        savedTransaction2.setTransactionDate(LocalDateTime.parse("2023-04-02T12:00:00"));
        savedTransaction2.setAmount(70.00);
        savedTransaction2.setCustomer(customer2);
        savedTransaction2.setPoints(20);

        savedTransactions.add(savedTransaction1);
        savedTransactions.add(savedTransaction2);

        when(transactionDao.saveAll(Mockito.anyList())).thenReturn(savedTransactions);

        // invoke the service method
        List<Transaction> result = transactionService.saveTransactions(requestBodyList);

        // assert the result
        assertEquals(savedTransactions, result);
    }


    @Test
    public void testGetMonthlyRewardsByCustomerId() {
        MockitoAnnotations.openMocks(this);

        // Mock customer and transaction data
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("John Doe");
        customer.setEmail("johndoe@example.com");

        Transaction transaction1 = new Transaction();
        transaction1.setId(1);
        transaction1.setTransactionDate(LocalDateTime.of(2023, 5, 1, 10, 0));
        transaction1.setAmount(120.0);
        transaction1.setPoints(4);
        transaction1.setCustomer(customer);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2);
        transaction2.setTransactionDate(LocalDateTime.of(2023, 4, 1, 11, 0));
        transaction2.setAmount(50.0);
        transaction2.setPoints(0);
        transaction2.setCustomer(customer);

        // Mock DAO methods
        when(transactionDao.findTransactionsByCustomerIdAndDateTimeRange(eq(1), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(transaction1, transaction2));
        when(customerDao.findById(eq(1))).thenReturn(Optional.of(customer));

        // Call the method being tested
        CustomerMonthlyRewards rewards = transactionService.getMonthlyRewardsByCustomerId(1);

        // Verify the results
        assertEquals(customer, rewards.getCustomer());

        List<MonthlyRewards> expectedRewardsList = new ArrayList<>();
        MonthlyRewards april = new MonthlyRewards();
        april.setRewards(0);
        april.setMonth(Month.APRIL);
        MonthlyRewards may = new MonthlyRewards();
        may.setRewards(4);
        may.setMonth(Month.MAY);
        expectedRewardsList.add(april);
        expectedRewardsList.add(may);
        assertEquals(expectedRewardsList.size(), rewards.getRewardsList().size());
    }

}

