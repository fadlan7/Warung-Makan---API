package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.entity.Menu;

import java.util.List;

public interface MenuService {
    Menu create(Menu menu);

    Menu getById(String id);

    List<Menu> getAll(SearchMenuRequest request);

    Menu update(Menu menu);

    void delete(String id);
}
