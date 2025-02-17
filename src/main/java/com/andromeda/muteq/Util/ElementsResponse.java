package com.andromeda.muteq.Util;

import java.util.Set;

public record ElementsResponse<T>(Set<T> elements, Long count) { }
