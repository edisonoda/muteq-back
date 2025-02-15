package com.andromeda.muteq.Service;

import java.util.HashSet;
import java.util.Set;
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
            category.getImage().getId(),
            category.getImage().getName(),
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
            category.getImage().getId(),
            category.getImage().getName(),
            new HashSet<>()
        );
    }

    public Category mapToEntity(CategoryDTO category) {
        return new Category(
            category.id(),
            category.name(),
            category.description(),
            imageRepository.findByPath(category.image()),
            itemRepository.findAllById(category.items()).stream().collect(Collectors.toSet())
        );
    }
    
    public Set<CategoryDTO> getAllCategories(Pageable page) {
        Page<Category> pageCategory = repository.findAll(page);

        return pageCategory.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category item = repository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToDTO(item);
    }

    public Set<CategoryDTO> getCategoriesByName(String name, Pageable page) {
        Category item = new Category();
        item.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreCase()
            .withIgnorePaths("content")
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        
        return repository.findAll(Example.of(item, matcher), page).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet());
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category item = mapToEntity(categoryDTO);
        Category savedCategory = repository.save(item);
        return mapToDTO(savedCategory);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = repository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());
        category.setImage(imageRepository.findById(categoryDTO.image_id()).get());
        category.setItems(itemRepository.findAllById(categoryDTO.items()).stream().collect(Collectors.toSet()));
        Category updatedCategory = repository.save(category);
        return mapToDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category item = repository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        repository.delete(item);
    }
}
