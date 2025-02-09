package com.andromeda.muteq.Service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.andromeda.muteq.DTO.ItemDTO;
import com.andromeda.muteq.Entity.Item;
import com.andromeda.muteq.Repository.ItemRepository;

@Service
public class ItemService {
    @Autowired
    private ItemRepository repository;

    public ItemDTO mapToDTO(Item item) {
        return new ItemDTO(item.getName(), item.getManufacturer(), item.getDescription(), item.getYear());
    }

    public Item mapToEntity(ItemDTO item) {
        return new Item(item.getName(), item.getManufacturer(), item.getDescription(), item.getYear());
    }

    public Set<ItemDTO> getAllItems(Pageable page) {
        Page<Item> pageUser = repository.findAll(page);

        return pageUser.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet());
    }

    public ItemDTO getItemById(Long id) {
        Item item = repository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        return mapToDTO(item);
    }

    // public Set<ItemDTO> getItemsByName(String name, Pageable page) {
    //     return repository.findItemsByName(name, page).stream()
    //             .map(this::mapToDTO)
    //             .collect(Collectors.toSet());
    // }

    public ItemDTO createItem(ItemDTO itemDTO) {
        Item item = mapToEntity(itemDTO);
        Item savedItem = repository.save(item);
        return mapToDTO(savedItem);
    }

    public ItemDTO updateItem(Long id, ItemDTO itemDTO) {
        Item item = repository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        item.setName(itemDTO.getName());
        item.setManufacturer(itemDTO.getManufacturer());
        item.setDescription(itemDTO.getDescription());
        item.setYear(itemDTO.getYear());
        Item updatedItem = repository.save(item);
        return mapToDTO(updatedItem);
    }

    public void deleteItem(Long id) {
        Item item = repository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        repository.delete(item);
    }
}
