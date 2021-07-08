package com.example.demo.Controllers;

import com.example.demo.Params.*;
import com.example.demo.services.CourseRegService;
import com.example.demo.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/api/v1/courses")
@RestController()
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRegService courseRegService;

    @GetMapping(path = "/{id}")
    public Response findById(@PathVariable(name = "id") String courseId) {
        UUID id;

        try {
            id = UUID.fromString(courseId);
        } catch (Exception e ) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Please provide a valid course id");
        }

        return this.courseService.findById(id);
    }

    @PostMapping()
    public Response create(@Valid @RequestBody CreateCourseParam course) {
        return this.courseService.create(course);
    }

    @GetMapping()
    PaginatedResponse findAll(@RequestParam(value = "", required = false) String search) {
        var searchQuery = SearchQuery.builder()
                .build();
        return this.courseService.findByQuery(searchQuery);
    }

    @PostMapping(path = "/register")
    Response registerCourse(@RequestBody() RegisterStudentParam registerRequest) {
        return courseRegService.register(registerRequest);
    }

}
