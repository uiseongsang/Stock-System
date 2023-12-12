package com.example.stock.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private PessimisticLockStockService stockService;

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

        assertEquals( 99L, stock.getQuantity());

    }

    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        // 멀티 스레드 사용
        int thread_count = 100;

        // 비동기로 실행하는 작업을 단순하게 사용하게 도와주는 API
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 100개의 요청을 끝날 떄까지 기달려햐하므로 활용
        CountDownLatch latch = new CountDownLatch(thread_count);

        for(int i = 0; i < thread_count; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown(); // 다른 스레드에서 작업이 끝나기 전까지 대기해주는 클래스
                }
            });
        }
        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0,stock.getQuantity());
    }
}