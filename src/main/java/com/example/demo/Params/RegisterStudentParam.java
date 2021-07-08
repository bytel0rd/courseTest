package com.example.demo.Params;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class RegisterStudentParam {

    @NotNull
    @Email
    private String email;
    @NotNull
    private String courseCode;

}
