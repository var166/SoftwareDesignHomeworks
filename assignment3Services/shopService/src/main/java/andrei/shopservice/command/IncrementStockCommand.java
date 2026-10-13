package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class IncrementStockCommand implements Command {

    private final IShopCommandService receiver;
    private final long shopId;
    private final long productId;
    private final int amount;

    public IncrementStockCommand(IShopCommandService receiver, long shopId, long productId, int amount) {
        this.receiver = receiver;
        this.shopId = shopId;
        this.productId = productId;
        this.amount = amount;
    }

    @Override
    public void execute() {
        receiver.incrementStock(shopId, productId, amount);
    }
}
