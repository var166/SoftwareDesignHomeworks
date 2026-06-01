package andrei.productsservice.command;

import andrei.productsservice.service.IProductCommandService;

public class UpdateProductDescriptionCommand implements Command {

    private final IProductCommandService receiver;
    private final long id;
    private final String description;
    private final String userEmail;

    public UpdateProductDescriptionCommand(IProductCommandService receiver, long id, String description, String userEmail) {
        this.receiver = receiver;
        this.id = id;
        this.description = description;
        this.userEmail = userEmail;
    }

    @Override
    public void execute() {
        receiver.updateDescription(id, description, userEmail);
    }
}
