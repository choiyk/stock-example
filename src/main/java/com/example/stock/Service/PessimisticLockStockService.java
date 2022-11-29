package com.example.stock.Service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessimisticLockStockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(long id, long quantity) {
        Stock stock = stockRepository.findByIdWithPessimistickLock(id);

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
