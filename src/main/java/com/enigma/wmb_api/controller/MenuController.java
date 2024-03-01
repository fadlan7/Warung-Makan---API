package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public Menu createNewMenu(@RequestBody Menu menu) {
        return menuService.create(menu);
    }

    @GetMapping(path = "/{id}")
    public Menu getMenuById(@PathVariable String id) {
        return menuService.getById(id);
    }

    @GetMapping
    public List<Menu> getAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "price", required = false) Float price,
            @RequestParam(name = "minPrice", required = false) Float minPrice,
            @RequestParam(name = "maxPrice", required = false) Float maxPrice
    ) {
        SearchMenuRequest request = SearchMenuRequest.builder()
                .name(name)
                .price(price)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();

        return menuService.getAll(request);
    }

    @PutMapping
    public Menu updateMenu(@RequestBody Menu menu) {
        return menuService.update(menu);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteById(@PathVariable String id) {
        menuService.delete(id);
        return "OK";
    }
}
