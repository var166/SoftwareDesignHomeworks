package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class UpdateShopPhoneCommand implements Command {

    private final IShopCommandService receiver;
    private final long id;
    private final String phone;

    public UpdateShopPhoneCommand(IShopCommandService receiver, long id, String phone) {
        this.receiver = receiver;
        this.id = id;
        this.phone = phone;
    }

    @Override
    public void execute() {
        receiver.updatePhone(id, phone);
    }
}
