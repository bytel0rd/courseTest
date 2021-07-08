package com.example.demo.services;

import com.example.demo.Entities.CourseReg;
import com.example.demo.Params.RegisterStudentParam;
import com.example.demo.Params.Response;
import com.example.demo.repos.CourseRegRepo;
import com.example.demo.repos.CourseRepo;
import com.example.demo.repos.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.NotNull;

@Service
public class CourseRegService {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private CourseRegRepo courseRegRepo;

    public Response<CourseReg> register(@NotNull RegisterStudentParam registerReq) {

        if (registerReq == null || registerReq.getEmail() == null || registerReq.getCourseCode() == null) {
            String errorMessage = String.format("missing required field for course registration");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        var studentInfo = studentRepo.findByEmail(registerReq.getEmail());
        if (studentInfo.isEmpty()) {
            String errorMessage = String.format("Student with %s email, does not exist", registerReq.getEmail());
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, errorMessage);
        }

        var courseInfo = courseRepo.findOneByCourseCode(registerReq.getCourseCode());
        if (courseInfo.isEmpty()) {
            String errorMessage = String.format("Course with %s code, does not exist", registerReq.getCourseCode());
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, errorMessage);
        }

        var previousCode = courseRegRepo
                .findOneByCourseCodeAndStudentEmail(registerReq.getCourseCode(), registerReq.getEmail());
        if (previousCode.isPresent()) {
            String errorMessage = String.format("%s is already registered for %s course already", registerReq.getEmail(), registerReq.getCourseCode());
            throw new HttpClientErrorException(HttpStatus.CONFLICT, errorMessage);
        }

        var courseReg = CourseReg.builder()
                .courseCode(registerReq.getCourseCode())
                .studentEmail(registerReq.getEmail())
                .build();

        courseReg = courseRegRepo.save(courseReg);

        return new Response<>(courseReg);
    }


}
