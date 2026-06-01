package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class AddProductToShopCommand implements Command {

    private final IShopCommandService receiver;
    private final long shopId;
    private final long productId;
    private final int initialStock;

    public AddProductToShopCommand(IShopCommandService receiver, long shopId, long productId, int initialStock) {
        this.receiver = receiver;
        this.shopId = shopId;
        this.productId = productId;
        this.initialStock = initialStock;
    }

    @Override
    public void execute() {
        receiver.addProduct(shopId, productId, initialStock);
    }
}
