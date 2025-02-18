package com.andromeda.muteq.Service;

import java.util.HashSet;
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
import com.andromeda.muteq.Util.ElementsResponse;

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
            section.getImage() != null ? section.getImage().getName() : null,
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
            section.getImage() != null ? section.getImage().getName() : null,
            new HashSet<>()
        );
    }

    public Section mapToEntity(SectionDTO section) {
        return new Section(
            section.id(),
            section.name(),
            section.description(),
            imageRepository.findByName(section.image()).orElse(null),
            itemRepository.findAllById(section.items()).stream().collect(Collectors.toList())
        );
    }
        
    public ElementsResponse<SectionDTO> getAllSections(Pageable page) {
        Page<Section> pageSection = repository.findAll(page);

        return new ElementsResponse<>(pageSection.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet()),
                pageSection.getTotalElements());
    }

    public SectionDTO getSectionById(Long id) {
        Section section = repository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        return mapToDTO(section);
    }

    public ElementsResponse<SectionDTO> getSectionsByName(String name, Pageable page) {
        Section section = new Section();
        section.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreCase()
            .withIgnorePaths("content")
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Page<Section> pageSection = repository.findAll(Example.of(section, matcher), page);
        
        return new ElementsResponse<>(pageSection.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet()),
                pageSection.getTotalElements());
    }

    public SectionDTO createSection(SectionDTO sectionDTO) {
        Section section = mapToEntity(sectionDTO);
        section.getItems().forEach(item -> item.setSection(section));
        Section savedSection = repository.save(section);
        return mapToDTO(savedSection);
    }

    public SectionDTO updateSection(Long id, SectionDTO sectionDTO) {
        Section section = repository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        section.setName(sectionDTO.name());
        section.setDescription(sectionDTO.description());
        section.setImage(imageRepository.findByName(sectionDTO.image()).orElse(null));
        section.setItems(itemRepository.findAllById(sectionDTO.items()).stream().map(item -> {
            item.setSection(section);
            return item;
        }).collect(Collectors.toList()));

        Section updatedSection = repository.save(section);
        return mapToDTO(updatedSection);
    }

    public void deleteSection(Long id) {
        Section section = repository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        repository.delete(section);
    }
}
