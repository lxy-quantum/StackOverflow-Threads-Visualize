package org.sustech.project.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.sustech.project.model.UserGroupStatus;

import java.util.List;

@Repository
public interface UserGroupStatusRepository extends JpaRepository<UserGroupStatus, String> {

    List<UserGroupStatus> findByStatusFalse();

    @Modifying
    @Transactional
    @Query("UPDATE UserGroupStatus u SET u.status = true WHERE u.groupIds = :groupIds")
    void updateStatusToTrueByGroupId(String groupIds);

    @Modifying
    @Transactional
    @Query("UPDATE UserGroupStatus u SET u.status = false WHERE u.groupIds = :groupIds")
    void updateStatusToFalseByGroupId(String groupIds);

}
