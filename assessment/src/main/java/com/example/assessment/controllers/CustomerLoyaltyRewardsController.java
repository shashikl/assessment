package com.example.assessment.controllers;

import com.example.assessment.CustomerMonthlyRewards;
import com.example.assessment.entities.Customer;
import com.example.assessment.entities.Transaction;
import com.example.assessment.requestbody.CustomerRequestBody;
import com.example.assessment.requestbody.TransactionRequestBody;
import com.example.assessment.services.LoyaltyService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CustomerLoyaltyRewardsController {

    @Autowired
    private LoyaltyService loyaltyService;

    @GetMapping(value = "/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionsById(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(loyaltyService.getTransactionById(id));
    }

    @GetMapping(value = "/customer/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomerId(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(loyaltyService.getTransactionsByCustomerId(id));
    }

    @PostMapping(value = "/customer")
    public ResponseEntity<Customer> addNewCustomer(@RequestBody CustomerRequestBody customerRequestBody) {
        return ResponseEntity.ok(loyaltyService.createCustomer(customerRequestBody.name, customerRequestBody.email));
    }

    @PostMapping(value = "/customer/{id}/transaction/{amount}")
    public ResponseEntity<Transaction> postTransaction(@NonNull @PathVariable Integer id, @NonNull @PathVariable Double amount) {
        return ResponseEntity.ok(loyaltyService.saveTransaction(id, amount));
    }

    @PostMapping(value = "/transactions/save")
    public ResponseEntity<List<Transaction>> saveTransactions(@NonNull @RequestBody List<TransactionRequestBody> transactionsList) {
        return ResponseEntity.ok(loyaltyService.saveTransactions(transactionsList));
    }

    @GetMapping(value = "/customer/{id}/points")
    public ResponseEntity<CustomerMonthlyRewards> getCustomerMonthlyPoints(@PathVariable @NonNull Integer id) {
        return ResponseEntity.ok(loyaltyService.getMonthlyRewardsByCustomerId(id));
    }
}
