package com.andromeda.muteq.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.andromeda.muteq.DTO.ItemDTO;
import com.andromeda.muteq.Service.ItemService;
import com.andromeda.muteq.Util.Constants;
import com.andromeda.muteq.Util.DefaultResponse;
import com.andromeda.muteq.Util.ElementsResponse;
import com.andromeda.muteq.Util.GroupedItemsResponse;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<ElementsResponse<ItemDTO>> getAllItems(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size > Constants.MAX_PAGE_SIZE ? Constants.MAX_PAGE_SIZE : size);
        Set<ItemDTO> items = itemService.getAllItems(pageable);

        ElementsResponse<ItemDTO> res = new ElementsResponse<ItemDTO>(items, itemService.count());
        
        return ResponseEntity.ok(res);
    }

    @GetMapping("/category")
    public ResponseEntity<GroupedItemsResponse> getItemsByCategory(
            @RequestParam() Long category,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size > Constants.MAX_PAGE_SIZE ? Constants.MAX_PAGE_SIZE : size);
        GroupedItemsResponse res = itemService.getItemsByCategory(category, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/section")
    public ResponseEntity<GroupedItemsResponse> getItemsBySection(
            @RequestParam() Long section,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size > Constants.MAX_PAGE_SIZE ? Constants.MAX_PAGE_SIZE : size);
        GroupedItemsResponse res = itemService.getItemsBySection(section, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<Set<ItemDTO>> getItemsByName(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Set<ItemDTO> items = itemService.getItemsByName(name, pageable);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        ItemDTO item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO) {
        ItemDTO createdItem = itemService.createItem(itemDTO);
        return ResponseEntity.ok(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
        ItemDTO updatedItem = itemService.updateItem(id, itemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResponse<Boolean>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(new DefaultResponse<Boolean>(true));
    }
}
