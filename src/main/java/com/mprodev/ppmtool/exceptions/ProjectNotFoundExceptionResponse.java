package com.mprodev.ppmtool.exceptions;
/* Mirshod created on 2/17/2021 */

public class ProjectNotFoundExceptionResponse {
    private String projectNotFound;

    public ProjectNotFoundExceptionResponse(String projectNotFound) {
        this.projectNotFound = projectNotFound;
    }

    public String getProjectNotFound() {
        return projectNotFound;
    }

    public void setProjectNotFound(String projectNotFound) {
        this.projectNotFound = projectNotFound;
    }
}
