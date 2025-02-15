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

import com.andromeda.muteq.DTO.SectionDTO;
import com.andromeda.muteq.Entity.Section;
import com.andromeda.muteq.Repository.ImageRepository;
import com.andromeda.muteq.Repository.ItemRepository;
import com.andromeda.muteq.Repository.SectionRepository;

@Service
public class SectionService {
    @Autowired
    private SectionRepository repository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ImageRepository imageRepository;

    public SectionDTO mapToDTO(Section section) {
        return new SectionDTO(
            section.getId(),
            section.getName(),
            section.getDescription(),
            section.getImage().getId(),
            section.getImage().getName(),
            section.getItems().stream().map(item -> item.getId()).collect(Collectors.toSet())
        );
    }

    public SectionDTO mapToDTO(Section section, boolean withoutItems) {
        if (!withoutItems)
            return mapToDTO(section);

        return new SectionDTO(
            section.getId(),
            section.getName(),
            section.getDescription(),
            section.getImage().getId(),
            section.getImage().getName(),
            new HashSet<>()
        );
    }

    public Section mapToEntity(SectionDTO section) {
        return new Section(
            section.id(),
            section.name(),
            section.description(),
            imageRepository.findByPath(section.image()),
            itemRepository.findAllById(section.items()).stream().collect(Collectors.toSet())
        );
    }
        
    public Set<SectionDTO> getAllSections(Pageable page) {
        Page<Section> pageSection = repository.findAll(page);

        return pageSection.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet());
    }

    public SectionDTO getSectionById(Long id) {
        Section item = repository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        return mapToDTO(item);
    }

    public Set<SectionDTO> getSectionsByName(String name, Pageable page) {
        Section item = new Section();
        item.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreCase()
            .withIgnorePaths("content")
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        
        return repository.findAll(Example.of(item, matcher), page).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet());
    }

    public SectionDTO createSection(SectionDTO sectionDTO) {
        Section item = mapToEntity(sectionDTO);
        Section savedSection = repository.save(item);
        return mapToDTO(savedSection);
    }

    public SectionDTO updateSection(Long id, SectionDTO sectionDTO) {
        Section section = repository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        section.setName(sectionDTO.name());
        section.setDescription(sectionDTO.description());
        section.setImage(imageRepository.findById(sectionDTO.image_id()).get());
        section.setItems(itemRepository.findAllById(sectionDTO.items()).stream().collect(Collectors.toSet()));
        Section updatedSection = repository.save(section);
        return mapToDTO(updatedSection);
    }

    public void deleteSection(Long id) {
        Section item = repository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        repository.delete(item);
    }
}
