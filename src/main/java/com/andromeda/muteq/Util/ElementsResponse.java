package com.andromeda.muteq.Util;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementsResponse<T> {
    public Set<T> elements;
    public Long count;

    public ElementsResponse(Set<T> elements, Long count) {
        this.elements = elements;
        this.count = count;
    }
}
