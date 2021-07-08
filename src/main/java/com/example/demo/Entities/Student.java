package com.example.demo.Entities;

import com.example.demo.Enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false) // EdgeCase:#1 to prevent duplicate user signup entries of the course.
    @Email // EdgeCase:#1.2 guide against any random user sending invalid email, especially if not called from the create user flow.
    private String email;

    @Column(nullable = false)
    private Gender gender;

    @CreatedDate
    private Date createAt;
    @LastModifiedDate
    private Date updatedAt;
}
