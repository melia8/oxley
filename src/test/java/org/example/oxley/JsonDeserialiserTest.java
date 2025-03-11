package org.example.oxley;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.example.oxley.dto.request.CreateTransactionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JsonTest
public class JsonDeserialiserTest {

    @Autowired
    JacksonTester<CreateTransactionDto> transactionJacksonTester;

    @Test
    void createTransactionDto_usingSuppliedTestDefinition_createdSuccessfully() throws Exception {
        String manufacturer = "Coca Cola";
        String retailerId = "Tesco";
        String productCode = "A1235";
        String transactionId = "e1c7343e-101e-42ff-b26c-c9e2d441a351";
        String transactionDate = "2024-01-01";
        String quantity = "123.3456";
        String value = "44456.12";

        String input = """
                    {
                      "manufacturer": "%s",
                      "retailer_id": "%s",
                      "product_code": "%s",
                      "transaction_id": "%s",
                      "transaction_date": "%s",
                      "quantity": %s,
                      "value": %s
                    }
                """.formatted(manufacturer, retailerId, productCode, transactionId, transactionDate, quantity, value);

        CreateTransactionDto createTransactionDto = transactionJacksonTester.parseObject(input);

        assertThat(createTransactionDto.manufacturer()).isEqualTo(manufacturer);
        assertThat(createTransactionDto.retailerId()).isEqualTo(retailerId);
        assertThat(createTransactionDto.productCode()).isEqualTo(productCode);
        assertThat(createTransactionDto.transactionId().toString()).isEqualTo(transactionId);
        assertThat(createTransactionDto.transactionDate().toString()).isEqualTo(transactionDate);
        assertThat(createTransactionDto.quantity()).isEqualTo(Double.parseDouble(quantity));
        assertThat(createTransactionDto.value()).isEqualTo(BigDecimal.valueOf(Double.parseDouble(value)));
    }

    @Test
    void createTransactionDto_invalidDateInput_throwsException() throws Exception {
        String manufacturer = "Coca Cola";
        String retailerId = "Tesco";
        String productCode = "A1235";
        String transactionId = "e1c7343e-101e-42ff-b26c-c9e2d441a351";
        String transactionDate = "2024-JAN-01";
        String quantity = "123.3456";
        String value = "44456.12";

        String input = """
                    {
                      "manufacturer": "%s",
                      "retailer_id": "%s",
                      "product_code": "%s",
                      "transaction_id": "%s",
                      "transaction_date": "%s",
                      "quantity": %s,
                      "value": %s
                    }
                """.formatted(manufacturer, retailerId, productCode, transactionId, transactionDate, quantity, value);

        assertThatThrownBy(() -> transactionJacksonTester.parseObject(input))
                .isInstanceOf(InvalidFormatException.class)
                .hasMessageContaining("Cannot deserialize value of type `java.time.LocalDate` from String \"%s\""
                        .formatted(transactionDate));
    }
}
