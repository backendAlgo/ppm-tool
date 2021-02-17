package com.mprodev.ppmtool.web;

import com.mprodev.ppmtool.domain.ProjectTask;
import com.mprodev.ppmtool.services.MapValidationErrorService;
import com.mprodev.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/* Mirshod created on 2/16/2021 */
@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {
    private final ProjectTaskService projectTaskService;
    private final MapValidationErrorService mapValidationErrorService;

    @Autowired
    public BacklogController(ProjectTaskService projectTaskService,
                             MapValidationErrorService mapValidationErrorService) {
        this.projectTaskService = projectTaskService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result,
                                            @PathVariable String backlog_id) {
        var errorMap =
                mapValidationErrorService.mapValidationErrorService(result);
        if (errorMap != null) return errorMap;

        var projectTask1 =
                projectTaskService.addProjectTask(backlog_id, projectTask);
        return new ResponseEntity<>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(
            @PathVariable String backlog_id) {
        return projectTaskService.findBacklogById(backlog_id);
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id,
                                            @PathVariable String pt_id) {
        var projectTask =
                projectTaskService.findPTByProjectSequence(backlog_id, pt_id);
        return new ResponseEntity<>(projectTask, HttpStatus.OK);
    }
}
