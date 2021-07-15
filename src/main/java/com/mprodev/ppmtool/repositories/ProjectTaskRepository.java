package com.mprodev.ppmtool.repositories;

import com.mprodev.ppmtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/* Mirshod created on 2/16/2021 */
@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
    List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);

    ProjectTask findByProjectSequence(String sequence);
}
