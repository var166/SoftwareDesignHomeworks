package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class DeleteShopCommand implements Command {

    private final IShopCommandService receiver;
    private final long id;

    public DeleteShopCommand(IShopCommandService receiver, long id) {
        this.receiver = receiver;
        this.id = id;
    }

    @Override
    public void execute() {
        receiver.delete(id);
    }
}
