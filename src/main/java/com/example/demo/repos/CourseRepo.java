package com.example.demo.repos;

import com.example.demo.Entities.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepo extends CrudRepository<Course, UUID> {

    Optional<Course> findById(UUID courseId);

    <S extends Course> S save(S entity);

    ArrayList<Course> findAll();

    Optional<Course> findOneByCourseCode(String courseCode);
}
