package org.example.oxley.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.validation.Valid;
import org.example.oxley.dto.request.CreateTransactionDto;
import org.example.oxley.dto.request.SearchDataDto;
import org.example.oxley.dto.response.SearchResultDto;
import org.example.oxley.dto.response.TransactionResponseDto;
import org.example.oxley.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final String jwtSecretKey;

    public TransactionController(@Value("${jwt.secretKey}") String jwtSecretKey,  TransactionService transactionService) {
        this.jwtSecretKey = jwtSecretKey;
        this.transactionService = transactionService;

    }
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

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteAllTransactions(@RequestHeader(value="Authorization") String token) {
        if (decodeToken(token).equals("Auth")) {
            transactionService.deleteTransactions();
        }
    }

    private String decodeToken(String token) throws SecurityException{
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
        String jwt = token.split(" ")[1];
        String role = "";

        try {
            role = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload().getSubject();
        } catch(Exception ex) {
            throw new SecurityException("Invalid JWT token");
        }

        return role;
    }
}
