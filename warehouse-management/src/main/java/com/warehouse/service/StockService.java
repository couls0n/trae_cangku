package com.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warehouse.entity.Stock;
import java.math.BigDecimal;

public interface StockService extends IService<Stock> {
    /**
     * 扣减库存（带分布式锁）
     */
    boolean deductStock(Long warehouseId, Long productId, BigDecimal quantity);

    /**
     * 增加库存
     */
    boolean addStock(Long warehouseId, Long productId, BigDecimal quantity);
}
