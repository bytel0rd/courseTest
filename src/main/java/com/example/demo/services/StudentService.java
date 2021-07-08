package com.example.demo.services;

import com.example.demo.Entities.Student;
import com.example.demo.Params.*;
import com.example.demo.repos.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

// BadCode #1: EntityModel should not be the ViewModel sent to the frontend.
// it causes issues later on, because frontends becomes tied to the backend model,
// changes on the backend can't be done because it might break some views on the frontend.
// Swift(IOS) -> Especially.

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    public Response<StudentViewModel> findById(UUID studentId) {

        if (studentId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid student Id provided.");
        }

        var dbResponse = this.studentRepo.findById(studentId);

        if (dbResponse.isPresent()) {
            Response response = new Response(StudentViewModel.fromStudentEntity(dbResponse.get()));
            return response;
        }

        String errorMessage = String.format("Student with ID: %s not found", studentId.toString());
        throw new HttpClientErrorException(HttpStatus.NOT_FOUND,  errorMessage);

    }


    public Response create(@NotNull CreateStudentParam studentRequest) {

        // can't happen, Just in case the not NotNull was ever removed.
        // in general will have preferred custom validation to send human friendly messages.
        if (studentRequest == null || studentRequest.getEmail() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Please provide required information for student profile creation");
        }

        studentRequest.setEmail(studentRequest.getEmail().toLowerCase());

        // presumptive operation using email to prevent duplicate operation.
        var existingStudent = this.studentRepo.findByEmail(studentRequest.getEmail());
        if (existingStudent.isPresent()) {
            String errorMessage = String.format("Please %s as already been register, if you are having any issues or complain, please contact IT.", studentRequest.getEmail());
            throw new HttpClientErrorException(HttpStatus.CONFLICT, errorMessage);
        }

        Student student = Student.builder()
                .id(UUID.randomUUID())
                .firstName(studentRequest.getFirstName())
                .lastName(studentRequest.getLastName())
                .email(studentRequest.getEmail())
                .gender(studentRequest.getGender())
                .createAt(new Date())
                .updatedAt(new Date())
                .build();

        student = this.studentRepo.save(student);

        //#Doing my BadCode#1 Here.
        return new Response(student, HttpStatus.CREATED);
    }

    public PaginatedResponse findByQuery(SearchQuery query) {

        // Please note, i won't use this in production!!!, it's just bad in performance for me.
        // I will most likely switch it out with store procedures for relationalDB or aggregation pipeline in MongoDB.
        // using procedures allows me to guarantee the query performance outside the main application.

        ArrayList students = this.studentRepo.findAll();

        return PaginatedResponse.builder()
                .data(students)
                .pageNumber(query.getPageNumber())
                .pageSize(query.getPageSize())
                .build();

    }
}
