package org.sustech.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sustech.project.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {}
