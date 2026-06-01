package andrei.ordersservice.command;

import andrei.ordersservice.dto.OrderItemDto;
import andrei.ordersservice.service.IOrderCommandService;

import java.util.List;

public class CreateOrderCommand implements Command {

    private final IOrderCommandService receiver;
    private final Long userId;
    private final List<OrderItemDto.OrderItemRequestDto> items;

    public CreateOrderCommand(IOrderCommandService receiver, Long userId, List<OrderItemDto.OrderItemRequestDto> items) {
        this.receiver = receiver;
        this.userId = userId;
        this.items = items;
    }

    @Override
    public void execute() {
        receiver.createOrder(userId, items);
    }
}
