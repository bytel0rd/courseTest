package com.example.demo.Params;

import com.example.demo.Entities.Student;
import com.example.demo.Enums.Gender;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Builder
@Data
public class StudentViewModel {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;

    public static StudentViewModel fromStudentEntity(Student student) {
        return StudentViewModel.builder()
                .id(student.getId())
                .email(student.getEmail())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .gender(student.getGender())
                .build();
    }
}
