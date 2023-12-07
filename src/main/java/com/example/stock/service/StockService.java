package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 재고를 감소 시키는 서비스
@Service
public class StockService {
    // CRUD가 필요하기 떄문에 DI 주입
    private final StockRepository stockRepository;

    // 생성자 주입
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    //@Transactional
    public synchronized void decrease(Long id, Long quantity) {
        // stock 조회
        Stock stock = stockRepository.findById(id).orElseThrow();

        // 재고를 감소
        stock.decrease(quantity);

        // 갱신된 값을 저장
        stockRepository.saveAndFlush(stock);
    }
}
