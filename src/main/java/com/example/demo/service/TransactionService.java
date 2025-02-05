// src/main/java/com/example/transactionservice/service/TransactionService.java
package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;

    @Cacheable(value = "transactions", key = "#page.current + '-' + #page.size")
    public IPage<Transaction> getAllTransactions(Page<Transaction> page) {
        return transactionMapper.selectPage(page,
                new LambdaQueryWrapper<Transaction>()
                        .orderByDesc(Transaction::getTimestamp));
    }

    public IPage<Transaction> getByType(TransactionType type, Page<Transaction> page) {
        return transactionMapper.selectPage(page,
                new LambdaQueryWrapper<Transaction>()
                        .eq(Transaction::getType, type)
                        .orderByDesc(Transaction::getTimestamp));
    }

    public IPage<Transaction> getByCategory(String category, Page<Transaction> page) {
        return transactionMapper.selectPage(page,
                new LambdaQueryWrapper<Transaction>()
                        .eq(Transaction::getCategory, category)
                        .orderByDesc(Transaction::getTimestamp));
    }

    public IPage<Transaction> getByTypeAndCategory(TransactionType type,String category, Page<Transaction> page){
        return transactionMapper.selectPage(page,
                new LambdaQueryWrapper<Transaction>()
                        .eq(Transaction::getType, type)
                        .eq(Transaction::getCategory, category)
                        .orderByDesc(Transaction::getTimestamp));
    }

    public IPage<Transaction> getByTypeAndAmountRange(
            TransactionType type,
            BigDecimal min,
            BigDecimal max,
            Page<Transaction> page
    ) {
        return transactionMapper.findByTypeAndAmountRange(page, type, min, max);
    }

    public Transaction getById(Long id) {
        Transaction transaction = transactionMapper.selectById(id);
        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
        }
        return transaction;
    }

    @CacheEvict(value = {"transactions", "transaction"}, allEntries = true)
    public void save(Transaction transaction) {
        transactionMapper.insert(transaction);
    }

    @CacheEvict(value = {"transactions", "transaction"}, allEntries = true)
    public void updateById(Transaction transaction) {
        transactionMapper.updateById(transaction);
    }

    @CacheEvict(value = {"transactions", "transaction"}, allEntries = true)
    public void removeById(Long id) {
        transactionMapper.deleteById(id);
    }

    @Cacheable(value = "transaction", key = "#id")
    public Transaction getTransactionWithCache(Long id) {
        return getById(id);
    }
}