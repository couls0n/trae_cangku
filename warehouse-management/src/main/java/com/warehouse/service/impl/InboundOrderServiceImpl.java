package com.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warehouse.entity.InboundOrder;
import com.warehouse.mapper.InboundOrderMapper;
import com.warehouse.service.InboundOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InboundOrderServiceImpl extends ServiceImpl<InboundOrderMapper, InboundOrder> implements InboundOrderService {

    @Override
    public boolean save(InboundOrder inboundOrder) {
        if (inboundOrder.getOrderNo() == null || inboundOrder.getOrderNo().isEmpty()) {
            inboundOrder.setOrderNo("IN" + System.currentTimeMillis());
        }
        if (inboundOrder.getOrderTime() == null) {
            inboundOrder.setOrderTime(LocalDateTime.now());
        }
        return super.save(inboundOrder);
    }
}
