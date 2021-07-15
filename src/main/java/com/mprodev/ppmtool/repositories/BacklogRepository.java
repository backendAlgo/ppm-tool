package com.mprodev.ppmtool.repositories;

import com.mprodev.ppmtool.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/* Mirshod created on 2/16/2021 */
@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {
    Backlog findByProjectIdentifier(String Identifier);
}
