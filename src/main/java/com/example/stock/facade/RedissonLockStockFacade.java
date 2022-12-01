package com.example.stock.facade;

import com.example.stock.Service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;

    private final StockService stockService;

    public void decrease(long id, long quantity) throws InterruptedException {
        final String key = String.valueOf(id);

        RLock lock = redissonClient.getLock(key);

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);  //waitTime: 락을 획득하지 못하면 이 시간 동안 재시도. leaseTime: 락을 획득한 후 이 시간이 지나면 락을 해제함.

            if(!available) {
                log.error("락을 획득하지 못했습니다.");
                return;
            }

            stockService.decrease(id, quantity);
        }
        finally {
            lock.unlock();
        }
    }
}
