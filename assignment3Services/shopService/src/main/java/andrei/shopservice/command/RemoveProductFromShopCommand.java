package andrei.shopservice.command;

import andrei.shopservice.service.IShopCommandService;

public class RemoveProductFromShopCommand implements Command {

    private final IShopCommandService receiver;
    private final long shopId;
    private final long productId;

    public RemoveProductFromShopCommand(IShopCommandService receiver, long shopId, long productId) {
        this.receiver = receiver;
        this.shopId = shopId;
        this.productId = productId;
    }

    @Override
    public void execute() {
        receiver.removeProduct(shopId, productId);
    }
}
