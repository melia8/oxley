package org.example.oxley.mapper;

import org.example.oxley.domain.Transaction;
import org.example.oxley.dto.request.CreateTransactionDto;
import org.example.oxley.dto.response.TransactionResponseDto;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class TransactionMapper {

    public Transaction from(CreateTransactionDto dto) {
        return new Transaction(
                dto.manufacturer(),
                dto.retailerId(),
                dto.productCode(),
                dto.transactionId(),
                dto.transactionDate(),
                dto.quantity(),
                dto.value()
        );
    }

    public TransactionResponseDto from(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getManufacturer(),
                transaction.getRetailerId(),
                transaction.getProductCode(),
                transaction.getTransactionId().toString(),
                transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                Double.toString(transaction.getQuantity()),
                transaction.getValue().toString()
        );
    }
}
