package com.dz.logparser.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * A POJO representing a log entry, for fetching/inserting data from/into h2 database.
 */
@Entity
@Table(name = "LOG_ENTRY")
public class LogEntry {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String level;
    @Column
    private Timestamp timestamp;
    @Column
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
