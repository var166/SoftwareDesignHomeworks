package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class UpdateShopNameCommand implements Command {

    private final IShopCommandService receiver;
    private final long id;
    private final String name;

    public UpdateShopNameCommand(IShopCommandService receiver, long id, String name) {
        this.receiver = receiver;
        this.id = id;
        this.name = name;
    }

    @Override
    public void execute() {
        receiver.updateName(id, name);
    }
}
