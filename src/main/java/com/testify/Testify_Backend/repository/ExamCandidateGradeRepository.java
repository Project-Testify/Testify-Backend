package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.ExamCandidateGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamCandidateGradeRepository extends JpaRepository<ExamCandidateGrade, String> {

//    write a query to get the exam candidate grade details along with exam name and candidate name
    @Query("SELECT ecg FROM ExamCandidateGrade ecg")
    List<ExamCandidateGrade> getExamCandidateGradeDetails();
}
