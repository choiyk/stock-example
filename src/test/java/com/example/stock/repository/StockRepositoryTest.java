package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockRepositoryTest {

    @Autowired private StockRepository stockRepository;

    @Test
    @Transactional
    public void test() {
        Stock stock = new Stock(1, 1);
        stockRepository.save(stock);

        assertEquals(1, stockRepository.findAll().size());
    }
}