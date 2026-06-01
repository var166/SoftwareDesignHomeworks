package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class UpdateStockCommand implements Command {

    private final IShopCommandService receiver;
    private final long shopId;
    private final long productId;
    private final int quantity;

    public UpdateStockCommand(IShopCommandService receiver, long shopId, long productId, int quantity) {
        this.receiver = receiver;
        this.shopId = shopId;
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public void execute() {
        receiver.updateStock(shopId, productId, quantity);
    }
}
