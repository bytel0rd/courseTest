package com.example.demo.repos;

import com.example.demo.Entities.CourseReg;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface CourseRegRepo extends CrudRepository<CourseReg, UUID> {

    Optional<CourseReg> findOneByCourseCodeAndStudentEmail(String courseCode, String email);

    CourseReg save(CourseReg entity);
}
