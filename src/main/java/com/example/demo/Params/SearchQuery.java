package com.example.demo.Params;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SearchQuery {
    private String query;
    private int pageNumber;
    private int pageSize;
}
