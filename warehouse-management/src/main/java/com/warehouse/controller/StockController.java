package com.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.PageResult;
import com.warehouse.common.Result;
import com.warehouse.entity.Stock;
import com.warehouse.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/list")
    public Result<List<Stock>> list() {
        return Result.success(stockService.list());
    }

    @GetMapping("/page")
    public Result<PageResult<Stock>> page(@RequestParam(defaultValue = "1") Integer current,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam(required = false) Long warehouseId,
                                           @RequestParam(required = false) Long productId) {
        Page<Stock> page = new Page<>(current, size);
        QueryWrapper<Stock> wrapper = new QueryWrapper<>();
        if (warehouseId != null) {
            wrapper.eq("warehouse_id", warehouseId);
        }
        if (productId != null) {
            wrapper.eq("product_id", productId);
        }
        Page<Stock> result = stockService.page(page, wrapper);
        PageResult<Stock> pageResult = new PageResult<>(result.getTotal(), result.getRecords());
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Stock> getById(@PathVariable Long id) {
        return Result.success(stockService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Stock stock) {
        return Result.success(stockService.save(stock));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Stock stock) {
        return Result.success(stockService.updateById(stock));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(stockService.removeById(id));
    }
}
