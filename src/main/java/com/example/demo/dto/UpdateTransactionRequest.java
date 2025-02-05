package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateTransactionRequest {
    @Positive(message = "金额必须为正数")
    @Digits(integer = 15, fraction = 2, message = "金额格式无效")
    private BigDecimal amount;

    @Size(max = 50, message = "分类最大长度50个字符")
    private String category;

    @Size(max = 255, message = "描述最大长度255个字符")
    private String description;
}