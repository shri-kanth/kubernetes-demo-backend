package com.graphqljava.example.todolistapp.model.db;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DELETED")
    private boolean deleted;

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "MODIFIED_ON")
    private Date modifiedOn;
}
