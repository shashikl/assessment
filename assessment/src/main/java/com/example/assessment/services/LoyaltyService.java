package com.example.assessment.services;

import com.example.assessment.CustomerMonthlyRewards;
import com.example.assessment.MonthlyRewards;
import com.example.assessment.entities.Customer;
import com.example.assessment.entities.Transaction;
import com.example.assessment.repositories.CustomerDao;
import com.example.assessment.repositories.TransactionDao;
import com.example.assessment.requestbody.TransactionRequestBody;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LoyaltyService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private TransactionDao transactionDao;

    public Customer getCustomerById(@NonNull Integer id) {
        return customerDao.findById(id).orElse(null);
    }

    public Transaction getTransactionById(@NonNull Integer id) {
        return transactionDao.findById(id).orElse(null);
    }

    public List<Transaction> getTransactionsByCustomerId(@NonNull Integer customerId) {
        return transactionDao.findTransactionsByCustomerId(customerId);
    }

    public Customer createCustomer(@NonNull String name, @NonNull String email) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        return customerDao.save(customer);
    }

    public Transaction saveTransaction(@NonNull Integer customerId, @NonNull Double amount) {
        LocalDateTime transactionDateTime = LocalDateTime.now();
        Customer customer = getCustomerById(customerId);
        if (customer != null) {
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setCustomer(customer);
            transaction.setTransactionDate(transactionDateTime);
            int points = calculateRewards(amount);
            transaction.setPoints(points);
            return transactionDao.save(transaction);
        }
        return null;
    }

    public List<Transaction> saveTransactions(List<TransactionRequestBody> transactionRequestBodyList) {
        List<Integer> customerIds = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        Map<Integer, Customer> customerMap = new HashMap<>();
        for (TransactionRequestBody requestBody: transactionRequestBodyList) {
            customerIds.add(requestBody.customerId);
        }

        List<Customer> customers = customerDao.findAllById(customerIds);
        for (Customer customer: customers) {
            customerMap.put(customer.getId(), customer);
        }

        for (TransactionRequestBody requestBody: transactionRequestBodyList) {
            LocalDateTime transactionDateTime = getLocalDateTime(requestBody.transactionDate);
            Customer customer = customerMap.get(requestBody.customerId);
            Transaction transaction = new Transaction();
            transaction.setTransactionDate(transactionDateTime);
            transaction.setAmount(requestBody.amount);
            transaction.setCustomer(customer);
            transactions.add(transaction);

            // calculate rewards for the transaction amount
            int points = calculateRewards(requestBody.amount);
            transaction.setPoints(points);
        }

        return transactionDao.saveAll(transactions);
    }

    public CustomerMonthlyRewards getMonthlyRewardsByCustomerId(@NonNull Integer customerId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime beginDateTime = currentDateTime.minusMonths(3);
        List<Transaction> transactions = transactionDao.findTransactionsByCustomerIdAndDateTimeRange(customerId, beginDateTime, currentDateTime);
        Map<Month, Integer> pointsByMonthMap = new HashMap<>();

        for (Transaction transaction: transactions) {
            Month month = transaction.getTransactionDate().getMonth();
            Integer points = transaction.getPoints();
            pointsByMonthMap.put(month, pointsByMonthMap.getOrDefault(month, 0) + points);
        }

        CustomerMonthlyRewards customerMonthlyRewards = new CustomerMonthlyRewards();
        customerMonthlyRewards.setCustomer(getCustomerById(customerId));
        List<MonthlyRewards> monthlyRewardsList = new ArrayList<>();
        for (Map.Entry<Month, Integer> entry: pointsByMonthMap.entrySet()) {
            MonthlyRewards monthlyRewards = new MonthlyRewards();
            monthlyRewards.setMonth(entry.getKey());
            monthlyRewards.setRewards(entry.getValue());
            monthlyRewardsList.add(monthlyRewards);
        }
        monthlyRewardsList.sort(new MonthlyRewards());
        customerMonthlyRewards.setRewardsList(monthlyRewardsList);
        return customerMonthlyRewards;
    }

    private int calculateRewards(double amount) {
        int intVal = (int) amount;
        int twoPoints = (intVal > 100) ? ((intVal % 100) * 2) : 0;
        int singlePoints = 0;
        if (intVal >= 100) {
            singlePoints += 50;
        } else if (intVal > 50) {
            singlePoints += intVal - 50;
        }
        return twoPoints + singlePoints;
    }

    private LocalDateTime getLocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}
