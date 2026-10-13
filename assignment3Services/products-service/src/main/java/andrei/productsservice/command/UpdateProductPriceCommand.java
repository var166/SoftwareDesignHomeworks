package andrei.productsservice.command;

import andrei.productsservice.service.IProductCommandService;

import java.math.BigDecimal;

public class UpdateProductPriceCommand implements Command {

    private final IProductCommandService receiver;
    private final long id;
    private final BigDecimal price;
    private final String userEmail;

    public UpdateProductPriceCommand(IProductCommandService receiver, long id, BigDecimal price, String userEmail) {
        this.receiver = receiver;
        this.id = id;
        this.price = price;
        this.userEmail = userEmail;
    }

    @Override
    public void execute() {
        receiver.updatePrice(id, price, userEmail);
    }
}
