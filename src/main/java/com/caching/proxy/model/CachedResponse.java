package com.caching.proxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CachedResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private byte[] body;
    private HttpHeaders headers;
    private int statusCodeValue;

}
