package andrei.shopservice.controller;

import andrei.shopservice.dto.ShopDto;
import andrei.shopservice.service.ShopCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopCommandController {

    private final ShopCommandService shopCommandService;

    @PostMapping
    public void create(@RequestBody ShopDto.ShopRequestDto request) {
        shopCommandService.create(
                request.name(),
                request.address(),
                request.phone(),
                request.email(),
                request.description(),
                request.adminId()
        );
    }

    @PutMapping("/updateName")
    public void updateName(@RequestParam Long id, @RequestParam String name) {
        shopCommandService.updateName(id, name);
    }

    @PutMapping("/updateAddress")
    public void updateAddress(@RequestParam Long id, @RequestParam String address) {
        shopCommandService.updateAddress(id, address);
    }

    @PutMapping("/updatePhone")
    public void updatePhone(@RequestParam Long id, @RequestParam String phone) {
        shopCommandService.updatePhone(id, phone);
    }

    @PutMapping("/updateEmail")
    public void updateEmail(@RequestParam Long id, @RequestParam String email) {
        shopCommandService.updateEmail(id, email);
    }

    @PutMapping("/updateDescription")
    public void updateDescription(@RequestParam Long id, @RequestParam String description) {
        shopCommandService.updateDescription(id, description);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        shopCommandService.delete(id);
    }

    @PostMapping("/addProduct")
    public void addProduct(@RequestParam Long shopId, @RequestParam Long productId, @RequestParam int initialStock) {
        shopCommandService.addProduct(shopId, productId, initialStock);
    }

    @DeleteMapping("/removeProduct")
    public void removeProduct(@RequestParam Long shopId, @RequestParam Long productId) {
        shopCommandService.removeProduct(shopId, productId);
    }

    @PutMapping("/updateStock")
    public void updateStock(@RequestParam Long shopId, @RequestParam Long productId, @RequestParam int quantity) {
        shopCommandService.updateStock(shopId, productId, quantity);
    }

    @PutMapping("/incrementStock")
    public void incrementStock(@RequestParam Long shopId, @RequestParam Long productId, @RequestParam int amount) {
        shopCommandService.incrementStock(shopId, productId, amount);
    }

    @PutMapping("/decrementStock")
    public void decrementStock(@RequestParam Long shopId, @RequestParam Long productId, @RequestParam int amount) {
        shopCommandService.decrementStock(shopId, productId, amount);
    }
}
