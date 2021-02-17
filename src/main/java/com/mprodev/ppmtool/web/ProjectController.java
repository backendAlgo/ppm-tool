package com.mprodev.ppmtool.web;

import com.mprodev.ppmtool.domain.Project;
import com.mprodev.ppmtool.exceptions.ProjectIdException;
import com.mprodev.ppmtool.services.MapValidationErrorService;
import com.mprodev.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/* Mirshod created on 2/12/2021 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService projectService;
    private final MapValidationErrorService mapValidationErrorService;

    @Autowired
    public ProjectController(ProjectService projectService,
                             MapValidationErrorService mapValidationErrorService) {
        this.projectService = projectService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("")
    public ResponseEntity<?> store(@Valid @RequestBody Project project,
                                   BindingResult result) {
        var errorMap =
                mapValidationErrorService.mapValidationErrorService(result);
        if (errorMap != null) return errorMap;

        return new ResponseEntity<>(projectService.saveOrUpdateProject(project),
                HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {
        var project = projectService.findProjectByIdentifier(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("")
    public Iterable<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
        projectService.deleteProjectByIdentifier(projectId);
        return new ResponseEntity<>("Project with ID: '"
                + projectId + "' was deleted", HttpStatus.OK);
    }
}
