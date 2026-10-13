package andrei.productsservice.controller;

import andrei.productsservice.dto.ProductDto;
import andrei.productsservice.export.CsvProductExporter;
import andrei.productsservice.export.JsonProductExporter;
import andrei.productsservice.export.ProductExporter;
import andrei.productsservice.export.XmlProductExporter;
import andrei.productsservice.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductQueryController {

    private final ProductQueryService productQueryService;

    @GetMapping("/findById/{id}")
    public ProductDto.ProductResponseDto getById(@PathVariable Long id) {
        return ProductDto.ProductResponseDto.from(productQueryService.findById(id));
    }

    @GetMapping("/findAll")
    public List<ProductDto.ProductResponseDto> getAll(
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return productQueryService.findAllSorted(sortBy, direction).stream()
                    .map(ProductDto.ProductResponseDto::from).toList();
        }
        return productQueryService.findAll().stream()
                .map(ProductDto.ProductResponseDto::from).toList();
    }

    @GetMapping("/findByName/{name}")
    public ProductDto.ProductResponseDto getByName(@PathVariable String name) {
        return ProductDto.ProductResponseDto.from(productQueryService.findByName(name));
    }

    @GetMapping("/findByShopId/{shopId}")
    public List<ProductDto.ProductResponseDto> getByShopId(
            @PathVariable Long shopId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return productQueryService.findByShopIdSorted(shopId, sortBy, direction).stream()
                    .map(ProductDto.ProductResponseDto::from).toList();
        }
        return productQueryService.findByShopId(shopId).stream()
                .map(ProductDto.ProductResponseDto::from).toList();
    }

    @GetMapping("/export")
    public ResponseEntity<String> export(
            @RequestParam(defaultValue = "json") String format,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) BigDecimal min,
            @RequestParam(required = false) BigDecimal max) {
        var products = (min != null && max != null)
                ? (sortBy != null ? productQueryService.findByPriceRangeSorted(min, max, sortBy, direction)
                                  : productQueryService.findByPriceRange(min, max))
                : (sortBy != null ? productQueryService.findAllSorted(sortBy, direction)
                                  : productQueryService.findAll());
        ProductExporter exporter = switch (format.toLowerCase()) {
            case "csv"  -> new CsvProductExporter();
            case "xml"  -> new XmlProductExporter();
            default     -> new JsonProductExporter();
        };
        String content = exporter.export(products);
        String contentType = switch (format.toLowerCase()) {
            case "csv"  -> "text/csv";
            case "xml"  -> "application/xml";
            default     -> "application/json";
        };
        return ResponseEntity.ok()
                .header("Content-Type", contentType + "; charset=UTF-8")
                .header("Content-Disposition", "attachment; filename=\"products." + format + "\"")
                .body(content);
    }

    @GetMapping("/findByPriceRange")
    public List<ProductDto.ProductResponseDto> getByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return productQueryService.findByPriceRangeSorted(min, max, sortBy, direction).stream()
                    .map(ProductDto.ProductResponseDto::from).toList();
        }
        return productQueryService.findByPriceRange(min, max).stream()
                .map(ProductDto.ProductResponseDto::from).toList();
    }
}
