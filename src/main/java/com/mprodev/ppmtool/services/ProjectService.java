package com.mprodev.ppmtool.services;

import com.mprodev.ppmtool.domain.Backlog;
import com.mprodev.ppmtool.domain.Project;
import com.mprodev.ppmtool.exceptions.ProjectIdException;
import com.mprodev.ppmtool.repositories.BacklogRepository;
import com.mprodev.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

/* Mirshod created on 2/12/2021 */
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final BacklogRepository backlogRepository;
    @Autowired
    public ProjectService(ProjectRepository projectRepository, BacklogRepository backlogRepository) {
        this.projectRepository = projectRepository;
        this.backlogRepository = backlogRepository;
    }

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId() == null) {
                var backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            } else {
                project.setBacklog(backlogRepository
                        .findByProjectIdentifier(project
                                .getProjectIdentifier()));
            }

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project ID '"
                    + project.getProjectIdentifier().toUpperCase()
                    + "' already exist");
        }
    }

    public Project findProjectByIdentifier(String projectId) {
        var project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project ID '"
                    + projectId.toUpperCase()
                    + "' doesn't exist");
        }
        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Cannot delete with ID '" +
                    projectId + "'. This project does not exist ");
        }
        projectRepository.delete(project);
    }
}
