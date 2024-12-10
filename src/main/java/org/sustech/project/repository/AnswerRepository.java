package org.sustech.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sustech.project.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {}
