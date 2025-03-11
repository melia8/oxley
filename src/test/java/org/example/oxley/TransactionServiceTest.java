package org.example.oxley;

import org.example.oxley.domain.Transaction;
import org.example.oxley.dto.SearchType;
import org.example.oxley.dto.request.SearchDataDto;
import org.example.oxley.dto.response.SearchResultDto;
import org.example.oxley.mapper.TransactionMapper;
import org.example.oxley.repository.TransactionRepository;
import org.example.oxley.service.QuantitySum;
import org.example.oxley.service.Summing;
import org.example.oxley.service.TransactionService;
import org.example.oxley.service.ValueSum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    static final String[] TRANSACTION1 =
            "Coca Cola, Tesco, A1235, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, 123.1111, 1000.50"
                    .split(",");
    static final String[] TRANSACTION2 =
            "Coca Cola, Tesco, A1235, 3c7cb1e3-c00d-490b-ac59-946a8084d451, 2025-02-03, 1203.2222, 3010.60"
                    .split(",");
    static final String[] TRANSACTION3 =
            "Coca Cola, Tesco, A1236, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, 123.5555, 1000.50"
                    .split(",");
    static final String[] TRANSACTION4 =
            "Coca Cola, Tesco, A1237, 3c7cb1e3-c00d-490b-ac59-946a8084d451, 2025-02-03, 1203.1010, 5010.20"
                    .split(",");
    static final String[] TRANSACTION5 =
            "Coca Cola, Tesco, A1237, 3c7cb1e3-c00d-490b-ac59-946a8084d451, 2025-02-03, 1203.2020, 2010.50"
                    .split(",");

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    TransactionMapper transactionMapper;

    List<Summing> strategies = List.of(new QuantitySum(), new ValueSum());

    TransactionService transactionService;

    @BeforeEach
    void setup() {
        transactionService = new TransactionService(transactionRepository,
                transactionMapper, strategies);
    }


    @Test
    void transactionSearch_sumsValuesCorrectly() {
        when(transactionRepository.searchTransactions(any(), any(), any(), any(), any())).thenReturn(
                List.of(
                        createTransaction(TRANSACTION1),
                        createTransaction(TRANSACTION2),
                        createTransaction(TRANSACTION3),
                        createTransaction(TRANSACTION4),
                        createTransaction(TRANSACTION5)
                )
        );

        SearchDataDto searchData = new SearchDataDto(
                "Coca cola",
                "Tesco",
                List.of("A1235", "A1236", "A1237"),
                LocalDate.of(2012,1,1),
                LocalDate.of(2025, 3, 3),
                SearchType.VALUE
        );

        List<SearchResultDto> expectedResult = List.of(
                new SearchResultDto("A1235", "4011.10"),
                new SearchResultDto("A1236", "1000.50"),
                new SearchResultDto("A1237", "7020.70")
        );

        List<SearchResultDto> result = transactionService.searchTransactions(searchData);

        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResult);
    }

    @Test
    void transactionSearch_sumsQuantityCorrectly() {
        when(transactionRepository.searchTransactions(any(), any(), any(), any(), any())).thenReturn(
                List.of(
                        createTransaction(TRANSACTION1),
                        createTransaction(TRANSACTION2),
                        createTransaction(TRANSACTION3),
                        createTransaction(TRANSACTION4),
                        createTransaction(TRANSACTION5)
                )
        );

        SearchDataDto searchData = new SearchDataDto(
                "Coca cola",
                "Tesco",
                List.of("A1235", "A1236", "A1237"),
                LocalDate.of(2012,1,1),
                LocalDate.of(2025, 3, 3),
                SearchType.QUANTITY
        );

        List<SearchResultDto> expectedResult = List.of(
                new SearchResultDto("A1235", "1326.3333"),
                new SearchResultDto("A1236", "123.5555"),
                new SearchResultDto("A1237", "2406.3030")
        );

        List<SearchResultDto> result = transactionService.searchTransactions(searchData);

        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResult);
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

}
