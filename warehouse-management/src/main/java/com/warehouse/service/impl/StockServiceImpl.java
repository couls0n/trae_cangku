package com.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warehouse.entity.Stock;
import com.warehouse.mapper.StockMapper;
import com.warehouse.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @Transactional
    public boolean deductStock(Long warehouseId, Long productId, BigDecimal quantity) {
        // 生成分布式锁的key
        String lockKey = "stock:deduct:" + warehouseId + ":" + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 尝试获取锁，最多等待10秒，锁自动过期时间30秒
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                try {
                    // 查询库存
                    QueryWrapper<Stock> wrapper = new QueryWrapper<>();
                    wrapper.eq("warehouse_id", warehouseId);
                    wrapper.eq("product_id", productId);
                    Stock stock = this.getOne(wrapper);

                    if (stock == null || stock.getQuantity().compareTo(quantity) < 0) {
                        return false; // 库存不足
                    }

                    // 扣减库存
                    stock.setQuantity(stock.getQuantity().subtract(quantity));
                    return this.updateById(stock);
                } finally {
                    lock.unlock();
                }
            }
            return false; // 未能获取锁
        } catch (Exception e) {
            throw new RuntimeException("库存扣减失败", e);
        }
    }

    @Override
    @Transactional
    public boolean addStock(Long warehouseId, Long productId, BigDecimal quantity) {
        // 生成分布式锁的key
        String lockKey = "stock:add:" + warehouseId + ":" + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                try {
                    // 查询库存
                    QueryWrapper<Stock> wrapper = new QueryWrapper<>();
                    wrapper.eq("warehouse_id", warehouseId);
                    wrapper.eq("product_id", productId);
                    Stock stock = this.getOne(wrapper);

                    if (stock == null) {
                        // 新建库存记录
                        stock = new Stock();
                        stock.setWarehouseId(warehouseId);
                        stock.setProductId(productId);
                        stock.setQuantity(quantity);
                        stock.setFrozenQuantity(BigDecimal.ZERO);
                        return this.save(stock);
                    } else {
                        // 增加库存
                        stock.setQuantity(stock.getQuantity().add(quantity));
                        return this.updateById(stock);
                    }
                } finally {
                    lock.unlock();
                }
            }
            return false; // 未能获取锁
        } catch (Exception e) {
            throw new RuntimeException("库存增加失败", e);
        }
    }
}
