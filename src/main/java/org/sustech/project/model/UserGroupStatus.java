package org.sustech.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_group_status")
public class UserGroupStatus {
    @Id
    @Column(columnDefinition = "TEXT", name = "group_ids")
    private String groupIds;

    private Boolean status;

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String group) {
        this.groupIds = group;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
