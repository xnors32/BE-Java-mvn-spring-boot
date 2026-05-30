package com.inventorilab.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebResponse<T> {
    private int code;
    private String status;
    private String message;
    private T data;
    private PagingResponse paging;
}
