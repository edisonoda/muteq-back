package com.andromeda.muteq.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.andromeda.muteq.DTO.ItemDTO;
import com.andromeda.muteq.Service.ItemService;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
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
    public ResponseEntity<Set<ItemDTO>> getAllItems(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        // Pageable pageable = PageRequest.of(page, size);
        Set<ItemDTO> items = itemService.getAllItems(new Pageable());
        return ResponseEntity.ok(items);
    }

    // @GetMapping
    // public ResponseEntity<Set<ItemDTO>> getItemsByName(
    //         @RequestParam(name = "name", defaultValue = " ") String name,
    //         @RequestParam(name = "page", defaultValue = "0") Integer page,
    //         @RequestParam(name = "size", defaultValue = "10") Integer size) {
    //     // Pageable pageable = PageRequest.of(page, size);
    //     Set<ItemDTO> items = itemService.getItemsByName(name, new Pageable());
    //     return ResponseEntity.ok(items);
    // }

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
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
