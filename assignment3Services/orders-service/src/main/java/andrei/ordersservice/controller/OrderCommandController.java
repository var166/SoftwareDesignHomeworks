package andrei.ordersservice.controller;

import andrei.ordersservice.command.*;
import andrei.ordersservice.dto.OrderDto;
import andrei.ordersservice.dto.OrderItemDto;
import andrei.ordersservice.service.IOrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderCommandController {

    private final IOrderCommandService orderCommandService;
    private final CommandInvoker commandInvoker;

    @PostMapping
    public void createOrder(@RequestBody OrderDto.OrderRequestDto request) {
        commandInvoker.execute(new CreateOrderCommand(orderCommandService, request.userId(), request.items()));
    }

    @PutMapping("/markAsPaid/{orderId}")
    public void markAsPaid(@PathVariable Long orderId) {
        commandInvoker.execute(new MarkOrderAsPaidCommand(orderCommandService, orderId));
    }

    @DeleteMapping("/delete/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        commandInvoker.execute(new DeleteOrderCommand(orderCommandService, orderId));
    }

    @PostMapping("/addItem")
    public void addItem(@RequestBody OrderItemDto.OrderItemRequestDto request, @RequestParam Long orderId) {
        commandInvoker.execute(new AddOrderItemCommand(
                orderCommandService,
                orderId,
                request.productId(),
                request.quantity(),
                request.pricePerUnit()
        ));
    }

    @DeleteMapping("/removeItem/{itemId}")
    public void removeItem(@PathVariable Long itemId) {
        commandInvoker.execute(new RemoveOrderItemCommand(orderCommandService, itemId));
    }
}
