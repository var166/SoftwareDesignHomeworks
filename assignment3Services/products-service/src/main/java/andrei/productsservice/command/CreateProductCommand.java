package andrei.productsservice.command;

import andrei.productsservice.service.IProductCommandService;

import java.math.BigDecimal;

public class CreateProductCommand implements Command {

    private final IProductCommandService receiver;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Long shopId;
    private final String userEmail;

    public CreateProductCommand(IProductCommandService receiver, String name, String description,
                                BigDecimal price, Long shopId, String userEmail) {
        this.receiver = receiver;
        this.name = name;
        this.description = description;
        this.price = price;
        this.shopId = shopId;
        this.userEmail = userEmail;
    }

    @Override
    public void execute() {
        receiver.create(name, description, price, shopId, userEmail);
    }
}
