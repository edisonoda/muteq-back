package com.andromeda.muteq.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.andromeda.muteq.DTO.CategoryDTO;
import com.andromeda.muteq.Entity.Category;
import com.andromeda.muteq.Repository.CategoryRepository;
import com.andromeda.muteq.Repository.ImageRepository;
import com.andromeda.muteq.Repository.ItemRepository;
import com.andromeda.muteq.Util.ElementsResponse;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ImageRepository imageRepository;

    public CategoryDTO mapToDTO(Category category) {
        return new CategoryDTO(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getImage() != null ? category.getImage().getName() : null,
            category.getItems().stream().map(item -> item.getId()).collect(Collectors.toSet())
        );
    }

    public CategoryDTO mapToDTO(Category category, boolean withoutItems) {
        if (!withoutItems)
            return mapToDTO(category);

        return new CategoryDTO(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getImage() != null ? category.getImage().getName() : null,
            new HashSet<>()
        );
    }

    public Category mapToEntity(CategoryDTO category) {
        return new Category(
            category.id(),
            category.name(),
            category.description(),
            imageRepository.findByName(category.image()).orElse(null),
            itemRepository.findAllById(category.items()).stream().collect(Collectors.toList())
        );
    }
    
    public ElementsResponse<CategoryDTO> getAllCategories(Pageable page) {
        Page<Category> pageCategory = repository.findAll(page);

        return new ElementsResponse<>(pageCategory.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet()),
                pageCategory.getTotalElements());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = repository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToDTO(category);
    }

    public ElementsResponse<CategoryDTO> getCategoriesByName(String name, Pageable page) {
        Category category = new Category();
        category.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreCase()
            .withIgnorePaths("content")
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        
        Page<Category> pageCategory = repository.findAll(Example.of(category, matcher), page);

        return new ElementsResponse<>(pageCategory.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet()),
                pageCategory.getTotalElements());
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = mapToEntity(categoryDTO);
        category.getItems().forEach(item -> item.setCategory(category));
        Category savedCategory = repository.save(category);
        return mapToDTO(savedCategory);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = repository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());
        category.setImage(imageRepository.findByName(categoryDTO.image()).orElse(null));
        category.setItems(itemRepository.findAllById(categoryDTO.items()).stream().map(item -> {
            item.setCategory(category);
            return item;
        }).collect(Collectors.toList()));
        Category updatedCategory = repository.save(category);
        return mapToDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = repository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        repository.delete(category);
    }
}
