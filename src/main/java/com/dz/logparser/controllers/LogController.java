package com.dz.logparser.controllers;

import com.dz.logparser.model.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.dz.logparser.LogParser.DATE_TIME_FORMAT;

/**
 * This RestController performs a multi-Predicate query that uses tbe passed arguments.
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    EntityManager entityManager;

    @RequestMapping(value = "/search")
    public ResponseEntity<List<LogEntry>> getLogEntries (
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {

        try {

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<LogEntry> criteriaQuery = criteriaBuilder.createQuery(LogEntry.class);
            Root<LogEntry> itemRoot = criteriaQuery.from(LogEntry.class);

            // prepare s list of Predicates for the query
            ArrayList<Predicate> predicates = new ArrayList<>();
            if (level != null) {
                predicates.add(criteriaBuilder.equal(itemRoot.get("level"), level));
            }
            if (text != null) {
                predicates.add(criteriaBuilder.like(
                        // case insensitive
                        criteriaBuilder.lower(itemRoot.get("message")), "%"+text.toLowerCase()+"%"));
            }
            if(from!=null && to != null){
                Timestamp fromTimestamp = new Timestamp(DATE_TIME_FORMAT.parse(from).getTime());
                Timestamp toTimestamp = new Timestamp(DATE_TIME_FORMAT.parse(to).getTime());
                predicates.add(criteriaBuilder.between(itemRoot.get("timestamp"), fromTimestamp, toTimestamp));
            }

            Predicate[] predicatesArray = new Predicate[predicates.size()];
            for (int i = 0; i < predicates.size(); i++) {
                predicatesArray[i] = predicates.get(i);
            }

            // run the query
            criteriaQuery.select(itemRoot).where(predicatesArray);
            List<LogEntry> logEntries = entityManager.createQuery(criteriaQuery).getResultList();

            return ResponseEntity.ok(logEntries);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
