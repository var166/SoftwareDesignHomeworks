package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class UpdateShopEmailCommand implements Command {

    private final IShopCommandService receiver;
    private final long id;
    private final String email;

    public UpdateShopEmailCommand(IShopCommandService receiver, long id, String email) {
        this.receiver = receiver;
        this.id = id;
        this.email = email;
    }

    @Override
    public void execute() {
        receiver.updateEmail(id, email);
    }
}
