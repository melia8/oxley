package org.example.oxley.repository;

import org.example.oxley.domain.Transaction;
import org.example.oxley.domain.TransactionPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, TransactionPk> {

    @Query("select t from Transaction t where t.manufacturer = ?1 and " +
            "t.retailerId = ?2 and t.productCode in ?3 and " +
            "t.transactionDate > ?4 and t.transactionDate < ?5")
    List<Transaction> searchTransactions(String manufacturer, String retailerId,
                                         List<String> productCodes, LocalDate startDate, LocalDate endDate);
}
