package com.mprodev.ppmtool.services;

import com.mprodev.ppmtool.domain.Backlog;
import com.mprodev.ppmtool.domain.Project;
import com.mprodev.ppmtool.domain.User;
import com.mprodev.ppmtool.exceptions.ProjectIdException;
import com.mprodev.ppmtool.exceptions.ProjectNotFoundException;
import com.mprodev.ppmtool.repositories.BacklogRepository;
import com.mprodev.ppmtool.repositories.ProjectRepository;
import com.mprodev.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

/* Mirshod created on 2/12/2021 */
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final BacklogRepository backlogRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          BacklogRepository backlogRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.backlogRepository = backlogRepository;
        this.userRepository = userRepository;
    }

    public Project saveOrUpdateProject(Project project, String username) {
        if (project.getId() != null) {
            projectRepository.findById(project.getId()).ifPresentOrElse((p) -> {
                if (!p.getProjectLeader().equals(username))
                    throw new ProjectNotFoundException("Project not found for this user");
            }, () -> {
                throw new ProjectIdException("Project ID '"
                        + project.getProjectIdentifier()
                        + "' doesn't exist");
            });
        }
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
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

    public Project findProjectByIdentifier(String projectId, String username) {
        var project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project ID '"
                    + projectId.toUpperCase()
                    + "' doesn't exist");
        }
        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }
}
