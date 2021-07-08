package com.example.demo.Controllers;

import com.example.demo.Params.CreateStudentParam;
import com.example.demo.Params.PaginatedResponse;
import com.example.demo.Params.Response;
import com.example.demo.Params.SearchQuery;
import com.example.demo.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(value = "/api/v1/students")
@RestController()
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}")
    public Response findById(@PathVariable String id) {

        UUID pathId;

        try {
            pathId = UUID.fromString(id);
        } catch (Exception e ) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Please provide a valid id");
        }

        return this.studentService.findById(pathId);
    }

    @PostMapping()
    public Response create(@Valid @RequestBody CreateStudentParam student) {
        return this.studentService.create(student);
    }

    @GetMapping
    PaginatedResponse findAll(@RequestParam(defaultValue = "") String search ) {

        var searchQuery = SearchQuery.builder().build();

        return this.studentService.findByQuery(searchQuery);
    }

}
