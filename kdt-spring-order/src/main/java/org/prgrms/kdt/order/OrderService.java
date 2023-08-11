package org.prgrms.kdt.order;

import org.prgrms.kdt.voucher.VoucherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final VoucherService voucherService;
    private final OrderRepository orderRepository;

    public OrderService(VoucherService voucherService, OrderRepository orderRepository) {
        this.voucherService = voucherService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(UUID customerID, List<OrderItem> orderItems) {
        var order = new Order(UUID.randomUUID(), customerID, orderItems);
        return orderRepository.insert(order);
    }

    public Order createOrder(UUID customerID, List<OrderItem> orderItems, UUID voucherId) {
        var voucher = voucherService.getVoucher(voucherId);
        var order = new Order(UUID.randomUUID(), customerID, orderItems, voucher);
        orderRepository.insert(order);
        voucherService.userVoucher(voucher);
        return order;
    }
}
