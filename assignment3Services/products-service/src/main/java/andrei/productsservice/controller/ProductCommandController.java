package andrei.productsservice.controller;

import andrei.productsservice.dto.ProductDto;
import andrei.productsservice.service.ProductCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final ProductCommandService productCommandService;

    @PostMapping
    public void create(@RequestBody ProductDto.ProductRequestDto request,
                       @RequestParam(required = false) String userEmail) {
        productCommandService.create(
                request.name(),
                request.description(),
                request.price(),
                request.shopId(),
                userEmail
        );
    }

    @PutMapping("/updatePrice")
    public void updatePrice(@RequestParam Long id, @RequestParam BigDecimal price,
                            @RequestParam(required = false) String userEmail) {
        productCommandService.updatePrice(id, price, userEmail);
    }

    @PutMapping("/updateDescription")
    public void updateDescription(@RequestParam Long id, @RequestParam String description,
                                  @RequestParam(required = false) String userEmail) {
        productCommandService.updateDescription(id, description, userEmail);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        productCommandService.delete(id);
    }
}
