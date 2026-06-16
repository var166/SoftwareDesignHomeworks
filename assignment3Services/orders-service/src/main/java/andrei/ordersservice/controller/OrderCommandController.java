package andrei.ordersservice.controller;

import andrei.ordersservice.dto.OrderDto;
import andrei.ordersservice.dto.OrderItemDto;
import andrei.ordersservice.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderCommandController {

    private final OrderCommandService orderCommandService;

    @PostMapping
    public void createOrder(@RequestBody OrderDto.OrderRequestDto request) {
        orderCommandService.createOrder(request.userId(), request.items());
    }

    @PutMapping("/markAsPaid/{orderId}")
    public void markAsPaid(@PathVariable Long orderId) {
        orderCommandService.markAsPaid(orderId);
    }

    @DeleteMapping("/delete/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderCommandService.deleteOrder(orderId);
    }

    @PostMapping("/addItem")
    public void addItem(@RequestBody OrderItemDto.OrderItemRequestDto request, @RequestParam Long orderId) {
        orderCommandService.addItem(orderId, request.productId(), request.quantity(), request.pricePerUnit());
    }

    @DeleteMapping("/removeItem/{itemId}")
    public void removeItem(@PathVariable Long itemId) {
        orderCommandService.removeItem(itemId);
    }
}
