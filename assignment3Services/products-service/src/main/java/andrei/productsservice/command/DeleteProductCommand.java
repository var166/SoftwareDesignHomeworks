package andrei.productsservice.command;

import andrei.productsservice.service.IProductCommandService;

public class DeleteProductCommand implements Command {

    private final IProductCommandService receiver;
    private final long id;

    public DeleteProductCommand(IProductCommandService receiver, long id) {
        this.receiver = receiver;
        this.id = id;
    }

    @Override
    public void execute() {
        receiver.delete(id);
    }
}
