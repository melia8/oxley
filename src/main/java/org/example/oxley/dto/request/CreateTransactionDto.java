package org.example.oxley.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateTransactionDto(

    @NotEmpty
    String manufacturer,

    @NotEmpty
    String retailerId,

    @NotEmpty
    String productCode,

    @NotNull
    UUID transactionId,

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate transactionDate,

    @NotNull
    Double quantity,

    @NotNull
    BigDecimal value
) { }
