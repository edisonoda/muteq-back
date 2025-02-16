package com.andromeda.muteq.DTO;

import java.util.Set;

public record SectionDTO(
    Long id,
    String name,
    String description,
    String image,
    Set<Long> items
) { }
