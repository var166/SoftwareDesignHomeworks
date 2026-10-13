package andrei.ordersservice.command;

import andrei.ordersservice.service.IOrderCommandService;

public class MarkOrderAsPaidCommand implements Command {

    private final IOrderCommandService receiver;
    private final long orderId;

    public MarkOrderAsPaidCommand(IOrderCommandService receiver, long orderId) {
        this.receiver = receiver;
        this.orderId = orderId;
    }

    @Override
    public void execute() {
        receiver.markAsPaid(orderId);
    }
}
