package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    @Query("SELECT g FROM Grade g WHERE g.exam.id = :examId")
    List<Grade> findByExamId(@Param("examId") Long examId);

    void deleteByExamId(Long examId);
}
