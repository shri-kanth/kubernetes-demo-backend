package com.graphqljava.example.todolistapp.model.db;

import lombok.Data;

import javax.persistence.*;


@Table(name = "ITEM")
@Entity
@Data
public class Item extends AbstractBaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "POSITION")
    private int position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LIST_ID", nullable = false)
    private ListEntity listEntity;
}
