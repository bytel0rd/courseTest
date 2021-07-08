package com.example.demo.repos;

import com.example.demo.Entities.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface StudentRepo extends CrudRepository<Student, UUID> {

    Optional<Student> findById(UUID studentId);

    Optional<Student> findByEmail(String email);

    <S extends Student> S save(S entity);

//  I won't use this in production at all.
    ArrayList<Student> findAll();
}

