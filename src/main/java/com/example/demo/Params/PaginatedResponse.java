package com.example.demo.Params;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class PaginatedResponse<T> {

    private int pageNumber;
    private int pageSize;

    private ArrayList<T> data;

}
