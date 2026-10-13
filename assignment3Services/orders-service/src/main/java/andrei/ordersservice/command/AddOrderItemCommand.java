package andrei.ordersservice.command;

import andrei.ordersservice.service.IOrderCommandService;

import java.math.BigDecimal;

public class AddOrderItemCommand implements Command {

    private final IOrderCommandService receiver;
    private final long orderId;
    private final Long productId;
    private final int quantity;
    private final BigDecimal pricePerUnit;

    public AddOrderItemCommand(IOrderCommandService receiver, long orderId, Long productId,
                               int quantity, BigDecimal pricePerUnit) {
        this.receiver = receiver;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public void execute() {
        receiver.addItem(orderId, productId, quantity, pricePerUnit);
    }
}
