package com.andromeda.muteq.Util;

import java.util.Set;

import com.andromeda.muteq.DTO.ItemResponseDTO;

public record GroupedItemsResponse(Set<ItemResponseDTO> elements, Long count, String name) { }
