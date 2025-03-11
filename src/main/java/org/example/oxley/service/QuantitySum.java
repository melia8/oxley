package org.example.oxley.service;

import org.example.oxley.domain.Transaction;
import org.example.oxley.dto.SearchType;
import org.example.oxley.dto.response.SearchResultDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QuantitySum implements Summing{
    @Override
    public List<SearchResultDto> getSum(List<Transaction> transactions) {
        Map<String, Double> quantity = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getProductCode,
                        Collectors.summingDouble(Transaction::getQuantity)));

        return quantity.entrySet().stream()
                .map(entry ->
                        new SearchResultDto(entry.getKey(), String.format("%.4f", entry.getValue())))
                .toList();

    }

    @Override
    public boolean canUse(SearchType searchType) {
        return searchType==SearchType.QUANTITY;
    }
}
