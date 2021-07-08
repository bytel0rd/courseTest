package com.example.demo.Repositories;

import com.example.demo.Entities.Student;
import com.example.demo.Enums.Gender;
import com.example.demo.repos.StudentRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
//import asser


/**
 * Database Integration Test for StudentRepo
 */

@SpringBootTest
public class StudentRepoTest {

    @Autowired
    private StudentRepo studentRepo;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void createStudent() {

        String randomEmail = UUID.randomUUID().toString().substring(0, 5);

        Student unSavedStudent = Student.builder()
                .id(UUID.randomUUID())
                .firstName("Oyegoke")
                .lastName("Abiodun")
                .email(randomEmail + "-test@gmail.com")
                .gender(Gender.MALE)
                .build();

        Student student = studentRepo.save(unSavedStudent);

        assertThat(student).isInstanceOf(Student.class);
    }


    @SneakyThrows
    @Test
    void findOneById() {

        UUID uniqueId = UUID.fromString("b9ba2919-8a15-42a5-b7b9-58235e31dbfe");

        Optional<Student> student = studentRepo.findById(uniqueId);

        assertThat(student.isPresent()).isTrue();
        System.out.println(mapper.writeValueAsString(student.get()));

    }

    @SneakyThrows
    @Test
    void findByEmail() {

        Optional<Student> student = studentRepo.findByEmail("ogundijoabiodun@gmail.com");

        assertThat(student.isPresent()).isTrue();
        System.out.println(mapper.writeValueAsString(student.get()));
    }

    @SneakyThrows
    @Test
    void findAll() {

        ArrayList students = studentRepo.findAll();

        assertThat(students).isNotNull();
        System.out.println(mapper.writeValueAsString(students));

    }

}
