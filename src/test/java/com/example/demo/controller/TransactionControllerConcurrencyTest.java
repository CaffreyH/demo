package com.example.demo.controller;

import com.example.demo.dto.CreateTransactionRequest;
import com.example.demo.dto.TransactionResponse;
import com.example.demo.model.TransactionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author huyan
 * @since 2025/2/5 16:16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransactionControllerConcurrencyTest {
    @Autowired
    private TransactionController controller;

    @Test
    public void testAddDate() {
        int threads = 500;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threads);

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executor.execute(() -> {
                try {
                    CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
                    createTransactionRequest.setAmount(BigDecimal.valueOf(Math.random() * 100));
                    createTransactionRequest.setCategory("test");
                    createTransactionRequest.setType(TransactionType.INCOME);
                    createTransactionRequest.setDescription("test data");
                    startLatch.await();
                    TransactionResponse transaction = controller.createTransaction(createTransactionRequest);
                } catch (Exception e) {
                    fail("Concurrent access failed", e);
                } finally {
                    endLatch.countDown();
                }
            });
        }
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        try {
            endLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("token:" + duration + "ms");
        assertTrue(duration < 1000, "Response time exceeds 1000ms");
    }

    @Test
    public void testGetData() {
        int threads = 500;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threads);

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executor.execute(() -> {
                try {
                    long randomNumber = (long) (Math.random() * 5) + 1; // 生成1到5的随机数
                    startLatch.await();
                    TransactionResponse transaction = controller.getTransaction(randomNumber);
                } catch (Exception e) {
                    fail("Concurrent access failed", e);
                } finally {
                    endLatch.countDown();
                }
            });
        }
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        try {
            endLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("token:" + duration + "ms");
        assertTrue(duration < 500, "Response time exceeds 500ms");
    }
}
