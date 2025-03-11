package org.example.oxley.service;

import lombok.AllArgsConstructor;
import org.example.oxley.domain.Transaction;
import org.example.oxley.dto.request.CreateTransactionDto;
import org.example.oxley.dto.request.SearchDataDto;
import org.example.oxley.dto.response.SearchResultDto;
import org.example.oxley.dto.response.TransactionResponseDto;
import org.example.oxley.mapper.TransactionMapper;
import org.example.oxley.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;
    private TransactionMapper transactionMapper;
    private List<Summing> sumStrategies;

    public TransactionResponseDto addTransaction(CreateTransactionDto createTransactionDto) {
        Transaction transaction = transactionRepository.save(transactionMapper.from(createTransactionDto));
        return transactionMapper.from(transaction);
    }

    public List<TransactionResponseDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::from)
                .toList();
    }

    public List<SearchResultDto> searchTransactions(SearchDataDto searchDataDto) {

        List<Transaction> transactions = transactionRepository.searchTransactions(
                searchDataDto.manufacturer(),
                searchDataDto.retailerId(),
                searchDataDto.productList(),
                searchDataDto.startDate(),
                searchDataDto.endDate()
        );

        return sumStrategies.stream()
                .filter(s -> s.canUse(searchDataDto.sum()))
                .findFirst()
                .orElseThrow()
                .getSum(transactions);

    }
}
