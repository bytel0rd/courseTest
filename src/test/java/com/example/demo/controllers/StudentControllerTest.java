package com.example.demo.controllers;

import com.example.demo.Controllers.CourseController;
import com.example.demo.Controllers.StudentController;
import com.example.demo.Enums.Gender;
import com.example.demo.Params.CreateCourseParam;
import com.example.demo.Params.CreateStudentParam;
import com.example.demo.Params.Response;
import com.example.demo.services.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * StudentController Integration Test
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentControllerTest {

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentService studentService;

    private ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Test
    void TestFindById() {

        assertThatThrownBy(() -> studentController.findById("")).hasMessageEndingWith("valid id");

        Response response = studentController.findById("b9ba2919-8a15-42a5-b7b9-58235e31dbfe");
        assertThat(response).isNotNull();
        System.out.println(mapper.writeValueAsString(response));
    }

    @SneakyThrows
    @Test
    void TestCreateStudent() {

      var createRequest= CreateStudentParam.builder()
                .firstName("Oyegoke")
                .lastName("Abiodun")
                .gender(Gender.MALE)
                .email("abiodun@mail.com")
                .courseNo("STB-INC-EE232")
                .build();

        Response response = studentController.create(createRequest);
        assertThat(response).isNotNull();
        System.out.println(mapper.writeValueAsString(response));
    }

}
