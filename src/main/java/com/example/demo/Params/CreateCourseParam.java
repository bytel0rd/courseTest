package com.example.demo.Params;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@Builder
public class CreateCourseParam {
    private String courseCode;
    private String courseName;
    @Min(5)
    private int totalCandidates = 20;
}
