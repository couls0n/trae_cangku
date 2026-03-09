package com.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.PageResult;
import com.warehouse.common.Result;
import com.warehouse.entity.Warehouse;
import com.warehouse.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@CrossOrigin
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/list")
    public Result<List<Warehouse>> list() {
        return Result.success(warehouseService.list());
    }

    @GetMapping("/page")
    public Result<PageResult<Warehouse>> page(@RequestParam(defaultValue = "1") Integer current,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) String warehouseName) {
        Page<Warehouse> page = new Page<>(current, size);
        QueryWrapper<Warehouse> wrapper = new QueryWrapper<>();
        if (warehouseName != null && !warehouseName.isEmpty()) {
            wrapper.like("warehouse_name", warehouseName);
        }
        Page<Warehouse> result = warehouseService.page(page, wrapper);
        PageResult<Warehouse> pageResult = new PageResult<>(result.getTotal(), result.getRecords());
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Warehouse> getById(@PathVariable Long id) {
        return Result.success(warehouseService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Warehouse warehouse) {
        return Result.success(warehouseService.save(warehouse));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Warehouse warehouse) {
        return Result.success(warehouseService.updateById(warehouse));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(warehouseService.removeById(id));
    }
}
