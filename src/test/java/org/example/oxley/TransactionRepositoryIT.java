package org.example.oxley;

import org.example.oxley.domain.Transaction;
import org.example.oxley.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class TransactionRepositoryIT {

    static final String[] TRANSACTION1 =
            "Coca Cola, Tesco, A1235, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, 123.1111, 1000.50"
                    .split(",");
    static final String[] TRANSACTION2 =
            "Coca Cola, Tesco, A1235, 3c7cb1e3-c00d-490b-ac59-946a8084d451, 2025-02-03, 1203.2222, 3010.60"
                    .split(",");
    static final String[] TRANSACTION3 =
            "Coca Cola, Tesco, A1236, 1a9fd6cc-44c7-47b3-bb9b-e5c720b880b9, 2024-01-01, 123.5555, 1000.50"
                    .split(",");
    static final String[] TRANSACTION4 =
            "Coca Cola, Tesco, A1237, 7ba08fe9-60e6-4790-8ea3-434d78fcbf79, 2025-02-03, 1203.1010, 5010.20"
                    .split(",");
    static final String[] TRANSACTION5 =
            "Coca Cola, Tesco, A1237, cc36a454-c860-436c-bedc-0f385270f00f, 2025-02-03, 1203.2020, 2010.50"
                    .split(",");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void getAllTransactions_returnsCorrectTransactions() {
        Transaction transaction1 = transactionRepository.save(createTransaction(TRANSACTION1));
        Transaction transaction2 = transactionRepository.save(createTransaction(TRANSACTION2));
        Transaction transaction3 = transactionRepository.save(createTransaction(TRANSACTION3));
        Transaction transaction4 = transactionRepository.save(createTransaction(TRANSACTION4));
        Transaction transaction5 = transactionRepository.save(createTransaction(TRANSACTION5));

        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions).containsExactlyInAnyOrder(
                transaction1,
                transaction2,
                transaction3,
                transaction4,
                transaction5
        );

    }

    @Test
    void transactionSearch_returnsCorrectTransactions() {
        Transaction transaction1 = transactionRepository.save(createTransaction(TRANSACTION1));
        Transaction transaction2 = transactionRepository.save(createTransaction(TRANSACTION2));
        Transaction transaction3 = transactionRepository.save(createTransaction(TRANSACTION3));
        Transaction transaction4 = transactionRepository.save(createTransaction(TRANSACTION4));
        Transaction transaction5 = transactionRepository.save(createTransaction(TRANSACTION5));

        List<Transaction> transactions = transactionRepository.searchTransactions(
                "Coca Cola",
                "Tesco",
                List.of("A1235", "A1236"),
                LocalDate.of(2010, 1, 1),
                LocalDate.of(2025, 3, 10));

        assertThat(transactions).containsExactlyInAnyOrder(
                transaction1,
                transaction2,
                transaction3
        );
    }

    Transaction createTransaction(String[] fields) {
        return new Transaction(
                fields[0].trim(),
                fields[1].trim(),
                fields[2].trim(),
                UUID.fromString(fields[3].trim()),
                LocalDate.parse(fields[4].trim()),
                Double.parseDouble(fields[5].trim()),
                BigDecimal.valueOf(Double.parseDouble(fields[6].trim()))
        );
    }

    @Test
    void deleteAllTransactions_returnsSuccessfully() {
        Transaction transaction1 = transactionRepository.save(createTransaction(TRANSACTION1));
        Transaction transaction2 = transactionRepository.save(createTransaction(TRANSACTION2));
        Transaction transaction3 = transactionRepository.save(createTransaction(TRANSACTION3));
        Transaction transaction4 = transactionRepository.save(createTransaction(TRANSACTION4));
        Transaction transaction5 = transactionRepository.save(createTransaction(TRANSACTION5));

        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions).containsExactlyInAnyOrder(
                transaction1,
                transaction2,
                transaction3,
                transaction4,
                transaction5
        );

       transactionRepository.deleteAll();

        List<Transaction> remainingTransactions = transactionRepository.findAll();

        assertThat(remainingTransactions).isEmpty();

    }
}
