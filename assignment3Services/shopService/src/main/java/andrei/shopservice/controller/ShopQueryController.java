package andrei.shopservice.controller;

import andrei.shopservice.dto.ShopDto;
import andrei.shopservice.service.ShopQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopQueryController {

    private final ShopQueryService shopQueryService;

    @GetMapping("/findAll")
    public List<ShopDto.ShopResponseDto> getAll(
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return shopQueryService.findAllSorted(sortBy, direction).stream()
                    .map(ShopDto.ShopResponseDto::from).toList();
        }
        return shopQueryService.findAll().stream()
                .map(ShopDto.ShopResponseDto::from).toList();
    }

    @GetMapping("/findById/{id}")
    public ShopDto.ShopResponseDto getById(@PathVariable Long id) {
        return ShopDto.ShopResponseDto.from(shopQueryService.findById(id));
    }

    @GetMapping("/findByName/{name}")
    public ShopDto.ShopResponseDto getByName(@PathVariable String name) {
        return ShopDto.ShopResponseDto.from(shopQueryService.findByName(name));
    }

    @GetMapping("/findByEmail")
    public ShopDto.ShopResponseDto getByEmail(@RequestParam String email) {
        return ShopDto.ShopResponseDto.from(shopQueryService.findByEmail(email));
    }

    @GetMapping("/findByAdminId/{adminId}")
    public List<ShopDto.ShopResponseDto> getByAdminId(
            @PathVariable Long adminId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        if (sortBy != null) {
            return shopQueryService.findByAdminIdSorted(adminId, sortBy, direction).stream()
                    .map(ShopDto.ShopResponseDto::from).toList();
        }
        return shopQueryService.findByAdminId(adminId).stream()
                .map(ShopDto.ShopResponseDto::from).toList();
    }
}
