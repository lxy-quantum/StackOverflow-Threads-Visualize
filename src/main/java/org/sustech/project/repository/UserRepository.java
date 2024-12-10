package org.sustech.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sustech.project.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
