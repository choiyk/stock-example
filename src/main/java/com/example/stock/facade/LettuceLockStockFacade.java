package com.example.stock.facade;

import com.example.stock.Service.StockService;
import com.example.stock.repository.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;

    private final StockService stockService;

    public void decrease(long id, long quantity) throws InterruptedException {
        while(!redisLockRepository.lock(id)) {
            Thread.sleep(50);
        }

        try {
            stockService.decrease(id, quantity);
        }
        finally {
            redisLockRepository.unlock(id);
        }
    }
}
