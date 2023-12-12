package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockStockFacade {
    private final OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        // update할 떄 실패시 재 시도를 해야하므로
        while(true) {
            try {
                optimisticLockStockService.decrease(id,quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
