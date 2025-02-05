// src/main/java/com/example/transactionservice/model/Transaction.java
package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    public Transaction(Long id, BigDecimal amount, TransactionType type, String description) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    @TableId(type = IdType.AUTO)
    private Long id;

    private BigDecimal amount;
    private TransactionType type;
    private String category;
    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime timestamp;
}