package com.example.demo.services;

import com.example.demo.Entities.Course;
import com.example.demo.Params.CreateCourseParam;
import com.example.demo.Params.Response;
import com.example.demo.repos.CourseRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A lot of mocks, this can be a challenge especially when a lot of autowiring is in the class,
 * just to test the unique action point of the business logic. This actually a part of the ANEMIC programming style.
 * In the application context,
 *
 * an approach to mitigate this is to use Domain Driven Design (Hexagon Development),
 * which allows easier separation of the business logic and supporting infrastructure.
 * the challenge with it, is that it's super expressive (a lot of code) and be an over kill most time,
 * but it's used for reliable microservices architecture especially because it allows emitting events from domain aggregates.
 */

@SpringBootTest
public class CourseServiceTest {
    @MockBean
    private CourseRepo courseRepo;

    @Autowired
    private CourseService courseService;

    private ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Test
    void findById() {

        // should throw exception for not found.
        UUID id = UUID.randomUUID();
        Mockito.when(courseRepo.findById(id)).thenReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> courseService.findById(id)).hasMessageEndingWith("not found");

        // should return the value.
        var course = Course.builder().id(id).build();
        Mockito.when(courseRepo.findById(id)).thenReturn(Optional.of(course));

        Response response = courseService.findById(id);
        assertThat(response.getData()).isInstanceOf(Course.class);
        System.out.println(mapper.writeValueAsString(response));

    }

    @SneakyThrows
    @Test
    void create() {

        // missing courceCode during creation.
        assertThatThrownBy(() -> courseService.create(CreateCourseParam.builder().build())).hasMessageEndingWith("profile creation");

        var mockStudent = Course.builder()
                .id(UUID.randomUUID())
                .courseCode("EEE231")
                .courseName("Engineering Mathematices")
                .totalCandidates(20)
                .registeredCandidates(0)
                .createAt(new Date())
                .updatedAt(new Date())
                .build();

        // case in which the course code already exist before.
        var request = CreateCourseParam.builder().courseCode("EEE231").build();
        Mockito.when(courseRepo.findOneByCourseCode(request.getCourseCode())).thenReturn(Optional.of(mockStudent));
        assertThatThrownBy(() -> courseService.create(request)).hasMessageEndingWith("please contact IT.");

        CreateCourseParam courseRequest = CreateCourseParam.builder()
                .courseCode("EEE232")
                .courseName("Engineering Mathematics")
                .totalCandidates(20)
                .build();

        Mockito.when(courseRepo.findOneByCourseCode(courseRequest.getCourseCode())).thenReturn(Optional.ofNullable(null));

        // should return the value.
        Response response = courseService.create(courseRequest);
        assertThat(response.getData()).isInstanceOf(Course.class);
        System.out.println(mapper.writeValueAsString(response));

    }

}
