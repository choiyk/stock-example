package com.example.stock.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long productId;

    @Getter
    private long quantity;

    @Version
    private long version;

    public Stock(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void decrease(long quantity) {
        if(this.quantity - quantity < 0)
            throw new RuntimeException("재고가 부족합니다.");

        this.quantity -= quantity;
    }
}
