package com.andromeda.muteq.DTO;


public record ItemResponseDTO(
    Long id,
    String name,
    String manufacturer,
    String description,
    Integer year,
    String image,
    CategoryDTO category,
    SectionDTO section
) { }
