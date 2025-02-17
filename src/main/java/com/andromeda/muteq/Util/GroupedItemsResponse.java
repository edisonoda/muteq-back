package com.andromeda.muteq.Util;

import java.util.Set;

import com.andromeda.muteq.DTO.ItemDTO;

public record GroupedItemsResponse(Set<ItemDTO> elements, Long count, String name) { }
