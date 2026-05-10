package andrei.ordersservice.controller;

import andrei.ordersservice.dto.OrderDto;
import andrei.ordersservice.dto.OrderItemDto;
import andrei.ordersservice.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    @GetMapping("/findAll")
    public List<OrderDto.OrderResponseDto> getAll(
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return orderQueryService.findAllSorted(sortBy, direction).stream()
                    .map(OrderDto.OrderResponseDto::from).toList();
        }
        return orderQueryService.findAll().stream()
                .map(OrderDto.OrderResponseDto::from).toList();
    }

    @GetMapping("/findById/{orderId}")
    public OrderDto.OrderResponseDto getById(@PathVariable Long orderId) {
        return OrderDto.OrderResponseDto.from(orderQueryService.findById(orderId));
    }

    @GetMapping("/findByUserId/{userId}")
    public List<OrderDto.OrderResponseDto> getByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return orderQueryService.findByUserIdSorted(userId, sortBy, direction).stream()
                    .map(OrderDto.OrderResponseDto::from).toList();
        }
        return orderQueryService.findByUserId(userId).stream()
                .map(OrderDto.OrderResponseDto::from).toList();
    }

    @GetMapping("/findByIsPaid")
    public List<OrderDto.OrderResponseDto> getByIsPaid(
            @RequestParam boolean isPaid,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return orderQueryService.findByIsPaidSorted(isPaid, sortBy, direction).stream()
                    .map(OrderDto.OrderResponseDto::from).toList();
        }
        return orderQueryService.findByIsPaid(isPaid).stream()
                .map(OrderDto.OrderResponseDto::from).toList();
    }

    @GetMapping("/findByUserIdAndIsPaid/{userId}")
    public List<OrderDto.OrderResponseDto> getByUserIdAndIsPaid(
            @PathVariable Long userId,
            @RequestParam boolean isPaid,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return orderQueryService.findByUserIdAndIsPaidSorted(userId, isPaid, sortBy, direction).stream()
                    .map(OrderDto.OrderResponseDto::from).toList();
        }
        return orderQueryService.findByUserIdAndIsPaid(userId, isPaid).stream()
                .map(OrderDto.OrderResponseDto::from).toList();
    }

    @GetMapping("/findItems/{orderId}")
    public List<OrderItemDto.OrderItemResponseDto> getItemsByOrderId(@PathVariable Long orderId) {
        return orderQueryService.findItemsByOrderId(orderId).stream()
                .map(OrderItemDto.OrderItemResponseDto::from).toList();
    }
}
