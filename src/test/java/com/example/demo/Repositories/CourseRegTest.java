package com.example.demo.Repositories;

import com.example.demo.Entities.CourseReg;
import com.example.demo.repos.CourseRegRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Database Integration Test for CourseRegRepo
 */
@SpringBootTest
public class CourseRegTest {

    @Autowired
    private CourseRegRepo courseRegRepo;

    private ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Test
    void TestCreateCourse()  {
        var courseReg = CourseReg.builder()
                .courseCode("GNS202")
                .studentEmail("abeytest@gmail.com")
                .build();

        courseReg = courseRegRepo.save(courseReg);
        assertThat(courseReg).isNotNull();
        System.out.println(mapper.writeValueAsString(courseReg));

    }

    @SneakyThrows
    @Test
    void TestFindByCourseCodeAndEmail() {
        var savedCourse = courseRegRepo
                .findOneByCourseCodeAndStudentEmail("GNS202", "abeytest@gmail.com");

        assertThat(savedCourse.isPresent()).isTrue();
        System.out.println(mapper.writeValueAsString(savedCourse.get()));

    }

}
