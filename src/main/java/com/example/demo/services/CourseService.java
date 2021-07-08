package com.example.demo.services;

import com.example.demo.Entities.Course;
import com.example.demo.Params.*;
import com.example.demo.repos.CourseRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class CourseService {
    @Autowired
    private CourseRepo courseRepo;

    public Response<Course> findById(UUID courseId) {

        if (courseId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid course Id provided.");
        }

        var dbResponse = courseRepo.findById(courseId);

        if (dbResponse.isPresent()) {
            return new Response(dbResponse.get());
        }

        String errorMessage = String.format("Student with ID: %s not found", courseId.toString());
        throw new HttpClientErrorException(HttpStatus.NOT_FOUND, errorMessage);

    }


    public Response create(@NotNull CreateCourseParam courseRequest) {

        if (courseRequest == null || courseRequest.getCourseCode() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Please provide required information to create course profile");
        }

        // prevent inconsistent codes like # EEE232, eE232
        courseRequest.setCourseCode(courseRequest.getCourseCode().toUpperCase());

        // optimistic validation to see if courseCode has already been previously created to send meaningful duplicate Error
        var previousCourse = courseRepo.findOneByCourseCode(courseRequest.getCourseCode());

        if (previousCourse.isPresent()) {
            String errorMessage = String.format("A course with %s code, as already been created", courseRequest.getCourseCode());
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        Course course = Course.builder()
                .id(UUID.randomUUID())
                .courseName(courseRequest.getCourseName())
                .courseCode(courseRequest.getCourseCode())
                .totalCandidates(courseRequest.getTotalCandidates())
                .createAt(new Date())
                .updatedAt(new Date())
                .build();

        course = courseRepo.save(course);

        return new Response(course, HttpStatus.CREATED);
    }


    public Response update(@NotNull Course updateRequest) {

        if (updateRequest == null || updateRequest.getId() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Please provide required course information update.");
        }

        updateRequest.setCourseCode(updateRequest.getCourseCode().toUpperCase());

        // Pre-emptive Assumptions #2.
        // validate the tatalAmount of course students allowed. (It should not be lower than existing signup students count).
        // neither the amountOfStudents signedUp are lower than totalAmount. An of this cases, is an inconsistent state.
        // this can happen unknowingly when a section of the code updates the totalAmount without amount signedUp.
        // if the validation happens, though it has to be done everywhere where the model is been updated

        if (updateRequest.getRegisteredCandidates() > updateRequest.getTotalCandidates()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Inconsistent state suggested, total candidates cannot be less than registerd candidates");
        }

        // the nature of save here, also creates, to prevent multiple creation
        // depending on the system, a optimistic look up should make the process failed.
        var course = courseRepo.save(updateRequest);

        return new Response(course, HttpStatus.CREATED);
    }

    public PaginatedResponse findByQuery(SearchQuery query) {

        ArrayList courses = courseRepo.findAll();

        return PaginatedResponse.builder()
                .data(courses)
                .pageNumber(query.getPageNumber())
                .pageSize(query.getPageSize())
                .build();

    }

}