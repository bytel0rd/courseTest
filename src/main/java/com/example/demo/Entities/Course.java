package com.example.demo.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    //    using UUID should be avoided with mssql, because it's stored as binary to allow table joining.
    // on postgres it's string though.

    @Id
    private UUID id;

    @Column(nullable = false)
    private String courseName;

    // intentially validating this here (Another DB Guard).
    @Column(unique = true)
    private String courseCode;

    @Column(nullable = false)
    private int totalCandidates = 20;

    @Column(nullable = false)
    private int registeredCandidates = 0;

    @CreatedDate
    private Date createAt;
    @LastModifiedDate
    private Date updatedAt;

}
