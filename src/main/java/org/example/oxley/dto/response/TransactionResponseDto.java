package org.example.oxley.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TransactionResponseDto (
    String manufacturer,
    String retailerId,
    String productCode,
    String transactionId,
    String transactionDate,
    String quantity,
    String value
){}
