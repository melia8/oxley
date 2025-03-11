package org.example.oxley.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.oxley.dto.request.CreateTransactionDto;
import org.example.oxley.dto.request.SearchDataDto;
import org.example.oxley.dto.response.SearchResultDto;
import org.example.oxley.dto.response.TransactionResponseDto;
import org.example.oxley.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping
    public TransactionResponseDto createTransaction(@RequestBody @Valid CreateTransactionDto createTransactionDto) {
        return transactionService.addTransaction(createTransactionDto);
    }

    @GetMapping
    public List<TransactionResponseDto> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/search")
    public List<SearchResultDto> searchTransactions(@RequestBody @Valid SearchDataDto searchData) {
        return transactionService.searchTransactions(searchData);
    }
}
