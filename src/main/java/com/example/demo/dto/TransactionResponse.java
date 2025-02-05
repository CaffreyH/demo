// src/main/java/com/example/transactionservice/dto/TransactionResponse.java
package com.example.demo.dto;

import com.example.demo.model.TransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String category;
    private String description;
    private LocalDateTime timestamp;
}