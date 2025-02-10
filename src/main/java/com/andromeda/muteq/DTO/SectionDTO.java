package com.andromeda.muteq.DTO;

import java.util.Set;

public record SectionDTO(
    Long id,
    String name,
    String description,
    Long image_id,
    String image,
    Set<ItemDTO> items
) { }
