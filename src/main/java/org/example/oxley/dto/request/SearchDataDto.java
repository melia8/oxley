package org.example.oxley.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.oxley.dto.SearchType;

import java.time.LocalDate;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SearchDataDto(
    @NotEmpty
    String manufacturer,

    @NotEmpty
    String retailerId,

    @NotNull
    List<String> productList,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate,

    @NotEmpty
    SearchType sum
) {}
