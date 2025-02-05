// src/main/java/com/example/transactionservice/controller/TransactionController.java
package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dto.CreateTransactionRequest;
import com.example.demo.dto.TransactionResponse;
import com.example.demo.dto.UpdateTransactionRequest;
import com.example.demo.model.TransactionType;
import com.example.demo.model.Transaction;
import com.example.demo.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        Transaction transaction = modelMapper.map(request, Transaction.class);
        transactionService.save(transaction);
        return modelMapper.map(transaction, TransactionResponse.class);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "transactions", key = "#id")
    public TransactionResponse getTransaction(@PathVariable Long id) {
        Transaction transaction = transactionService.getById(id);
        return modelMapper.map(transaction, TransactionResponse.class);
    }

    @GetMapping
    public IPage<TransactionResponse> listTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return transactionService.getAllTransactions(new Page<>(page, size))
                .convert(t -> modelMapper.map(t, TransactionResponse.class));
    }

    @PutMapping("/{id}")
    public TransactionResponse updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransactionRequest request
    ) {
        Transaction transaction = transactionService.getById(id);
        modelMapper.map(request, transaction);
        transactionService.updateById(transaction);
        return modelMapper.map(transaction, TransactionResponse.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "transactions", allEntries = true)
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.removeById(id);
    }

    @GetMapping("/search")
    public IPage<TransactionResponse> searchTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Transaction> pageable = new Page<>(page, size);
        IPage<Transaction> result;
        
        if (type != null && category != null) {
            result = transactionService.getByTypeAndCategory(type, category, pageable);
        } else if (type != null) {
            result = transactionService.getByType(type, pageable);
        } else if (category != null) {
            result = transactionService.getByCategory(category, pageable);
        } else {
            result = transactionService.getAllTransactions(pageable);
        }
        
        return result.convert(t -> modelMapper.map(t, TransactionResponse.class));
    }
}