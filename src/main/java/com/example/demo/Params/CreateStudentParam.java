package com.example.demo.Params;

import com.example.demo.Enums.Gender;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class CreateStudentParam {

    @NotNull
    private String lastName;

    @NotNull
    private String firstName;

    @NotNull
    private Gender gender;

    @NotNull
    private String courseNo;

    @Email
    @NotNull
    private String email;

}
