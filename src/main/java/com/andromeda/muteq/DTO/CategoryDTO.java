package com.andromeda.muteq.DTO;

import java.util.Set;

public record CategoryDTO(
    Long id,
    String name,
    String description,
    Long image_id,
    String image,
    Set<Long> items
) { }
