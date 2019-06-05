package com.graphqljava.example.todolistapp.model.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "LIST_ENTITY")
@Entity
@Data
public class ListEntity extends AbstractBaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "POSITION")
    private int position;
}
