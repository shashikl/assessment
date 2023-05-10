package com.example.assessment.repositories;

import com.example.assessment.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.*;

public interface TransactionDao extends JpaRepository<Transaction, Integer> {

    @Query("Select t from Transaction t where t.customer.id=:customer_id")
    public List<Transaction> findTransactionsByCustomerId(@Param("customer_id") Integer customerId);

    @Query("Select t from Transaction t where t.customer.id=:customer_id and t.transactionDate >=:start_date and t.transactionDate <=:end_date order by t.transactionDate desc")
    public List<Transaction> findTransactionsByCustomerIdAndDateTimeRange(
            @Param("customer_id") Integer customerId,
            @Param("start_date") LocalDateTime startDate,
            @Param("end_date") LocalDateTime endDate
    );
}
