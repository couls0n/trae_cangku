package com.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.PageResult;
import com.warehouse.common.Result;
import com.warehouse.entity.InboundOrder;
import com.warehouse.service.InboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inbound")
@CrossOrigin
public class InboundOrderController {

    @Autowired
    private InboundOrderService inboundOrderService;

    @GetMapping("/list")
    public Result<List<InboundOrder>> list() {
        return Result.success(inboundOrderService.list());
    }

    @GetMapping("/page")
    public Result<PageResult<InboundOrder>> page(@RequestParam(defaultValue = "1") Integer current,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestParam(required = false) String orderNo) {
        Page<InboundOrder> page = new Page<>(current, size);
        QueryWrapper<InboundOrder> wrapper = new QueryWrapper<>();
        if (orderNo != null && !orderNo.isEmpty()) {
            wrapper.like("order_no", orderNo);
        }
        wrapper.orderByDesc("create_time");
        Page<InboundOrder> result = inboundOrderService.page(page, wrapper);
        PageResult<InboundOrder> pageResult = new PageResult<>(result.getTotal(), result.getRecords());
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<InboundOrder> getById(@PathVariable Long id) {
        return Result.success(inboundOrderService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody InboundOrder inboundOrder) {
        return Result.success(inboundOrderService.save(inboundOrder));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody InboundOrder inboundOrder) {
        return Result.success(inboundOrderService.updateById(inboundOrder));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(inboundOrderService.removeById(id));
    }
}
