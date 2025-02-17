package com.andromeda.muteq.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.andromeda.muteq.DTO.ItemDTO;
import com.andromeda.muteq.DTO.ItemResponseDTO;
import com.andromeda.muteq.Service.ItemService;
import com.andromeda.muteq.Util.Constants;
import com.andromeda.muteq.Util.DefaultResponse;
import com.andromeda.muteq.Util.ElementsResponse;
import com.andromeda.muteq.Util.GroupedItemsResponse;

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
    public ResponseEntity<ElementsResponse<ItemResponseDTO>> getAllItems(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size > Constants.MAX_PAGE_SIZE ? Constants.MAX_PAGE_SIZE : size);
        ElementsResponse<ItemResponseDTO> res = itemService.getAllItems(pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<ElementsResponse<ItemResponseDTO>> getItemsByName(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        ElementsResponse<ItemResponseDTO> res = itemService.getItemsByName(name, pageable);
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

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
        ItemResponseDTO item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> createItem(@RequestBody ItemDTO itemDTO) {
        ItemResponseDTO createdItem = itemService.createItem(itemDTO);
        return ResponseEntity.ok(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
        ItemResponseDTO updatedItem = itemService.updateItem(id, itemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResponse<Boolean>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(new DefaultResponse<Boolean>(true));
    }
}
