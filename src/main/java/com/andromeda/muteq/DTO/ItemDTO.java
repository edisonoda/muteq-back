package com.andromeda.muteq.DTO;

public record ItemDTO(
    Long id,
    String name,
    String manufacturer,
    String description,
    Integer year,
    String image,
    Long category,
    Long section
) { }
