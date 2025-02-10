package com.andromeda.muteq.Service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.andromeda.muteq.DTO.ItemDTO;
import com.andromeda.muteq.Entity.Item;
import com.andromeda.muteq.Repository.ImageRepository;
import com.andromeda.muteq.Repository.ItemRepository;

@Service
public class ItemService {
    @Autowired
    private ItemRepository repository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private ImageRepository imageRepository;

    public ItemDTO mapToDTO(Item item) {
        return new ItemDTO(
            item.getId(),
            item.getName(),
            item.getManufacturer(),
            item.getDescription(),
            item.getYear(),
            item.getImage().getId(),
            item.getImage().getPath(),
            categoryService.mapToDTO(item.getCategory()),
            sectionService.mapToDTO(item.getSection())
        );
    }

    public Set<ItemDTO> mapToDTO(Set<Item> items) {
        return items.stream().map(item -> new ItemDTO(
            item.getId(),
            item.getName(),
            item.getManufacturer(),
            item.getDescription(),
            item.getYear(),
            item.getImage().getId(),
            item.getImage().getPath(),
            categoryService.mapToDTO(item.getCategory()),
            sectionService.mapToDTO(item.getSection())
        )).collect(Collectors.toSet());
    }

    public Item mapToEntity(ItemDTO item) {
        return new Item(
            item.id(),
            item.name(),
            item.manufacturer(),
            item.description(),
            item.year(),
            imageRepository.findByPath(item.image()),
            categoryService.mapToEntity(item.category()),
            sectionService.mapToEntity(item.section())
        );
    }

    public Set<Item> mapToEntity(Set<ItemDTO> items) {
        return items.stream().map(item -> new Item(
            item.id(),
            item.name(),
            item.manufacturer(),
            item.description(),
            item.year(),
            imageRepository.findByPath(item.image()),
            categoryService.mapToEntity(item.category()),
            sectionService.mapToEntity(item.section())
        )).collect(Collectors.toSet());
    }

    public Set<ItemDTO> getAllItems(Pageable page) {
        Page<Item> pageItem = repository.findAll(page);

        return pageItem.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet());
    }

    public ItemDTO getItemById(Long id) {
        Item item = repository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        return mapToDTO(item);
    }

    public Set<ItemDTO> getItemsByName(String name, Pageable page) {
        Item item = new Item();
        item.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreCase()
            .withIgnorePaths("content")
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        
        return repository.findAll(Example.of(item, matcher), page).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet());
    }

    // public Set<ItemDTO> getItemsByCategory(Long id, Pageable page) {
    //     Item item = new Item();
    //     item.setCategory(id);

    //     ExampleMatcher matcher = ExampleMatcher.matching()
    //         .withIgnoreCase()
    //         .withIgnorePaths("content")
    //         .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        
    //     return repository.findAll(Example.of(item, matcher), page).stream()
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
        item.setName(itemDTO.name());
        item.setManufacturer(itemDTO.manufacturer());
        item.setDescription(itemDTO.description());
        item.setYear(itemDTO.year());
        item.setImage(imageRepository.findById(itemDTO.image_id()).get());
        item.setSection(sectionService.mapToEntity(itemDTO.section()));
        item.setCategory(categoryService.mapToEntity(itemDTO.category()));
        Item updatedItem = repository.save(item);
        return mapToDTO(updatedItem);
    }

    public void deleteItem(Long id) {
        Item item = repository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        repository.delete(item);
    }

    public Long count() {
        return repository.count();
    }
}
