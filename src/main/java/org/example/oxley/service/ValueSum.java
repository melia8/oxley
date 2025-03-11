package org.example.oxley.service;

import org.example.oxley.domain.Transaction;
import org.example.oxley.dto.SearchType;
import org.example.oxley.dto.response.SearchResultDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ValueSum implements Summing{
    @Override
    public List<SearchResultDto> getSum(List<Transaction> transactions) {
        Map<String, BigDecimal> quantity = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getProductCode,
                        Collectors.mapping(Transaction::getValue,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        return quantity.entrySet().stream()
                .map(entry ->
                        new SearchResultDto(entry.getKey(), entry.getValue().setScale(2, RoundingMode.CEILING).toString()))
                .toList();
    }

    @Override
    public boolean canUse(SearchType searchType) {
        return searchType==SearchType.VALUE;
    }
}
