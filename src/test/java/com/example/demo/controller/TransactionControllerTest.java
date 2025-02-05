package com.example.demo.controller;

import com.example.demo.dto.CreateTransactionRequest;
import com.example.demo.dto.TransactionResponse;
import com.example.demo.dto.UpdateTransactionRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionType;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class TransactionControllerTest {

    @Autowired
    private TransactionController transactionController;


    private Transaction testTransaction;

    @BeforeEach
    void setup() {
        testTransaction = new Transaction(
                1L,
                new BigDecimal("100.50"),
                TransactionType.INCOME,
                "Salary",
                "Monthly salary",
                LocalDateTime.now()
        );
    }

    @Test
    void createTransaction_Success() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAmount(new BigDecimal("100.50"));
        request.setType(TransactionType.INCOME);
        request.setCategory("Salary");

        TransactionResponse responseTransaction = transactionController.createTransaction(request);
        assertEquals("Salary", responseTransaction.getCategory());
    }

    @Test
    void getTransaction_NotFound() throws Exception {
        try {
            transactionController.getTransaction(999L);
        } catch (ResourceNotFoundException e) {
            // 断言异常信息
            assertTrue(e.getMessage().contains("not found"));
        }
    }

    @Test
    void updateTransaction() throws Exception {

        UpdateTransactionRequest request = new UpdateTransactionRequest();
        TransactionResponse transactionResponse = null;
        request.setAmount(new BigDecimal("100"));
        request.setDescription("test");
        request.setCategory("test");
        try {
            transactionResponse = transactionController.updateTransaction(1L, request);
        } catch (IllegalArgumentException e) {
            TestCase.fail();
        }
        assertEquals(transactionResponse.getAmount(), BigDecimal.valueOf(100L));
    }
}