package dev.kaldiroglu.dp.introduction.problems.complexObject.user;

import java.util.List;

public class Role {
    private String resourceId;
    private boolean deleted;
    private String name;
    private List<UserRole> userRoles;
    private List<RolePrivilege> privileges;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public List<RolePrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<RolePrivilege> privileges) {
        this.privileges = privileges;
    }
}