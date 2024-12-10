package org.sustech.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sustech.project.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {}
