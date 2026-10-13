package andrei.ordersservice.command;

import andrei.ordersservice.service.IOrderCommandService;

public class RemoveOrderItemCommand implements Command {

    private final IOrderCommandService receiver;
    private final long itemId;

    public RemoveOrderItemCommand(IOrderCommandService receiver, long itemId) {
        this.receiver = receiver;
        this.itemId = itemId;
    }

    @Override
    public void execute() {
        receiver.removeItem(itemId);
    }
}
