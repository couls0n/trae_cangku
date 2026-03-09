package com.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.PageResult;
import com.warehouse.common.Result;
import com.warehouse.entity.OutboundOrder;
import com.warehouse.service.OutboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outbound")
@CrossOrigin
public class OutboundOrderController {

    @Autowired
    private OutboundOrderService outboundOrderService;

    @GetMapping("/list")
    public Result<List<OutboundOrder>> list() {
        return Result.success(outboundOrderService.list());
    }

    @GetMapping("/page")
    public Result<PageResult<OutboundOrder>> page(@RequestParam(defaultValue = "1") Integer current,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestParam(required = false) String orderNo) {
        Page<OutboundOrder> page = new Page<>(current, size);
        QueryWrapper<OutboundOrder> wrapper = new QueryWrapper<>();
        if (orderNo != null && !orderNo.isEmpty()) {
            wrapper.like("order_no", orderNo);
        }
        wrapper.orderByDesc("create_time");
        Page<OutboundOrder> result = outboundOrderService.page(page, wrapper);
        PageResult<OutboundOrder> pageResult = new PageResult<>(result.getTotal(), result.getRecords());
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<OutboundOrder> getById(@PathVariable Long id) {
        return Result.success(outboundOrderService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody OutboundOrder outboundOrder) {
        return Result.success(outboundOrderService.save(outboundOrder));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody OutboundOrder outboundOrder) {
        return Result.success(outboundOrderService.updateById(outboundOrder));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(outboundOrderService.removeById(id));
    }
}
