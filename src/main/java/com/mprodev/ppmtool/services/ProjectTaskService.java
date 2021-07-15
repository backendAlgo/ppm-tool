package com.mprodev.ppmtool.services;
/* Mirshod created on 2/16/2021 */

import com.mprodev.ppmtool.domain.Backlog;
import com.mprodev.ppmtool.domain.ProjectTask;
import com.mprodev.ppmtool.exceptions.ProjectNotFoundException;
import com.mprodev.ppmtool.repositories.BacklogRepository;
import com.mprodev.ppmtool.repositories.ProjectRepository;
import com.mprodev.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {
    private final BacklogRepository backlogRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectTaskService(BacklogRepository backlogRepository,
                              ProjectTaskRepository projectTaskRepository,
                              ProjectRepository projectRepository) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectRepository = projectRepository;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);
            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            //e.g IDPRO-1,IDPRO-2
            projectTask.setProjectIdentifier(projectIdentifier);
            //INITIAL priority when priority is null
            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }
            if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
                projectTask.setStatus("TODO");
            }

            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException("Project not found");
        }

    }

    public Iterable<ProjectTask> findBacklogById(String id) {
        var project = projectRepository.findByProjectIdentifier(id);
        if (project == null) {
            throw new ProjectNotFoundException("Project with ID: '" + id +
                    "' does not exist");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id,
                                               String pt_id) {
        //make sure we are searching on existing backlog
        var backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog == null)
            throw new ProjectNotFoundException("Project with ID: '" + backlog_id +
                    "' does not exist");
        var projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        //make sure we are searching on existing project task
        if (projectTask == null)
            throw new ProjectNotFoundException("Project Task '" + pt_id + "' not found");
        //make sure project task and backlog correspond each other
        if (!projectTask.getProjectIdentifier().equals(backlog_id))
            throw new ProjectNotFoundException("Project Task '" + pt_id + "'" +
                    " does not exist in project: '" + backlog_id + "'");
        return projectTaskRepository.findByProjectSequence(pt_id);
    }

    public ProjectTask updateByProjectSequence(ProjectTask updateTask,
                                               String backlog_id,
                                               String pt_id) {
        var projectTask =
                findPTByProjectSequence(backlog_id, pt_id);
        projectTask = updateTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id) {
        var projectTask = findPTByProjectSequence(backlog_id, pt_id);

        projectTaskRepository.delete(projectTask);
    }

}
