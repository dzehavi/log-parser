package com.dz.logparser.repository;

import com.dz.logparser.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This Repository is used for inserting items of type LogEntry into the h2 database.
 */
@Repository
public interface LogRepository extends JpaRepository<LogEntry,Long> {
}