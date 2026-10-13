package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class CreateShopCommand implements Command {

    private final IShopCommandService receiver;
    private final String name;
    private final String address;
    private final String phone;
    private final String email;
    private final String description;
    private final Long adminId;

    public CreateShopCommand(IShopCommandService receiver, String name, String address,
                             String phone, String email, String description, Long adminId) {
        this.receiver = receiver;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.adminId = adminId;
    }

    @Override
    public void execute() {
        receiver.create(name, address, phone, email, description, adminId);
    }
}
