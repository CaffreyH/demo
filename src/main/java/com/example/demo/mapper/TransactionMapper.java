package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.model.TransactionType;
import com.example.demo.model.Transaction;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;


public interface TransactionMapper extends BaseMapper<Transaction> {
    IPage<Transaction> findByTypeAndAmountRange(
        IPage<Transaction> page,
        @Param("type") TransactionType type,
        @Param("min") BigDecimal min,
        @Param("max") BigDecimal max
    );
}