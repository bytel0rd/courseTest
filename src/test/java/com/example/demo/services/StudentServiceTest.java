package com.example.demo.services;

import com.example.demo.Entities.Student;
import com.example.demo.Enums.Gender;
import com.example.demo.Params.CreateStudentParam;
import com.example.demo.Params.Response;
import com.example.demo.Params.StudentViewModel;
import com.example.demo.repos.StudentRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class StudentServiceTest {

    @MockBean
    private StudentRepo studentRepo;

    @Autowired
    private StudentService studentSvc;

    private ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Test
    void findById() {

    // should throw exception for not found.
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> this.studentSvc.findById(id)).hasMessageEndingWith("not found");

        var mockStudent = Student.builder()
                .id(UUID.randomUUID())
                .firstName("Oyegoke")
                .lastName("Abiodun")
                .email("ogundijoabiodun@gmail.com")
                .gender(Gender.MALE)
                .build();

        // should return the value.
        Mockito.when(studentRepo.findById(mockStudent.getId())).thenReturn(Optional.of(mockStudent));
       Response response = this.studentSvc.findById(mockStudent.getId());
       assertThat(response.getData()).isInstanceOf(StudentViewModel.class);
       System.out.println(mapper.writeValueAsString(response));
    }

    @SneakyThrows
    @Test
    void createAndTriggerException() {

        assertThatThrownBy(() -> this.studentSvc.create(CreateStudentParam.builder().build())).hasMessageEndingWith("profile creation");

        var mockStudent = Student.builder()
                .id(UUID.randomUUID())
                .firstName("Oyegoke")
                .lastName("Abiodun")
                .email("ogundijoabiodun@gmail.com")
                .gender(Gender.MALE)
                .build();

        var request = CreateStudentParam.builder().email("abiodun@mail.com").build();
        Mockito.when(studentRepo.findByEmail(request.getEmail())).thenReturn(Optional.of(mockStudent));
        assertThatThrownBy(() -> this.studentSvc.create(request)).hasMessageEndingWith("please contact IT.");

    }

    @SneakyThrows
    @Test
    void createStudentSuccessFully() {

        var mockStudent = Student.builder()
                .id(UUID.randomUUID())
                .firstName("Oyegoke")
                .lastName("Abiodun")
                .email(UUID.randomUUID().toString().substring(0, 5) + "@test.com")
                .gender(Gender.MALE)
                .build();

        Mockito.when(studentRepo.save(Mockito.any(Student.class))).thenReturn(mockStudent);

        CreateStudentParam studentRequest = CreateStudentParam.builder()
                .firstName("Oyegoke")
                .lastName("Abiodun")
                .gender(Gender.MALE)
                .email("abiodun@mail.com")
                .courseNo("STB-INC-EE232")
                .build();

        Response response = this.studentSvc.create(studentRequest);
        assertThat(response.getData()).isInstanceOf(Student.class);
        System.out.println(mapper.writeValueAsString(response));

    }

}
