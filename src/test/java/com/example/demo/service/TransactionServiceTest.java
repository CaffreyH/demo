// src/test/java/com/example/transactionservice/service/TransactionServiceTest.java
package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    void testPagination() {
        
        Page<Transaction> page = new Page<>(1, 10);
        IPage<Transaction> result = transactionService.getAllTransactions(page);
        
        assertThat(result.getRecords()).hasSize(5);
    }
}