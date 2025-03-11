package org.example.oxley;

import org.example.oxley.controller.TransactionController;
import org.example.oxley.dto.response.TransactionResponseDto;
import org.example.oxley.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Value( "${jwt.secretKey}" )
    String secretKey;

    @MockitoBean
    TransactionService transactionService;

    @ParameterizedTest
    @CsvSource({
            "'',Tesco, A1235, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, 123.3456, 44456.12",
            "Coca Cola,'', A1235, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, 123.3456, 44456.12",
            "Coca Cola,Tesco, '', e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, 123.3456, 44456.12",
            "Coca Cola,Tesco, A1235, '', 2024-01-01, 123.3456, 44456.12",
            "Coca Cola,Tesco, A1235, e1c7343e-101e-42ff-b26c-c9e2d441a351,'', 123.3456, 44456.12",
            "Coca Cola,Tesco, A1235, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, , 44456.12",
            "Coca Cola,Tesco, A1235, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, 123.3456, , 44456.12''"
    })
    void postJsonWithAnEmptyField_throwsValidationException(String manufacturer, String retailerId,
                                     String productCode, String  transactionId,
                                     String transactionDate, String quantity, String value) throws Exception {

        String input = """
                    { "manufacturer": "%s",
                      "retailer_id": "%s",
                      "product_code": "%s",
                      "transaction_id": "%s",
                      "transaction_date": "%s",
                      "quantity": %s,
                      "value": %s }
                """.formatted(manufacturer, retailerId, productCode, transactionId, transactionDate, quantity, value);


        mockMvc.perform(post("/transactions")
                        .content(input)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException().getClass())
                                .isEqualTo(MethodArgumentNotValidException.class));
    }

    @ParameterizedTest
    @CsvSource({
            "Coca Cola, Tesco, A1235, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, 123.3456, 44456.12",
            "Coca Cola, Tesco, A1236, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2025-02-25, 999.3456, 11111.12"
    })
    void postJsonWithValidFields_success(String manufacturer, String retailerId,
                                     String productCode, String  transactionId,
                                     String transactionDate, String quantity, String value) throws Exception {
        String input = """
                    { "manufacturer": "%s",
                      "retailer_id": "%s",
                      "product_code": "%s",
                      "transaction_id": "%s",
                      "transaction_date": "%s",
                      "quantity": %s,
                      "value": %s }
                """.formatted(manufacturer, retailerId, productCode, transactionId, transactionDate, quantity, value);

        TransactionResponseDto response = new TransactionResponseDto(
                manufacturer,
                retailerId,
                productCode,
                transactionId,
                transactionDate,
                quantity,
                value
        );

        when(transactionService.addTransaction(any())).thenReturn(response);

        mockMvc.perform(post("/transactions")
                        .content(input)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.retailer_id").value(retailerId))
                .andExpect(jsonPath("$.manufacturer").value(manufacturer))
                .andExpect(jsonPath("$.product_code").value(productCode))
                .andExpect(jsonPath("$.transaction_date").value(transactionDate))
                .andExpect(jsonPath("$.quantity").value(quantity))
                .andExpect(jsonPath("$.value").value(value));
    }

    @Test
    void getAllTransactions_success() throws Exception {
        String[] transaction1 = "Coca Cola, Tesco, A1235, e1c7343e-101e-42ff-b26c-c9e2d441a351, 2024-01-01, 123.3456, 44456.12".split(",");
        String[] transaction2 = "Coca Cola, Lidl, A1236, 3c7cb1e3-c00d-490b-ac59-946a8084d451, 2025-02-03, 1203.6534, 99456.12".split(",");

        List<TransactionResponseDto> response = List.of(
                createTransactionResponseDto(transaction1),
                createTransactionResponseDto(transaction2)
        );

        when(transactionService.getAllTransactions()).thenReturn(response);

        mockMvc.perform(get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].manufacturer").value(transaction1[0]))
                .andExpect(jsonPath("$[0].retailer_id").value(transaction1[1]))
                .andExpect(jsonPath("$[0].product_code").value(transaction1[2]))
                .andExpect(jsonPath("$[0].transaction_id").value(transaction1[3]))
                .andExpect(jsonPath("$[0].transaction_date").value(transaction1[4]))
                .andExpect(jsonPath("$[0].quantity").value(transaction1[5]))
                .andExpect(jsonPath("$[0].value").value(transaction1[6]))
                .andExpect(jsonPath("$[1].manufacturer").value(transaction2[0]))
                .andExpect(jsonPath("$[1].retailer_id").value(transaction2[1]))
                .andExpect(jsonPath("$[1].product_code").value(transaction2[2]))
                .andExpect(jsonPath("$[1].transaction_id").value(transaction2[3]))
                .andExpect(jsonPath("$[1].transaction_date").value(transaction2[4]))
                .andExpect(jsonPath("$[1].quantity").value(transaction2[5]))
                .andExpect(jsonPath("$[1].value").value(transaction2[6]));

    }

    @Test
    void invalidUrl_returnsNotFound() throws Exception {
        mockMvc.perform(get("/transactions/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.message").value("Could not find object"));
    }

    @Test
    void invalidPostBody_returnsNotFound() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Bad request"));
    }

    @Test
    void delete_validAdminToken_success() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.U5uekccSES74PO9gGPdHUJLyjf9qIG0mRtQ6IPx1hDM";

        when(transactionService.getAllTransactions()).thenReturn(Collections.emptyList());

        mockMvc.perform(delete("/transactions")
                        .header("Authorization", "Bearer %s".formatted(jwt)))
                .andExpect(status().is(204));
    }

    @Test
    void delete_invalidAdminToken_success() throws Exception {
        String jwt = "INVALIDGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.U5uekccSES74PO9gGPdHUJLyjf9qIG0mRtQ6IPx1hDM";

        mockMvc.perform(delete("/transactions")
                        .header("Authorization", "Bearer %s".formatted(jwt)))
                .andExpect(status().is(403))
                .andExpect(jsonPath("$.message").value("Forbidden"));
    }

    TransactionResponseDto createTransactionResponseDto(String[] fields) {
        return new TransactionResponseDto(fields[0], fields[1], fields[2], fields[3],
                fields[4], fields[5], fields[6]);

    }


}
