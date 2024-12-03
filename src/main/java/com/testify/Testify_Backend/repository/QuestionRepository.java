package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Essay;
import com.testify.Testify_Backend.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{
    @Query("SELECT q FROM Question q WHERE q.exam.id = :examId AND q.isDeleted = false")
    List<Question> findAllActiveQuestionsByExamId(@Param("examId") long examId);

    Optional<Question> findByIdAndIsDeletedFalse(Long id);
    List<Question> findByExamId(Long examId);

    @Query("SELECT e FROM Essay e WHERE e.exam.id = :examId")
    List<Essay> findAllEssaysByExamId(@Param("examId") Long examId);

}
