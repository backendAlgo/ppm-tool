package com.mprodev.ppmtool.services;
/* Mirshod created on 2/16/2021 */

import com.mprodev.ppmtool.domain.Backlog;
import com.mprodev.ppmtool.domain.ProjectTask;
import com.mprodev.ppmtool.exceptions.ProjectNotFoundException;
import com.mprodev.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProjectTaskService {
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectService projectService;

    @Autowired
    public ProjectTaskService(ProjectTaskRepository projectTaskRepository,
                              ProjectService projectService) {
        this.projectTaskRepository = projectTaskRepository;
        this.projectService = projectService;
    }

    public ProjectTask addProjectTask(String projectIdentifier,
                                      ProjectTask projectTask,
                                      String username) {
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
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


    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {
        projectService.findProjectByIdentifier(id, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id,
                                               String pt_id,
                                               String username) {
        //make sure we are searching on existing backlog
        projectService.findProjectByIdentifier(backlog_id, username);
        var projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        //make sure we are searching on existing project task
        if (projectTask == null)
            throw new ProjectNotFoundException("Project Task '" + pt_id + "' not found");
        //make sure project task and backlog correspond each other
        if (!projectTask.getProjectIdentifier().equals(backlog_id))
            throw new ProjectNotFoundException("Project Task '" + pt_id + "'" +
                    " does not exist in project: '" + backlog_id + "'");
        return projectTask;
    }


    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {
        findPTByProjectSequence(backlog_id, pt_id, username);
        return projectTaskRepository.save(updatedTask);
    }


    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTaskRepository.delete(projectTask);
    }
}
