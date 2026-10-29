package dev.kaldiroglu.dp.introduction.problems.complexObject;

import java.util.List;

public class NewUserRequest {
    private String username;
    private boolean status;
    private String email;
    private boolean approved;
    private String listName;
    private String name;
    private String lastName;
    private String organization;
    private List<String> roleREntityIds;
    private List<String> categoryEntityIds;
    private List<String> configurationEntityIds;
    private boolean internal;

    private String header;
    private String remoteAddr;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<String> getRoleREntityIds() {
        return roleREntityIds;
    }

    public void setRoleREntityIds(List<String> roleREntityIds) {
        this.roleREntityIds = roleREntityIds;
    }

    public List<String> getCategoryEntityIds() {
        return categoryEntityIds;
    }

    public void setCategoryEntityIds(List<String> categoryEntityIds) {
        this.categoryEntityIds = categoryEntityIds;
    }

    public List<String> getConfigurationEntityIds() {
        return configurationEntityIds;
    }

    public void setConfigurationEntityIds(List<String> configurationEntityIds) {
        this.configurationEntityIds = configurationEntityIds;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public String getHeader(String header) {
        return header;
    }

    public void setHeader(String header, String value) {
//        ...
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }
}
