package com.andromeda.muteq.Util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultResponse<T> {
    public T data;
    public String message;

    public DefaultResponse(T data) {
        this.data = data;
        this.message = "";
    }

    public DefaultResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
