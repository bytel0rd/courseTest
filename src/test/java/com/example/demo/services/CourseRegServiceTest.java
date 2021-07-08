package com.example.demo.services;

import com.example.demo.Entities.Course;
import com.example.demo.Entities.CourseReg;
import com.example.demo.Entities.Student;
import com.example.demo.Enums.Gender;
import com.example.demo.Params.RegisterStudentParam;
import com.example.demo.Params.Response;
import com.example.demo.repos.CourseRegRepo;
import com.example.demo.repos.CourseRepo;
import com.example.demo.repos.StudentRepo;
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

@SpringBootTest
public class CourseRegServiceTest {
    @MockBean
    private CourseRepo courseRepo;

    @MockBean
    private StudentRepo studentRepo;

    @MockBean
    private CourseRegRepo courseRegRepo;

    @Autowired
    private CourseRegService courseRegService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void TestRegisterCourseForMissingStudent() {

        var testRequest = RegisterStudentParam.builder()
                .courseCode("EEE232")
                .email("ogundijoabiodun@gmail.com")
                .build();

        Mockito.when(studentRepo.findByEmail(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> courseRegService.register(testRequest)).hasMessageEndingWith("email, does not exist");
    }

    @Test
    void TestRegisterCourseForMissingCourse() {

        var testRequest = RegisterStudentParam.builder()
                .courseCode("EEE232")
                .email("ogundijoabiodun@gmail.com")
                .build();

        Mockito.when(studentRepo.findByEmail(Mockito.anyString())).thenReturn(Optional.of(Student.builder().build()));
        Mockito.when(courseRepo.findOneByCourseCode(Mockito.anyString())).thenReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> courseRegService.register(testRequest)).hasMessageEndingWith("code, does not exist");
    }

    @SneakyThrows
    @Test
    void TestRegisterCourseForStudent() {

        var testRequest = RegisterStudentParam.builder()
                .courseCode("EEE232")
                .email("ogundijoabiodun@gmail.com")
                .build();

        var mockedCourseReg = CourseReg.builder()
                .courseCode("GNS202")
                .studentEmail("abeytest@gmail.com")
                .build();

        var mockStudent = Student.builder()
                .id(UUID.randomUUID())
                .firstName("Oyegoke")
                .lastName("Abiodun")
                .email("ogundijoabiodun@gmail.com")
                .gender(Gender.MALE)
                .build();

        var mockedCourse = Course.builder()
                .id(UUID.randomUUID())
                .courseCode("EEE231")
                .courseName("Engineering Mathematices")
                .totalCandidates(20)
                .registeredCandidates(0)
                .createAt(new Date())
                .updatedAt(new Date())
                .build();

        Mockito.when(studentRepo.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockStudent));
        Mockito.when(courseRepo.findOneByCourseCode(Mockito.anyString())).thenReturn(Optional.of(mockedCourse));
        Mockito.when(courseRegRepo.findOneByCourseCodeAndStudentEmail(testRequest.getCourseCode(), testRequest.getEmail())).thenReturn(Optional.ofNullable(null));

        Mockito.when(courseRegRepo.save(Mockito.any(CourseReg.class))).thenReturn(mockedCourseReg);

        Response response =  courseRegService.register(testRequest);
        assertThat(response.getData()).isInstanceOf(CourseReg.class);
        System.out.println(mapper.writeValueAsString(response));

    }

    @SneakyThrows
    @Test
    void TestRegisterDuplicateCourseForStudent() {

        var testRequest = RegisterStudentParam.builder()
                .courseCode("EEE232")
                .email("ogundijoabiodun@gmail.com")
                .build();

        Mockito.when(studentRepo.findByEmail(Mockito.anyString())).thenReturn(Optional.of(new Student()));
        Mockito.when(courseRepo.findOneByCourseCode(Mockito.anyString())).thenReturn(Optional.of(new Course()));
        Mockito.when(courseRegRepo.findOneByCourseCodeAndStudentEmail(testRequest.getCourseCode(), testRequest.getEmail())).thenReturn(Optional.of(new CourseReg()));

        assertThatThrownBy(() -> courseRegService.register(testRequest)).hasMessageEndingWith("already");
    }

}
