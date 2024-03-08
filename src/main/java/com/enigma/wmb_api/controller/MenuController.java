package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.MENU_API)
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<CommonResponse<Menu>> createNewMenu(@RequestBody Menu menu) {

        Menu newMenu = menuService.create(menu);

        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully create new menu")
                .data(newMenu)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<Menu>> getMenuById(@PathVariable String id) {
        Menu menu = menuService.getById(id);

        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Menu found")
                .data(menu)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<Menu>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "price", required = false) Float price,
            @RequestParam(name = "minPrice", required = false) Float minPrice,
            @RequestParam(name = "maxPrice", required = false) Float maxPrice
    ) {
        SearchMenuRequest request = SearchMenuRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .price(price)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();
        Page<Menu> menus = menuService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(menus.getTotalPages())
                .totalElement(menus.getTotalElements())
                .page(menus.getPageable().getPageNumber() + 1)
                .size(menus.getPageable().getPageSize())
                .hasNext(menus.hasNext())
                .hasPrevious(menus.hasPrevious())
                .build();

        CommonResponse<List<Menu>> response = CommonResponse.<List<Menu>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get all menu")
                .data(menus.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<Menu>> updateMenu(@RequestBody Menu menu) {
        Menu updatedMenu = menuService.update(menu);

        CommonResponse<Menu> response = CommonResponse.<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Menu successfully modified")
                .data(updatedMenu)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> deleteById(@PathVariable String id) {
        menuService.delete(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Menu successfully deleted")
                .build();

        return ResponseEntity.ok(response);
    }
}
