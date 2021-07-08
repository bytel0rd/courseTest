package com.example.demo.Repositories;

import com.example.demo.Entities.Course;
import com.example.demo.repos.CourseRepo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Database Integration Test for CourseRepo
 */
@SpringBootTest
public class CourseRepoTest {

    @Autowired
    private CourseRepo courseRepo;

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    @SneakyThrows
    void createCourse()  {

        Course unsaveCourse = Course.builder()
                .id(UUID.randomUUID())
                .courseCode("EEE231")
                .courseName("Engineering Mathematices")
                .totalCandidates(20)
                .registeredCandidates(0)
                .createAt(new Date())
                .updatedAt(new Date())
                .build();

        System.out.println(mapper.writeValueAsString(unsaveCourse));


        Course course = courseRepo.save(unsaveCourse);

        assertThat(course).isInstanceOf(Course.class);
    }

    @SneakyThrows
    @Test
    void findOneById() {

        UUID uniqueId = UUID.fromString("31ffc569-8b75-431b-8e4b-6b490841d1da");

        Optional<Course> course = courseRepo.findById(uniqueId);

        assertThat(course.isPresent()).isTrue();
        System.out.println(mapper.writeValueAsString(course.get()));
    }


    @SneakyThrows
    @Test
    void findOneByCourseCode() {

        String courseCode = "EEE231";

        Optional<Course> course = courseRepo.findOneByCourseCode(courseCode);

        assertThat(course.isPresent()).isTrue();
        System.out.println(mapper.writeValueAsString(course.get()));

    }

}
