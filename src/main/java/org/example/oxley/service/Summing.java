package org.example.oxley.service;

import org.example.oxley.domain.Transaction;
import org.example.oxley.dto.SearchType;
import org.example.oxley.dto.response.SearchResultDto;

import java.util.List;

public interface Summing {

   List<SearchResultDto> getSum(List<Transaction> transactions);
   boolean canUse(SearchType searchType);
}
