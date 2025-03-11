package org.example.oxley.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@IdClass(TransactionPk.class)
public class Transaction {

    @Id
    private String manufacturer;

    @Id
    private String retailerId;

    @Id
    private String productCode;

    @Id
    private UUID transactionId;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private double quantity;

    @Column(name = "cost", nullable = false)
    private BigDecimal value;

}
