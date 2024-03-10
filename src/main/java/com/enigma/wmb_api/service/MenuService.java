package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.NewMenuRequest;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.request.UpdateMenuRequest;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.entity.Menu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MenuService {
    MenuResponse create(NewMenuRequest request);

    Menu getById(String id);

    Page<MenuResponse> getAll(SearchMenuRequest request);

    MenuResponse update(UpdateMenuRequest request);

    void delete(String id);
}
