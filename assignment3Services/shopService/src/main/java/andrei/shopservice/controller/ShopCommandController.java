package andrei.shopservice.controller;

import andrei.shopservice.command.*;
import andrei.shopservice.dto.ShopDto;
import andrei.shopservice.service.IShopCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopCommandController {

    private final IShopCommandService shopCommandService;
    private final CommandInvoker commandInvoker;

    @PostMapping
    public void create(@RequestBody ShopDto.ShopRequestDto request) {
        commandInvoker.execute(new CreateShopCommand(
                shopCommandService,
                request.name(),
                request.address(),
                request.phone(),
                request.email(),
                request.description(),
                request.adminId()
        ));
    }

    @PutMapping("/updateName")
    public void updateName(@RequestParam Long id, @RequestParam String name) {
        commandInvoker.execute(new UpdateShopNameCommand(shopCommandService, id, name));
    }

    @PutMapping("/updateAddress")
    public void updateAddress(@RequestParam Long id, @RequestParam String address) {
        commandInvoker.execute(new UpdateShopAddressCommand(shopCommandService, id, address));
    }

    @PutMapping("/updatePhone")
    public void updatePhone(@RequestParam Long id, @RequestParam String phone) {
        commandInvoker.execute(new UpdateShopPhoneCommand(shopCommandService, id, phone));
    }

    @PutMapping("/updateEmail")
    public void updateEmail(@RequestParam Long id, @RequestParam String email) {
        commandInvoker.execute(new UpdateShopEmailCommand(shopCommandService, id, email));
    }

    @PutMapping("/updateDescription")
    public void updateDescription(@RequestParam Long id, @RequestParam String description) {
        commandInvoker.execute(new UpdateShopDescriptionCommand(shopCommandService, id, description));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        commandInvoker.execute(new DeleteShopCommand(shopCommandService, id));
    }

    @PostMapping("/addProduct")
    public void addProduct(@RequestParam Long shopId, @RequestParam Long productId, @RequestParam int initialStock) {
        commandInvoker.execute(new AddProductToShopCommand(shopCommandService, shopId, productId, initialStock));
    }

    @DeleteMapping("/removeProduct")
    public void removeProduct(@RequestParam Long shopId, @RequestParam Long productId) {
        commandInvoker.execute(new RemoveProductFromShopCommand(shopCommandService, shopId, productId));
    }

    @PutMapping("/updateStock")
    public void updateStock(@RequestParam Long shopId, @RequestParam Long productId, @RequestParam int quantity) {
        commandInvoker.execute(new UpdateStockCommand(shopCommandService, shopId, productId, quantity));
    }

    @PutMapping("/incrementStock")
    public void incrementStock(@RequestParam Long shopId, @RequestParam Long productId, @RequestParam int amount) {
        commandInvoker.execute(new IncrementStockCommand(shopCommandService, shopId, productId, amount));
    }

    @PutMapping("/decrementStock")
    public void decrementStock(@RequestParam Long shopId, @RequestParam Long productId, @RequestParam int amount) {
        commandInvoker.execute(new DecrementStockCommand(shopCommandService, shopId, productId, amount));
    }
}
