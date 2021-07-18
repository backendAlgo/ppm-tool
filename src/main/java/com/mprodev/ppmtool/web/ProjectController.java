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
import java.security.Principal;

/* Mirshod created on 2/12/2021 */
@RestController
@RequestMapping("/api/project")
@CrossOrigin
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
                                   BindingResult result,
                                   Principal principal) {
        var errorMap =
                mapValidationErrorService.mapValidationErrorService(result);
        if (errorMap != null) return errorMap;

        return new ResponseEntity<>(projectService.saveOrUpdateProject(project,principal.getName()),
                HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {
        var project = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("")
    public Iterable<Project> getAllProjects(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId,
                                           Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<>("Project with ID: '"
                + projectId + "' was deleted", HttpStatus.OK);
    }
}
