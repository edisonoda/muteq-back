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
import com.andromeda.muteq.DTO.ItemResponseDTO;
import com.andromeda.muteq.Entity.Category;
import com.andromeda.muteq.Entity.Item;
import com.andromeda.muteq.Entity.Section;
import com.andromeda.muteq.Repository.CategoryRepository;
import com.andromeda.muteq.Repository.ImageRepository;
import com.andromeda.muteq.Repository.ItemRepository;
import com.andromeda.muteq.Repository.SectionRepository;
import com.andromeda.muteq.Util.GroupedItemsResponse;

@Service
public class ItemService {
    @Autowired
    private ItemRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SectionService sectionService;

    public ItemResponseDTO mapToResponseDTO(Item item) {
        return new ItemResponseDTO(
            item.getId(),
            item.getName(),
            item.getManufacturer(),
            item.getDescription(),
            item.getYear(),
            item.getImage() != null ? item.getImage().getName() : null,
            item.getCategory() != null ? categoryService.mapToDTO(item.getCategory()) : null,
            item.getSection() != null ? sectionService.mapToDTO(item.getSection()) : null
        );
    }

    public ItemDTO mapToDTO(Item item) {
        return new ItemDTO(
            item.getId(),
            item.getName(),
            item.getManufacturer(),
            item.getDescription(),
            item.getYear(),
            item.getImage() != null ? item.getImage().getName() : null,
            item.getCategory() != null ? item.getCategory().getId() : null,
            item.getSection() != null ? item.getSection().getId() : null
        );
    }

    public Set<ItemDTO> mapToDTO(Set<Item> items) {
        return items.stream().map(item -> new ItemDTO(
            item.getId(),
            item.getName(),
            item.getManufacturer(),
            item.getDescription(),
            item.getYear(),
            item.getImage() != null ? item.getImage().getName() : null,
            item.getCategory() != null ? item.getCategory().getId() : null,
            item.getSection() != null ? item.getSection().getId() : null
        )).collect(Collectors.toSet());
    }

    public Item mapToEntity(ItemDTO item) {
        return new Item(
            item.id(),
            item.name(),
            item.manufacturer(),
            item.description(),
            item.year(),
            imageRepository.findByName(item.image()).orElse(null),
            categoryRepository.findById(item.category()).orElse(null),
            sectionRepository.findById(item.section()).orElse(null)
        );
    }

    public Set<Item> mapToEntity(Set<ItemDTO> items) {
        return items.stream().map(item -> new Item(
            item.id(),
            item.name(),
            item.manufacturer(),
            item.description(),
            item.year(),
            imageRepository.findByName(item.image()).orElse(null),
            categoryRepository.findById(item.id()).orElse(null),
            sectionRepository.findById(item.id()).orElse(null)
        )).collect(Collectors.toSet());
    }

    public Set<ItemResponseDTO> getAllItems(Pageable page) {
        Page<Item> pageItem = repository.findAll(page);

        return pageItem.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toSet());
    }

    public ItemResponseDTO getItemById(Long id) {
        Item item = repository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        return mapToResponseDTO(item);
    }

    public Set<ItemResponseDTO> getItemsByName(String name, Pageable page) {
        Item item = new Item();
        item.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreCase()
            .withIgnorePaths("content")
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        
        return repository.findAll(Example.of(item, matcher), page).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toSet());
    }

    public GroupedItemsResponse getItemsByCategory(Long id, Pageable page) {
        Page<Item> pageItem = repository.findAllByCategoryId(id, page);
        Category category = categoryRepository.findById(id).orElse(null);

        return new GroupedItemsResponse(
            pageItem.stream().map(this::mapToResponseDTO).collect(Collectors.toSet()),
            count(),
            category != null ? category.getName() : "Categoria"
        );
    }

    public GroupedItemsResponse getItemsBySection(Long id, Pageable page) {
        Page<Item> pageItem = repository.findAllBySectionId(id, page);
        Section section = sectionRepository.findById(id).orElse(null);

        return new GroupedItemsResponse(
            pageItem.stream().map(this::mapToResponseDTO).collect(Collectors.toSet()),
            count(),
            section != null ? section.getName() : "Seção"
        );
    }

    public ItemResponseDTO createItem(ItemDTO itemDTO) {
        Item item = mapToEntity(itemDTO);
        Item savedItem = repository.save(item);
        return mapToResponseDTO(savedItem);
    }

    public ItemResponseDTO updateItem(Long id, ItemDTO itemDTO) {
        Item item = repository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        item.setName(itemDTO.name());
        item.setManufacturer(itemDTO.manufacturer());
        item.setDescription(itemDTO.description());
        item.setYear(itemDTO.year());
        item.setImage(imageRepository.findByName(itemDTO.image()).orElse(null));
        item.setCategory(categoryRepository.findById(itemDTO.category()).orElse(null));
        item.setSection(sectionRepository.findById(itemDTO.section()).orElse(null));
        Item updatedItem = repository.save(item);
        return mapToResponseDTO(updatedItem);
    }

    public void deleteItem(Long id) {
        Item item = repository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        repository.delete(item);
    }

    public Long count() {
        return repository.count();
    }
}
