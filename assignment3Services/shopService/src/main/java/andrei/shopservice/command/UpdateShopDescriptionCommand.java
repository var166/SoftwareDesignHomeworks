package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class UpdateShopDescriptionCommand implements Command {

    private final IShopCommandService receiver;
    private final long id;
    private final String description;

    public UpdateShopDescriptionCommand(IShopCommandService receiver, long id, String description) {
        this.receiver = receiver;
        this.id = id;
        this.description = description;
    }

    @Override
    public void execute() {
        receiver.updateDescription(id, description);
    }
}
