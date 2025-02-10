package com.andromeda.muteq.DTO;

public record ItemDTO(
    Long id,
    String name,
    String manufacturer,
    String description,
    Integer year,
    Long image_id,
    String image,
    CategoryDTO category,
    SectionDTO section
) { }
