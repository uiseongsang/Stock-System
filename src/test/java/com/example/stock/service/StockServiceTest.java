package com.example.stock.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    // Test를 위해선 재고가 있어야 하므로 BeforeEach()를 통해서 테스트가 실행하기 전에 재고를 넣어준다
    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L,100L));
    }

    // Test가 종료되면 모든 재고를 업애준다
    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    // 재고 로직에 대한 Test코드 작성
    @Test
    public void 재고감소() {
        stockService.decrease(1L, 1L);

        // 100 - 1 = 99
        Stock stock = stockRepository.findById(1L).orElseThrow();

        Assertions.assertEquals( 99L, stock.getQuantity());

    }
}