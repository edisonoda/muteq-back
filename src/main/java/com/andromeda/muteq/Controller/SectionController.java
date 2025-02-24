package com.andromeda.muteq.Controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.muteq.DTO.SectionDTO;
import com.andromeda.muteq.Service.SectionService;
import com.andromeda.muteq.Util.Constants;
import com.andromeda.muteq.Util.DefaultResponse;
import com.andromeda.muteq.Util.ElementsResponse;

@RestController
@RequestMapping("/section")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping
    public ResponseEntity<ElementsResponse<SectionDTO>> getAllSections(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size > Constants.MAX_PAGE_SIZE ? Constants.MAX_PAGE_SIZE : size);
        ElementsResponse<SectionDTO> res = sectionService.getAllSections(pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<ElementsResponse<SectionDTO>> getSectionsByName(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        ElementsResponse<SectionDTO> res = sectionService.getSectionsByName(name, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionDTO> getSectionById(@PathVariable Long id) {
        SectionDTO section = sectionService.getSectionById(id);
        return ResponseEntity.ok(section);
    }

    @PostMapping
    public ResponseEntity<SectionDTO> createSection(@RequestBody SectionDTO sectionDTO) {
        SectionDTO createdSection = sectionService.createSection(sectionDTO);
        return ResponseEntity.ok(createdSection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionDTO> updateSection(@PathVariable Long id, @RequestBody SectionDTO sectionDTO) {
        SectionDTO updatedSection = sectionService.updateSection(id, sectionDTO);
        return ResponseEntity.ok(updatedSection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResponse<Boolean>> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.ok(new DefaultResponse<Boolean>(true));
    }
}
