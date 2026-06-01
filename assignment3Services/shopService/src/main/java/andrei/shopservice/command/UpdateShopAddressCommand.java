package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class UpdateShopAddressCommand implements Command {

    private final IShopCommandService receiver;
    private final long id;
    private final String address;

    public UpdateShopAddressCommand(IShopCommandService receiver, long id, String address) {
        this.receiver = receiver;
        this.id = id;
        this.address = address;
    }

    @Override
    public void execute() {
        receiver.updateAddress(id, address);
    }
}
