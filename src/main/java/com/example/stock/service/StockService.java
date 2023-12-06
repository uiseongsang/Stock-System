package com.example.stock.service;

import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

// 재고를 감소 시키는 서비스
@Service
public class StockService {
    // CRUD가 필요하기 떄문에 DI 주입
    private final StockRepository stockRepository;

    // 생성자 주입
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
}
