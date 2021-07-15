package com.mprodev.ppmtool.repositories;
/* Mirshod created on 2/12/2021 */

import com.mprodev.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    Project findByProjectIdentifier(String project);

    @Override
    Iterable<Project> findAll();
}
