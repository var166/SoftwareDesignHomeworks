package andrei.ordersservice.command;

import andrei.ordersservice.service.IOrderCommandService;

public class DeleteOrderCommand implements Command {

    private final IOrderCommandService receiver;
    private final long orderId;

    public DeleteOrderCommand(IOrderCommandService receiver, long orderId) {
        this.receiver = receiver;
        this.orderId = orderId;
    }

    @Override
    public void execute() {
        receiver.deleteOrder(orderId);
    }
}
