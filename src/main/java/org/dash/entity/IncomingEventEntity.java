package org.dash.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "incoming_event")
public class IncomingEventEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String data;

    private String processing_errors;

    private ZonedDateTime timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getProcessing_errors() {
        return processing_errors;
    }

    public void setProcessing_errors(String processing_errors) {
        this.processing_errors = processing_errors;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
