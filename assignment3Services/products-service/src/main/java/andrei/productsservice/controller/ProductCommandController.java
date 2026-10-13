package andrei.productsservice.controller;

import andrei.productsservice.command.*;
import andrei.productsservice.dto.ProductDto;
import andrei.productsservice.service.IProductCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final IProductCommandService productCommandService;
    private final CommandInvoker commandInvoker;

    @PostMapping
    public void create(@RequestBody ProductDto.ProductRequestDto request,
                       @RequestParam(required = false) String userEmail) {
        commandInvoker.execute(new CreateProductCommand(
                productCommandService,
                request.name(),
                request.description(),
                request.price(),
                request.shopId(),
                userEmail
        ));
    }

    @PutMapping("/updatePrice")
    public void updatePrice(@RequestParam Long id, @RequestParam BigDecimal price,
                            @RequestParam(required = false) String userEmail) {
        commandInvoker.execute(new UpdateProductPriceCommand(productCommandService, id, price, userEmail));
    }

    @PutMapping("/updateDescription")
    public void updateDescription(@RequestParam Long id, @RequestParam String description,
                                  @RequestParam(required = false) String userEmail) {
        commandInvoker.execute(new UpdateProductDescriptionCommand(productCommandService, id, description, userEmail));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        commandInvoker.execute(new DeleteProductCommand(productCommandService, id));
    }
}
